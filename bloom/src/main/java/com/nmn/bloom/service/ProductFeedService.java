package com.nmn.bloom.service;

import com.nmn.bloom.domain.ProductFeed;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ProductFeed}.
 */
public interface ProductFeedService {
    /**
     * Save a productFeed.
     *
     * @param productFeed the entity to save.
     * @return the persisted entity.
     */
    ProductFeed save(ProductFeed productFeed);

    /**
     * Updates a productFeed.
     *
     * @param productFeed the entity to update.
     * @return the persisted entity.
     */
    ProductFeed update(ProductFeed productFeed);

    /**
     * Partially updates a productFeed.
     *
     * @param productFeed the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductFeed> partialUpdate(ProductFeed productFeed);

    /**
     * Get all the productFeeds.
     *
     * @return the list of entities.
     */
    List<ProductFeed> findAll();
    /**
     * Get all the ProductFeed where Cabinet is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ProductFeed> findAllWhereCabinetIsNull();

    /**
     * Get all the productFeeds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductFeed> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductFeed> findOne(Long id);

    /**
     * Delete the "id" productFeed.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
