package com.nmn.bloom.service.impl;

import com.nmn.bloom.domain.Comments;
import com.nmn.bloom.repository.CommentsRepository;
import com.nmn.bloom.service.CommentsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comments}.
 */
@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsServiceImpl.class);

    private final CommentsRepository commentsRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public Comments save(Comments comments) {
        log.debug("Request to save Comments : {}", comments);
        return commentsRepository.save(comments);
    }

    @Override
    public Comments update(Comments comments) {
        log.debug("Request to save Comments : {}", comments);
        return commentsRepository.save(comments);
    }

    @Override
    public Optional<Comments> partialUpdate(Comments comments) {
        log.debug("Request to partially update Comments : {}", comments);

        return commentsRepository
            .findById(comments.getId())
            .map(existingComments -> {
                if (comments.getProduct() != null) {
                    existingComments.setProduct(comments.getProduct());
                }
                if (comments.getDate() != null) {
                    existingComments.setDate(comments.getDate());
                }
                if (comments.getComment() != null) {
                    existingComments.setComment(comments.getComment());
                }
                if (comments.getImage() != null) {
                    existingComments.setImage(comments.getImage());
                }
                if (comments.getImageContentType() != null) {
                    existingComments.setImageContentType(comments.getImageContentType());
                }

                return existingComments;
            })
            .map(commentsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comments> findAll() {
        log.debug("Request to get all Comments");
        return commentsRepository.findAllWithEagerRelationships();
    }

    public Page<Comments> findAllWithEagerRelationships(Pageable pageable) {
        return commentsRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comments> findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        return commentsRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        commentsRepository.deleteById(id);
    }
}
