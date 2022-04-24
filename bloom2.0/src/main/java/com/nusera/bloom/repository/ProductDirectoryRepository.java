package com.nusera.bloom.repository;

import com.nusera.bloom.domain.ProductDirectory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductDirectory entity.
 */
@Repository
public interface ProductDirectoryRepository extends ProductDirectoryRepositoryWithBagRelationships, JpaRepository<ProductDirectory, Long> {
    default Optional<ProductDirectory> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<ProductDirectory> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<ProductDirectory> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
