/**
 * 
 */
package com.pms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSProject;

/**
 * @author jifang
 *
 */
public interface PMSProjectRepo extends JpaRepository<PMSProject, Long> {
    List<PMSProject> findAllByCompanyId(Long companyId);
}
