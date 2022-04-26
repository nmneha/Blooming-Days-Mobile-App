package com.nmn.bloom.service.impl;

import com.nmn.bloom.domain.Cabinet;
import com.nmn.bloom.repository.CabinetRepository;
import com.nmn.bloom.service.CabinetService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cabinet}.
 */
@Service
@Transactional
public class CabinetServiceImpl implements CabinetService {

    private final Logger log = LoggerFactory.getLogger(CabinetServiceImpl.class);

    private final CabinetRepository cabinetRepository;

    public CabinetServiceImpl(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    @Override
    public Cabinet save(Cabinet cabinet) {
        log.debug("Request to save Cabinet : {}", cabinet);
        return cabinetRepository.save(cabinet);
    }

    @Override
    public Cabinet update(Cabinet cabinet) {
        log.debug("Request to save Cabinet : {}", cabinet);
        return cabinetRepository.save(cabinet);
    }

    @Override
    public Optional<Cabinet> partialUpdate(Cabinet cabinet) {
        log.debug("Request to partially update Cabinet : {}", cabinet);

        return cabinetRepository
            .findById(cabinet.getId())
            .map(existingCabinet -> {
                if (cabinet.getProduct() != null) {
                    existingCabinet.setProduct(cabinet.getProduct());
                }
                if (cabinet.getProductId() != null) {
                    existingCabinet.setProductId(cabinet.getProductId());
                }

                return existingCabinet;
            })
            .map(cabinetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cabinet> findAll() {
        log.debug("Request to get all Cabinets");
        return cabinetRepository.findAllWithEagerRelationships();
    }

    public Page<Cabinet> findAllWithEagerRelationships(Pageable pageable) {
        return cabinetRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cabinet> findOne(Long id) {
        log.debug("Request to get Cabinet : {}", id);
        return cabinetRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cabinet : {}", id);
        cabinetRepository.deleteById(id);
    }
}
