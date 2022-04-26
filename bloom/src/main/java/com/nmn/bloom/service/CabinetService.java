package com.nmn.bloom.service;

import com.nmn.bloom.domain.Cabinet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cabinet}.
 */
public interface CabinetService {
    /**
     * Save a cabinet.
     *
     * @param cabinet the entity to save.
     * @return the persisted entity.
     */
    Cabinet save(Cabinet cabinet);

    /**
     * Updates a cabinet.
     *
     * @param cabinet the entity to update.
     * @return the persisted entity.
     */
    Cabinet update(Cabinet cabinet);

    /**
     * Partially updates a cabinet.
     *
     * @param cabinet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cabinet> partialUpdate(Cabinet cabinet);

    /**
     * Get all the cabinets.
     *
     * @return the list of entities.
     */
    List<Cabinet> findAll();

    /**
     * Get all the cabinets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cabinet> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cabinet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cabinet> findOne(Long id);

    /**
     * Delete the "id" cabinet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
