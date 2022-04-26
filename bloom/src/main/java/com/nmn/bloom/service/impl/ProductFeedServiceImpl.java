package com.nmn.bloom.service.impl;

import com.nmn.bloom.domain.ProductFeed;
import com.nmn.bloom.repository.ProductFeedRepository;
import com.nmn.bloom.service.ProductFeedService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductFeed}.
 */
@Service
@Transactional
public class ProductFeedServiceImpl implements ProductFeedService {

    private final Logger log = LoggerFactory.getLogger(ProductFeedServiceImpl.class);

    private final ProductFeedRepository productFeedRepository;

    public ProductFeedServiceImpl(ProductFeedRepository productFeedRepository) {
        this.productFeedRepository = productFeedRepository;
    }

    @Override
    public ProductFeed save(ProductFeed productFeed) {
        log.debug("Request to save ProductFeed : {}", productFeed);
        return productFeedRepository.save(productFeed);
    }

    @Override
    public ProductFeed update(ProductFeed productFeed) {
        log.debug("Request to save ProductFeed : {}", productFeed);
        return productFeedRepository.save(productFeed);
    }

    @Override
    public Optional<ProductFeed> partialUpdate(ProductFeed productFeed) {
        log.debug("Request to partially update ProductFeed : {}", productFeed);

        return productFeedRepository
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
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductFeed> findAll() {
        log.debug("Request to get all ProductFeeds");
        return productFeedRepository.findAllWithEagerRelationships();
    }

    public Page<ProductFeed> findAllWithEagerRelationships(Pageable pageable) {
        return productFeedRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the productFeeds where Cabinet is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductFeed> findAllWhereCabinetIsNull() {
        log.debug("Request to get all productFeeds where Cabinet is null");
        return StreamSupport
            .stream(productFeedRepository.findAll().spliterator(), false)
            .filter(productFeed -> productFeed.getCabinet() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductFeed> findOne(Long id) {
        log.debug("Request to get ProductFeed : {}", id);
        return productFeedRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductFeed : {}", id);
        productFeedRepository.deleteById(id);
    }
}
