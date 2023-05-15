/**
 * 
 */
package com.pms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.entities.PMSUser;

/**
 * @author jifang
 *
 */
public interface PMSUserRepo extends JpaRepository<PMSUser, Long>
{
}
