package com.nmneha.bloom.repository;

import com.nmneha.bloom.domain.ProductFeed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductFeed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductFeedRepository extends JpaRepository<ProductFeed, Long> {}
