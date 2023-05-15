/**
 * 
 */
package com.pms.controllers;

import java.util.ArrayList;
import java.util.List;

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

import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSTask;
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSProjectRepo;
import com.pms.repositories.PMSTaskRepo;
import com.pms.repositories.PMSUserRepo;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/tasks",
//            params={"project_id", "company_id"}, 
            produces="application/json", consumes="application/json")
public class PMSTaskController {
    @Autowired
    private PMSTaskRepo taskRepo;
    
    @Autowired
    private PMSProjectRepo projRepo;
    
    @Autowired
    private PMSUserRepo userRepo;
    
    @Autowired
    private EntityManagerFactory emf;
    
    /*@GetMapping(value="")
    public List<PMSTask> getTasks() {
        return taskRepo.findAll();
    }*/
    
    @GetMapping(value="")
    public List<PMSTask> getTasks(@RequestParam("projectId") Long projId) {
        if (!projRepo.existsById(projId)) {
            throw new ResourceNotFoundException("No project found with id=" + projId);
        }
        
        return taskRepo.findAllByProjectId(projId);
    }
    
    @GetMapping(value="/{taskId}")
    public PMSTask findTask(@PathVariable("taskId") Long taskId) {
        return taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
    }
    
    @PostMapping(value="")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSTask createTask(@RequestBody PMSTask task) {
        return taskRepo.save(task);
    }
    
    @PutMapping(value="/{taskId}")
    public PMSTask updateTask(@PathVariable("taskId") Long taskId, @RequestBody PMSTask task) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        ret.setAvatar(task.getAvatar());
        ret.setName(task.getName());
        ret.setProjectId(task.getProjectId());
        
        return taskRepo.save(ret);
    }
    
    @DeleteMapping(value="/{taskId}")
    public void deleteProject(@PathVariable("taskId") Long taskId) {
        
        /*if (!taskRepo(taskId)) {
            throw new ResourceNotFoundException("No task found with id=" + taskId);
        }*/
        
        taskRepo.findById(taskId);
        taskRepo.deleteById(taskId);
    }
    
    // dependencies
    @PostMapping(value="/{taskId}/dependencies/")
    public PMSTask addDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));

        for (long dependentId : dependentIds) {
            if (taskRepo.existsById(dependentId)) {
                    ret.addDependentTaskId(dependentId);
            }
        }
        
        taskRepo.save(ret);
        return ret;
    }
    
    @PutMapping(value="/{taskId}/dependencies/")
    public PMSTask setDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        ret.getDependentTaskIds().clear();
        for (long dependentId : dependentIds) {
            if (taskRepo.existsById(dependentId)) {
                ret.addDependentTaskId(dependentId);
            }
        }
        
        taskRepo.save(ret);
        return ret;
    }
    
    @DeleteMapping(value="/{taskId}/dependencies")
    public PMSTask removeDependentTasks(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> dependentIds) {
        PMSTask ret = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> oldDependentIds = ret.getDependentTaskIds();
        for (Long dependentId : dependentIds) {
            if (oldDependentIds.contains(dependentId)) {
                ret.removeDependentTaskId(dependentId);
            }
        }
        
        taskRepo.save(ret);
        return ret;
    }
    
    @GetMapping(value="/{taskId}/dependencies")
    public List<PMSTask> findDependentTasks(@PathVariable("taskId") long taskId) {
        List<PMSTask> tasks = new ArrayList<>();
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> taskIds = task.getDependentTaskIds();
        for (Long depId : taskIds) {
            PMSTask dep = taskRepo.findById(depId).orElseGet(null);
            if (dep != null) {
                tasks.add(dep);
            }
        }
        
        return tasks;
    }
    
    // assign
    @PostMapping(value="/{taskId}/users")
    public List<PMSUser> addUsers(@PathVariable("taskId") long taskId, 
                @RequestBody List<Long> userIds) {
        List<PMSUser> ret = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                task.addUserId(userId);
            }
        }
        taskRepo.save(task);
        userIds = task.getUserIds();
        for (Long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    @PutMapping(value="/{taskId}/users")
    public List<PMSUser> setUsers(@PathVariable("taskId") long taskId, 
                @RequestBody List<Long> userIds) {
        List<PMSUser> ret = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        task.getUserIds().clear();
        for (Long userId : userIds) {
            if (userRepo.existsById(userId)) {
                task.addUserId(userId);
            }
        }
        taskRepo.save(task);
        userIds = task.getUserIds();
        for (Long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
    
    @DeleteMapping(value="/{taskId}/users")
    public void removeUser(@PathVariable("taskId") long taskId, 
            @RequestBody List<Long> userIds) {
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> oldUserIds = task.getUserIds();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                oldUserIds.remove(userId);
            }
        }
        taskRepo.save(task);
    }
    
    @GetMapping(value="/{taskId}/users")
    public List<PMSUser> findUsers(@PathVariable("taskId") long taskId) {
        List<PMSUser> ret = new ArrayList<>();
        
        PMSTask task = taskRepo.findById(taskId).orElseThrow(
                ()->new ResourceNotFoundException("No task found with id=" + taskId));
        
        List<Long> userIds = task.getUserIds();
        for (long userId : userIds) {
            PMSUser user = userRepo.findById(userId).orElseGet(null);
            if (user != null) {
                ret.add(user);
            }
        }
        
        return ret;
    }
}
