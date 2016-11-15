package com.tsystem.tms.repository;

import com.tsystem.tms.domain.CustomerGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerGroup entity.
 */
@SuppressWarnings("unused")
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup,Long> {

}
