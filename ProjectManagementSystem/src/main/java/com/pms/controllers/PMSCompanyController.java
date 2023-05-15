/**
 * 
 */
package com.pms.controllers;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pms.controllers.exceptions.ResourceNotFoundException;
import com.pms.entities.PMSCompany;
import com.pms.repositories.PMSCompanyRepo;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/companies", 
            produces="application/json", 
            consumes="application/json")
public class PMSCompanyController {
    
    @Autowired
    private PMSCompanyRepo compRepo;
    
    @Autowired
    private EntityManagerFactory emf;
    
    @GetMapping(value="")
    public Object getCompanies() {
    	List<PMSCompany> ret = null;
    	
    	List<PMSCompany> companies = compRepo.findAll();
    	ret = companies;
    	
        return ret; 
    }
    
    @PostMapping(value="")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSCompany createCompany(@RequestBody PMSCompany comp) {
        return compRepo.save(comp);
    }
    
    @GetMapping(value="/{id}")
    public PMSCompany findCompany(@PathVariable("id") Long id) {
        return compRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No company found with id=" + id));
    }
    
    @PutMapping(value="/{id}")
    public PMSCompany updateCompany(@PathVariable("id") Long id, @RequestBody PMSCompany comp) {
        PMSCompany ret = compRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No company found with id=" + id));
        
        ret.setAvatar(comp.getAvatar());
        ret.setDesc(comp.getDesc());
        ret.setName(comp.getName());
        
        compRepo.save(ret);
        return ret;
    }
    
    @DeleteMapping(value="/{id}")
    public void deleteCompany(@PathVariable("id") Long id) {
        PMSCompany comp = compRepo.findById(id).orElse(null);
        if (comp != null) {
        	compRepo.deleteById(id);
        }
    }

}
