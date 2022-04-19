package com.nmneha.bloom.web.rest;

import com.nmneha.bloom.domain.Cabinet;
import com.nmneha.bloom.repository.CabinetRepository;
import com.nmneha.bloom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nmneha.bloom.domain.Cabinet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CabinetResource {

    private final Logger log = LoggerFactory.getLogger(CabinetResource.class);

    private static final String ENTITY_NAME = "cabinet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CabinetRepository cabinetRepository;

    public CabinetResource(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    /**
     * {@code POST  /cabinets} : Create a new cabinet.
     *
     * @param cabinet the cabinet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cabinet, or with status {@code 400 (Bad Request)} if the cabinet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cabinets")
    public ResponseEntity<Cabinet> createCabinet(@RequestBody Cabinet cabinet) throws URISyntaxException {
        log.debug("REST request to save Cabinet : {}", cabinet);
        if (cabinet.getId() != null) {
            throw new BadRequestAlertException("A new cabinet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cabinet result = cabinetRepository.save(cabinet);
        return ResponseEntity
            .created(new URI("/api/cabinets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cabinets/:id} : Updates an existing cabinet.
     *
     * @param id the id of the cabinet to save.
     * @param cabinet the cabinet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cabinet,
     * or with status {@code 400 (Bad Request)} if the cabinet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cabinet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cabinets/{id}")
    public ResponseEntity<Cabinet> updateCabinet(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cabinet cabinet)
        throws URISyntaxException {
        log.debug("REST request to update Cabinet : {}, {}", id, cabinet);
        if (cabinet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cabinet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cabinetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cabinet result = cabinetRepository.save(cabinet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cabinet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cabinets/:id} : Partial updates given fields of an existing cabinet, field will ignore if it is null
     *
     * @param id the id of the cabinet to save.
     * @param cabinet the cabinet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cabinet,
     * or with status {@code 400 (Bad Request)} if the cabinet is not valid,
     * or with status {@code 404 (Not Found)} if the cabinet is not found,
     * or with status {@code 500 (Internal Server Error)} if the cabinet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cabinets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cabinet> partialUpdateCabinet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cabinet cabinet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cabinet partially : {}, {}", id, cabinet);
        if (cabinet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cabinet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cabinetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cabinet> result = cabinetRepository
            .findById(cabinet.getId())
            .map(existingCabinet -> {
                if (cabinet.getProduct() != null) {
                    existingCabinet.setProduct(cabinet.getProduct());
                }
                if (cabinet.getProductId() != null) {
                    existingCabinet.setProductId(cabinet.getProductId());
                }

                return existingCabinet;
            })
            .map(cabinetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cabinet.getId().toString())
        );
    }

    /**
     * {@code GET  /cabinets} : get all the cabinets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cabinets in body.
     */
    @GetMapping("/cabinets")
    public List<Cabinet> getAllCabinets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Cabinets");
        return cabinetRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /cabinets/:id} : get the "id" cabinet.
     *
     * @param id the id of the cabinet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cabinet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cabinets/{id}")
    public ResponseEntity<Cabinet> getCabinet(@PathVariable Long id) {
        log.debug("REST request to get Cabinet : {}", id);
        Optional<Cabinet> cabinet = cabinetRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cabinet);
    }

    /**
     * {@code DELETE  /cabinets/:id} : delete the "id" cabinet.
     *
     * @param id the id of the cabinet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cabinets/{id}")
    public ResponseEntity<Void> deleteCabinet(@PathVariable Long id) {
        log.debug("REST request to delete Cabinet : {}", id);
        cabinetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
