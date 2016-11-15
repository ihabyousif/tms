package com.tsystem.tms.repository;

import com.tsystem.tms.domain.ScheduledPayement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduledPayement entity.
 */
@SuppressWarnings("unused")
public interface ScheduledPayementRepository extends JpaRepository<ScheduledPayement,Long> {

}
