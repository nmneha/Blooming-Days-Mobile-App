package com.nmn.bloom.web.rest;

import com.nmn.bloom.domain.ProductDirectory;
import com.nmn.bloom.repository.ProductDirectoryRepository;
import com.nmn.bloom.service.ProductDirectoryService;
import com.nmn.bloom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nmn.bloom.domain.ProductDirectory}.
 */
@RestController
@RequestMapping("/api")
public class ProductDirectoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductDirectoryResource.class);

    private static final String ENTITY_NAME = "productDirectory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductDirectoryService productDirectoryService;

    private final ProductDirectoryRepository productDirectoryRepository;

    public ProductDirectoryResource(
        ProductDirectoryService productDirectoryService,
        ProductDirectoryRepository productDirectoryRepository
    ) {
        this.productDirectoryService = productDirectoryService;
        this.productDirectoryRepository = productDirectoryRepository;
    }

    /**
     * {@code POST  /product-directories} : Create a new productDirectory.
     *
     * @param productDirectory the productDirectory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDirectory, or with status {@code 400 (Bad Request)} if the productDirectory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-directories")
    public ResponseEntity<ProductDirectory> createProductDirectory(@RequestBody ProductDirectory productDirectory)
        throws URISyntaxException {
        log.debug("REST request to save ProductDirectory : {}", productDirectory);
        if (productDirectory.getId() != null) {
            throw new BadRequestAlertException("A new productDirectory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDirectory result = productDirectoryService.save(productDirectory);
        return ResponseEntity
            .created(new URI("/api/product-directories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-directories/:id} : Updates an existing productDirectory.
     *
     * @param id the id of the productDirectory to save.
     * @param productDirectory the productDirectory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDirectory,
     * or with status {@code 400 (Bad Request)} if the productDirectory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDirectory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-directories/{id}")
    public ResponseEntity<ProductDirectory> updateProductDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDirectory productDirectory
    ) throws URISyntaxException {
        log.debug("REST request to update ProductDirectory : {}, {}", id, productDirectory);
        if (productDirectory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDirectory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDirectoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductDirectory result = productDirectoryService.update(productDirectory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDirectory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-directories/:id} : Partial updates given fields of an existing productDirectory, field will ignore if it is null
     *
     * @param id the id of the productDirectory to save.
     * @param productDirectory the productDirectory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDirectory,
     * or with status {@code 400 (Bad Request)} if the productDirectory is not valid,
     * or with status {@code 404 (Not Found)} if the productDirectory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productDirectory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-directories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductDirectory> partialUpdateProductDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductDirectory productDirectory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductDirectory partially : {}, {}", id, productDirectory);
        if (productDirectory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDirectory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productDirectoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductDirectory> result = productDirectoryService.partialUpdate(productDirectory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productDirectory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-directories} : get all the productDirectories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productDirectories in body.
     */
    @GetMapping("/product-directories")
    public List<ProductDirectory> getAllProductDirectories(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ProductDirectories");
        return productDirectoryService.findAll();
    }

    /**
     * {@code GET  /product-directories/:id} : get the "id" productDirectory.
     *
     * @param id the id of the productDirectory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDirectory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-directories/{id}")
    public ResponseEntity<ProductDirectory> getProductDirectory(@PathVariable Long id) {
        log.debug("REST request to get ProductDirectory : {}", id);
        Optional<ProductDirectory> productDirectory = productDirectoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDirectory);
    }

    /**
     * {@code DELETE  /product-directories/:id} : delete the "id" productDirectory.
     *
     * @param id the id of the productDirectory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-directories/{id}")
    public ResponseEntity<Void> deleteProductDirectory(@PathVariable Long id) {
        log.debug("REST request to delete ProductDirectory : {}", id);
        productDirectoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
