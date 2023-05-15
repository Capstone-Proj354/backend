/**
 * 
 */
package com.pms.controllers;

import java.util.List;

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
import com.pms.entities.PMSUser;
import com.pms.repositories.PMSUserRepo;

/**
 * @author jifang
 *
 */

@RestController
@RequestMapping(value="/users", 
        produces="application/json", consumes="application/json")
public class PMSUserController {
    
    @Autowired
    private PMSUserRepo userRepo;
    
    @GetMapping("")
    public List<PMSUser> getUsers() {
        return userRepo.findAll();
    }
    
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PMSUser createUser(@RequestBody PMSUser user) {
        return userRepo.save(user);
    }
    
    @GetMapping(value="/{id}")
    public PMSUser findUser(@PathVariable("id") Long id) {
        PMSUser ret = null;
        
        ret = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No user found with id="+id));
        
        return ret;
    }
    
    @PutMapping("/{id}")
    public PMSUser updateUser(@PathVariable("id") Long id, @RequestBody PMSUser user) {
        userRepo.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No user found with id=" + id));
        return userRepo.save(user);
    }
    
    @DeleteMapping(value="/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userRepo.findById(id);
        userRepo.deleteById(id);
    }

}
