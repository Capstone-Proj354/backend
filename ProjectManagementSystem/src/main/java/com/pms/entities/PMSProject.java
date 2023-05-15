/**
 * 
 */
package com.pms.entities;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jifang
 *
 */
@Entity
public class PMSProject {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="PROJ_ID")
    private long id;
    
    @Column(name="PROJ_NAME", nullable=false)
    @NotNull
    @Size(min=3)
    private String name;

    @Lob
    @Column(name = "PROJ_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "COMPANY_ID")
    @Min(0)
    private long companyId;
    
    @Column(name="PROJ_AVATAR")
    private String avatar;
    
    @ElementCollection
    @CollectionTable(name="DEPENDENT_PROJ_IDS")
    private List<Long> dependentProjectIds; 
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public  String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public long getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void addDependentProjectId(long projId) {
        if (!dependentProjectIds.contains(projId)) {
        	dependentProjectIds.add(projId);
        }
    }
    
    public void removeDependentProjectId(long projId) {
        if (dependentProjectIds.contains(projId)) {
        	dependentProjectIds.remove(projId);
        }
    }
    
    public List<Long> getDependentProjectIds() {
        return dependentProjectIds;
    }
}
