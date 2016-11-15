package com.tsystem.tms.repository;

import com.tsystem.tms.domain.CustomerProduct;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerProduct entity.
 */
@SuppressWarnings("unused")
public interface CustomerProductRepository extends JpaRepository<CustomerProduct,Long> {

}
