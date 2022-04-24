package com.nusera.bloom.web.rest;

import com.nusera.bloom.domain.ProductFeed;
import com.nusera.bloom.repository.ProductFeedRepository;
import com.nusera.bloom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nusera.bloom.domain.ProductFeed}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductFeedResource {

    private final Logger log = LoggerFactory.getLogger(ProductFeedResource.class);

    private static final String ENTITY_NAME = "productFeed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductFeedRepository productFeedRepository;

    public ProductFeedResource(ProductFeedRepository productFeedRepository) {
        this.productFeedRepository = productFeedRepository;
    }

    /**
     * {@code POST  /product-feeds} : Create a new productFeed.
     *
     * @param productFeed the productFeed to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productFeed, or with status {@code 400 (Bad Request)} if the productFeed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-feeds")
    public ResponseEntity<ProductFeed> createProductFeed(@RequestBody ProductFeed productFeed) throws URISyntaxException {
        log.debug("REST request to save ProductFeed : {}", productFeed);
        if (productFeed.getId() != null) {
            throw new BadRequestAlertException("A new productFeed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductFeed result = productFeedRepository.save(productFeed);
        return ResponseEntity
            .created(new URI("/api/product-feeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-feeds/:id} : Updates an existing productFeed.
     *
     * @param id the id of the productFeed to save.
     * @param productFeed the productFeed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeed,
     * or with status {@code 400 (Bad Request)} if the productFeed is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productFeed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-feeds/{id}")
    public ResponseEntity<ProductFeed> updateProductFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductFeed productFeed
    ) throws URISyntaxException {
        log.debug("REST request to update ProductFeed : {}, {}", id, productFeed);
        if (productFeed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeed.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductFeed result = productFeedRepository.save(productFeed);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeed.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-feeds/:id} : Partial updates given fields of an existing productFeed, field will ignore if it is null
     *
     * @param id the id of the productFeed to save.
     * @param productFeed the productFeed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productFeed,
     * or with status {@code 400 (Bad Request)} if the productFeed is not valid,
     * or with status {@code 404 (Not Found)} if the productFeed is not found,
     * or with status {@code 500 (Internal Server Error)} if the productFeed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-feeds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductFeed> partialUpdateProductFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductFeed productFeed
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductFeed partially : {}, {}", id, productFeed);
        if (productFeed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productFeed.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productFeedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductFeed> result = productFeedRepository
            .findById(productFeed.getId())
            .map(existingProductFeed -> {
                if (productFeed.getProduct() != null) {
                    existingProductFeed.setProduct(productFeed.getProduct());
                }
                if (productFeed.getProductId() != null) {
                    existingProductFeed.setProductId(productFeed.getProductId());
                }
                if (productFeed.getTarget() != null) {
                    existingProductFeed.setTarget(productFeed.getTarget());
                }
                if (productFeed.getPrimaryConcern() != null) {
                    existingProductFeed.setPrimaryConcern(productFeed.getPrimaryConcern());
                }

                return existingProductFeed;
            })
            .map(productFeedRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productFeed.getId().toString())
        );
    }

    /**
     * {@code GET  /product-feeds} : get all the productFeeds.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productFeeds in body.
     */
    @GetMapping("/product-feeds")
    public List<ProductFeed> getAllProductFeeds(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("cabinet-is-null".equals(filter)) {
            log.debug("REST request to get all ProductFeeds where cabinet is null");
            return StreamSupport
                .stream(productFeedRepository.findAll().spliterator(), false)
                .filter(productFeed -> productFeed.getCabinet() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ProductFeeds");
        return productFeedRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /product-feeds/:id} : get the "id" productFeed.
     *
     * @param id the id of the productFeed to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productFeed, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-feeds/{id}")
    public ResponseEntity<ProductFeed> getProductFeed(@PathVariable Long id) {
        log.debug("REST request to get ProductFeed : {}", id);
        Optional<ProductFeed> productFeed = productFeedRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(productFeed);
    }

    /**
     * {@code DELETE  /product-feeds/:id} : delete the "id" productFeed.
     *
     * @param id the id of the productFeed to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-feeds/{id}")
    public ResponseEntity<Void> deleteProductFeed(@PathVariable Long id) {
        log.debug("REST request to delete ProductFeed : {}", id);
        productFeedRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
