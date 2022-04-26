package com.nmn.bloom.service;

import com.nmn.bloom.domain.ProductDirectory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ProductDirectory}.
 */
public interface ProductDirectoryService {
    /**
     * Save a productDirectory.
     *
     * @param productDirectory the entity to save.
     * @return the persisted entity.
     */
    ProductDirectory save(ProductDirectory productDirectory);

    /**
     * Updates a productDirectory.
     *
     * @param productDirectory the entity to update.
     * @return the persisted entity.
     */
    ProductDirectory update(ProductDirectory productDirectory);

    /**
     * Partially updates a productDirectory.
     *
     * @param productDirectory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductDirectory> partialUpdate(ProductDirectory productDirectory);

    /**
     * Get all the productDirectories.
     *
     * @return the list of entities.
     */
    List<ProductDirectory> findAll();

    /**
     * Get all the productDirectories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDirectory> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productDirectory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDirectory> findOne(Long id);

    /**
     * Delete the "id" productDirectory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
