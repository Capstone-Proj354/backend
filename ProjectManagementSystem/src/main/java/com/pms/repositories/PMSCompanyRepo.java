/**
 * 
 */
package com.pms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSCompany;


/**
 * @author jifang
 *
 */
public interface PMSCompanyRepo extends JpaRepository<PMSCompany, Long>{

}
