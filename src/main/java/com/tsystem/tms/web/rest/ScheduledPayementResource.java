package com.tsystem.tms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tsystem.tms.domain.ScheduledPayement;
import com.tsystem.tms.service.ScheduledPayementService;
import com.tsystem.tms.web.rest.util.HeaderUtil;
import com.tsystem.tms.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ScheduledPayement.
 */
@RestController
@RequestMapping("/api")
public class ScheduledPayementResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledPayementResource.class);
        
    @Inject
    private ScheduledPayementService scheduledPayementService;
    
    /**
     * POST  /scheduled-payements : Create a new scheduledPayement.
     *
     * @param scheduledPayement the scheduledPayement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduledPayement, or with status 400 (Bad Request) if the scheduledPayement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scheduled-payements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduledPayement> createScheduledPayement(@Valid @RequestBody ScheduledPayement scheduledPayement) throws URISyntaxException {
        log.debug("REST request to save ScheduledPayement : {}", scheduledPayement);
        if (scheduledPayement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scheduledPayement", "idexists", "A new scheduledPayement cannot already have an ID")).body(null);
        }
        ScheduledPayement result = scheduledPayementService.save(scheduledPayement);
        return ResponseEntity.created(new URI("/api/scheduled-payements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scheduledPayement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scheduled-payements : Updates an existing scheduledPayement.
     *
     * @param scheduledPayement the scheduledPayement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduledPayement,
     * or with status 400 (Bad Request) if the scheduledPayement is not valid,
     * or with status 500 (Internal Server Error) if the scheduledPayement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scheduled-payements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduledPayement> updateScheduledPayement(@Valid @RequestBody ScheduledPayement scheduledPayement) throws URISyntaxException {
        log.debug("REST request to update ScheduledPayement : {}", scheduledPayement);
        if (scheduledPayement.getId() == null) {
            return createScheduledPayement(scheduledPayement);
        }
        ScheduledPayement result = scheduledPayementService.save(scheduledPayement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scheduledPayement", scheduledPayement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scheduled-payements : get all the scheduledPayements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scheduledPayements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/scheduled-payements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ScheduledPayement>> getAllScheduledPayements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduledPayements");
        Page<ScheduledPayement> page = scheduledPayementService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduled-payements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduled-payements/:id : get the "id" scheduledPayement.
     *
     * @param id the id of the scheduledPayement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduledPayement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/scheduled-payements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduledPayement> getScheduledPayement(@PathVariable Long id) {
        log.debug("REST request to get ScheduledPayement : {}", id);
        ScheduledPayement scheduledPayement = scheduledPayementService.findOne(id);
        return Optional.ofNullable(scheduledPayement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scheduled-payements/:id : delete the "id" scheduledPayement.
     *
     * @param id the id of the scheduledPayement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/scheduled-payements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScheduledPayement(@PathVariable Long id) {
        log.debug("REST request to delete ScheduledPayement : {}", id);
        scheduledPayementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scheduledPayement", id.toString())).build();
    }

}
