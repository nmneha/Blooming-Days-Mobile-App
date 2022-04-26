package com.nmn.bloom.service.impl;

import com.nmn.bloom.domain.ProductDirectory;
import com.nmn.bloom.repository.ProductDirectoryRepository;
import com.nmn.bloom.service.ProductDirectoryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductDirectory}.
 */
@Service
@Transactional
public class ProductDirectoryServiceImpl implements ProductDirectoryService {

    private final Logger log = LoggerFactory.getLogger(ProductDirectoryServiceImpl.class);

    private final ProductDirectoryRepository productDirectoryRepository;

    public ProductDirectoryServiceImpl(ProductDirectoryRepository productDirectoryRepository) {
        this.productDirectoryRepository = productDirectoryRepository;
    }

    @Override
    public ProductDirectory save(ProductDirectory productDirectory) {
        log.debug("Request to save ProductDirectory : {}", productDirectory);
        return productDirectoryRepository.save(productDirectory);
    }

    @Override
    public ProductDirectory update(ProductDirectory productDirectory) {
        log.debug("Request to save ProductDirectory : {}", productDirectory);
        return productDirectoryRepository.save(productDirectory);
    }

    @Override
    public Optional<ProductDirectory> partialUpdate(ProductDirectory productDirectory) {
        log.debug("Request to partially update ProductDirectory : {}", productDirectory);

        return productDirectoryRepository
            .findById(productDirectory.getId())
            .map(existingProductDirectory -> {
                if (productDirectory.getProduct() != null) {
                    existingProductDirectory.setProduct(productDirectory.getProduct());
                }
                if (productDirectory.getProductId() != null) {
                    existingProductDirectory.setProductId(productDirectory.getProductId());
                }
                if (productDirectory.getProductBrand() != null) {
                    existingProductDirectory.setProductBrand(productDirectory.getProductBrand());
                }
                if (productDirectory.getPrimaryIngredient() != null) {
                    existingProductDirectory.setPrimaryIngredient(productDirectory.getPrimaryIngredient());
                }

                return existingProductDirectory;
            })
            .map(productDirectoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDirectory> findAll() {
        log.debug("Request to get all ProductDirectories");
        return productDirectoryRepository.findAllWithEagerRelationships();
    }

    public Page<ProductDirectory> findAllWithEagerRelationships(Pageable pageable) {
        return productDirectoryRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDirectory> findOne(Long id) {
        log.debug("Request to get ProductDirectory : {}", id);
        return productDirectoryRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductDirectory : {}", id);
        productDirectoryRepository.deleteById(id);
    }
}
