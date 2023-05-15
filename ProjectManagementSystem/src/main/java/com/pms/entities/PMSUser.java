/**
 * 
 */
package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jifang
 *
 */
@Entity
public class PMSUser {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="USER_ID")
    private long id;
    
    @Column(name="USER_FNAME", nullable=false)
    @NotNull
    private String firstName;
    
    @Column(name="USER_LNAME", nullable=false)
    @NotNull
    private String lastName;
    
    @Column(name="USER_MNAME")
    private String midName;
    
    @Column(name="USER_EMAIL", nullable=false)
    @NotNull
    @Email
    private String email;
    
    @Column(name="USER_PASSWORD", nullable=false)
    @NotNull
    @Size(min=5)
    private String password;
    
    @Column(name="USER_ROLE", nullable=false)
    @NotNull
    private String role;
    
    @Column(name="USER_AVATAR")
    private String avatar;
    
    public long getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMidName() {
        return midName;
    }
    
    public void setMidName(String midName) {
        this.midName = midName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
