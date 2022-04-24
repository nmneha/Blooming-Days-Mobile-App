package com.nusera.bloom.repository;

import com.nusera.bloom.domain.Cabinet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cabinet entity.
 */
@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {
    @Query("select cabinet from Cabinet cabinet where cabinet.user.login = ?#{principal.username}")
    List<Cabinet> findByUserIsCurrentUser();

    default Optional<Cabinet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cabinet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cabinet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct cabinet from Cabinet cabinet left join fetch cabinet.user",
        countQuery = "select count(distinct cabinet) from Cabinet cabinet"
    )
    Page<Cabinet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct cabinet from Cabinet cabinet left join fetch cabinet.user")
    List<Cabinet> findAllWithToOneRelationships();

    @Query("select cabinet from Cabinet cabinet left join fetch cabinet.user where cabinet.id =:id")
    Optional<Cabinet> findOneWithToOneRelationships(@Param("id") Long id);
}
