/**
 * 
 */
package com.pms.controllers;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.entities.PMSProject;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.repositories.PMSCompanyRepo;
import com.pms.repositories.PMSProjectRepo;
import com.pms.repositories.PMSTaskRepo;
import com.pms.repositories.PMSUserRepo;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/projects",  
        consumes="application/json", 
        produces="application/json")
public class PMSProjectController {
	
	private static final String kDefaultTaskName = "__INNER_TASK__";
    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private PMSProjectRepo projRepo;
    
    @Autowired
    private PMSCompanyRepo compRepo;
    
    @Autowired
    private PMSTaskRepo taskRepo;
    
    @Autowired
    private PMSUserRepo userRepo;
    
    /*@GetMapping(value="")
    public List<PMSProject> getProjects()  {
        return projRepo.findAll();
    }*/
    
    // entity operation. 
    @GetMapping(value="")
    public List<PMSProject> getProjects(@RequestParam(value="companyId", required=true) Long companyId) {
    	
        if (!compRepo.existsById(companyId)) {
            throw new ResourceNotFoundException("No company found with id=" + companyId);
        }
        
        List<PMSProject> projects = projRepo.findAllByCompanyId(companyId);
        
        return projects;
    }
    
    @GetMapping(value="/{projectId}")
    public PMSProject findProject(@PathVariable("projectId") Long projectId) {
        return projRepo.findById(projectId).orElseThrow(()->
                    new ResourceNotFoundException("No project found with id=" + projectId));        
    }
    
    @PostMapping(value="")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSProject createProject(@RequestBody PMSProject project) {
    	PMSProject ret = projRepo.save(project);
        
        PMSTask innerTask = new PMSTask();
        innerTask.setProjectId(ret.getId());
        innerTask.setName(kDefaultTaskName);
        taskRepo.save(innerTask);
        
        return ret;
    }
    
    @PutMapping(value="/{projectId}")
    public PMSProject updateProject(@PathVariable("projectId") Long projectId, @RequestBody PMSProject project) {
        
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        ret.setAvatar(project.getAvatar());
        ret.setCompanyId(project.getCompanyId());
        ret.setDesc(project.getDesc());
        ret.setName(project.getName());
        
        return projRepo.save(ret);
    }
    
    @DeleteMapping(value="/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long projectId) {
        
        /*if (!projRepo.existsById(projectId)) {
            throw new ResourceNotFoundException("No project found with id=" + projectId
                     + " under company" + companyId);
        }*/
    	
        projRepo.findById(projectId);
        projRepo.deleteById(projectId);
    }
    
    // dependencies
    @PostMapping(value="/{projectId}/dependencies/")
    public PMSProject addDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));

        for (long dependentId : dependentIds) {
            if (projRepo.existsById(dependentId)) {
                    ret.addDependentProjectId(dependentId);
            }
        }
        
        projRepo.save(ret);
        return ret;
    }
    
    @PutMapping(value="/{projectId}/dependencies/")
    public PMSProject setDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        ret.getDependentProjectIds().clear();
        for (long dependentId : dependentIds) {
            if (projRepo.existsById(dependentId)) {
                ret.addDependentProjectId(dependentId);
            }
        }
        
        projRepo.save(ret);
        return ret;
    }
    
    @DeleteMapping(value="/{projectId}/dependencies")
    public PMSProject removeDependentProjects(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> dependentIds) {
        PMSProject ret = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> oldDependentIds = ret.getDependentProjectIds();
        for (Long dependentId : dependentIds) {
            if (oldDependentIds.contains(dependentId)) {
                ret.removeDependentProjectId(dependentId);
            }
        }
        
        projRepo.save(ret);
        return ret;
    }
    
    @GetMapping(value="/{projectId}/dependencies")
    public Object findDependentProjects(@PathVariable("projectId") long projectId) {
        List<PMSProject> projects = new ArrayList<PMSProject>();
        PMSProject proj = projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        List<Long> projIds = proj.getDependentProjectIds();
        for (Long projId : projIds) {
            PMSProject dep = projRepo.findById(projId).orElseGet(null);
            if (dep != null) {
                projects.add(dep);
            }
        }
        
        return projects;
    }
    
    // assign
    @PostMapping(value="/{projectId}/users")
    public List<PMSUser> addUsers(@PathVariable("projectId") long projectId, 
                @RequestBody List<Long> userIds) {
        List<PMSUser> ret = new ArrayList<>();
        
        projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                defaultTask.addUserId(userId);
            }
        }
        taskRepo.save(defaultTask);
        userIds = defaultTask.getUserIds();
        for (Long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    @PutMapping(value="/{projectId}/users")
    public List<PMSUser> setUsers(@PathVariable("projectId") long projectId, 
                @RequestBody List<Long> userIds) {
        List<PMSUser> ret = new ArrayList<>();
        
        projRepo.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("No project found with id=" + projectId));
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        
        defaultTask.getUserIds().clear();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                defaultTask.addUserId(userId);
            }
        }
        taskRepo.save(defaultTask);
        userIds = defaultTask.getUserIds();
        for (Long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    @DeleteMapping(value="/{projectId}/users")
    public void removeUser(@PathVariable("projectId") long projectId, 
            @RequestBody List<Long> userIds) {
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        List<Long> oldUserIds = defaultTask.getUserIds();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                oldUserIds.remove(userId);
            }
        }
        taskRepo.save(defaultTask);
    }
    
    @GetMapping(value="/{projectId}/users")
    public List<PMSUser> findUsers(@PathVariable("projectId") long projectId) {
        List<PMSUser> ret = new ArrayList<>();
        
        PMSTask defaultTask = taskRepo.findByName(kDefaultTaskName);
        List<Long> userIds = defaultTask.getUserIds();
        for (long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
}