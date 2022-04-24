package com.nusera.bloom.repository;

import com.nusera.bloom.domain.ProductFeed;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductFeed entity.
 */
@Repository
public interface ProductFeedRepository extends JpaRepository<ProductFeed, Long> {
    @Query("select productFeed from ProductFeed productFeed where productFeed.user.login = ?#{principal.username}")
    List<ProductFeed> findByUserIsCurrentUser();

    default Optional<ProductFeed> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductFeed> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductFeed> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct productFeed from ProductFeed productFeed left join fetch productFeed.user",
        countQuery = "select count(distinct productFeed) from ProductFeed productFeed"
    )
    Page<ProductFeed> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct productFeed from ProductFeed productFeed left join fetch productFeed.user")
    List<ProductFeed> findAllWithToOneRelationships();

    @Query("select productFeed from ProductFeed productFeed left join fetch productFeed.user where productFeed.id =:id")
    Optional<ProductFeed> findOneWithToOneRelationships(@Param("id") Long id);
}
