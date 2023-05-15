/**
 * 
 */
package com.pms.entities;

import java.util.List;
import java.util.ArrayList;

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
public class PMSTask {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="TASK_ID")
    private long id;
    
    @Column(name="TASK_NAME", nullable=false)
    @NotNull
    @Size(min=3)
    private String name;

    @Lob
    @Column(name = "TASK_DESC", columnDefinition="TEXT")
    private String desc;
    
    @Column(name = "PROJECT_ID")
    @Min(0)
    private long projectId;
    
    @Column(name="TASK_AVATAR")
    private String avatar;
    
    @ElementCollection
    @CollectionTable(name="DEPENDENT_TASK_IDS")
    private List<Long> dependentTaskIds;
    
    @ElementCollection
    @CollectionTable(name="TASK_USER_IDS")
    private List<Long> users;
    
    public PMSTask() {
    	dependentTaskIds = new ArrayList<Long>();
        users = new ArrayList<Long>();
    }
    
    public long getId() {
        return id;
    } 
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public void addDependentTaskId(long taskId) {
        if (!dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.add(taskId);
        }
    }
    
    public void removeDependentTaskId(long taskId) {
        if (dependentTaskIds.contains(taskId)) {
        	dependentTaskIds.remove(taskId);
        }
    }
    
    public List<Long> getDependentTaskIds() {
        return dependentTaskIds;
    }
    
    public void addUserId(long userId) {
        if (!users.contains(userId)) {
            users.add(userId);
        }
    }
    
    public void removeUserId(long userId) {
        if (users.contains(userId)) {
            users.remove(userId);
        }
    }

    public List<Long> getUserIds() {
        return users;
    }
    
}
