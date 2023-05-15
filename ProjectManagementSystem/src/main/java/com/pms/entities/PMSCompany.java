/**
 * 
 */
package com.pms.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jifang
 *
 */
@Entity
public class PMSCompany {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="COMPANY_ID")
    private long id;
    
    @Column(name="COMPANY_NAME", nullable=false)
    @NotNull
    @Size(min=3)
    private String name;

    @Lob
    @Column(name="COMPANY_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name="COMPANY_AVATAR")
    private String avatar;
    
    public long getId() {
    	return id;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getDesc() {
    	return desc;
    }
    
    public void setDesc(String desc) {
    	this.desc = desc;
    }
    
    public String getAvatar() {
    	return avatar;
    }
    
    public void setAvatar(String avatar) {
    	this.avatar = avatar;
    }
}
