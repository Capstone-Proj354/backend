/**
 * 
 */
package com.pms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSTask;

/**
 * @author jifang
 *
 */
public interface PMSTaskRepo extends JpaRepository<PMSTask, Long> {
    List<PMSTask> findAllByProjectId(long projId);
    PMSTask findByName(String taskName);
}
