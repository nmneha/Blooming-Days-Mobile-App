package com.nmneha.bloom.repository;

import com.nmneha.bloom.domain.ProductDirectory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductDirectoryRepositoryWithBagRelationships {
    Optional<ProductDirectory> fetchBagRelationships(Optional<ProductDirectory> productDirectory);

    List<ProductDirectory> fetchBagRelationships(List<ProductDirectory> productDirectories);

    Page<ProductDirectory> fetchBagRelationships(Page<ProductDirectory> productDirectories);
}
