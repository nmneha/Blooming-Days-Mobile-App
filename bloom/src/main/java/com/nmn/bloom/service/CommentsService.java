package com.nmn.bloom.service;

import com.nmn.bloom.domain.Comments;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Comments}.
 */
public interface CommentsService {
    /**
     * Save a comments.
     *
     * @param comments the entity to save.
     * @return the persisted entity.
     */
    Comments save(Comments comments);

    /**
     * Updates a comments.
     *
     * @param comments the entity to update.
     * @return the persisted entity.
     */
    Comments update(Comments comments);

    /**
     * Partially updates a comments.
     *
     * @param comments the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Comments> partialUpdate(Comments comments);

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    List<Comments> findAll();

    /**
     * Get all the comments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Comments> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" comments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Comments> findOne(Long id);

    /**
     * Delete the "id" comments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
