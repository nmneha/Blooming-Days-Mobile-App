package com.nusera.bloom.repository;

import com.nusera.bloom.domain.ProductDirectory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProductDirectoryRepositoryWithBagRelationshipsImpl implements ProductDirectoryRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ProductDirectory> fetchBagRelationships(Optional<ProductDirectory> productDirectory) {
        return productDirectory.map(this::fetchCabinets);
    }

    @Override
    public Page<ProductDirectory> fetchBagRelationships(Page<ProductDirectory> productDirectories) {
        return new PageImpl<>(
            fetchBagRelationships(productDirectories.getContent()),
            productDirectories.getPageable(),
            productDirectories.getTotalElements()
        );
    }

    @Override
    public List<ProductDirectory> fetchBagRelationships(List<ProductDirectory> productDirectories) {
        return Optional.of(productDirectories).map(this::fetchCabinets).orElse(Collections.emptyList());
    }

    ProductDirectory fetchCabinets(ProductDirectory result) {
        return entityManager
            .createQuery(
                "select productDirectory from ProductDirectory productDirectory left join fetch productDirectory.cabinets where productDirectory is :productDirectory",
                ProductDirectory.class
            )
            .setParameter("productDirectory", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<ProductDirectory> fetchCabinets(List<ProductDirectory> productDirectories) {
        return entityManager
            .createQuery(
                "select distinct productDirectory from ProductDirectory productDirectory left join fetch productDirectory.cabinets where productDirectory in :productDirectories",
                ProductDirectory.class
            )
            .setParameter("productDirectories", productDirectories)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
