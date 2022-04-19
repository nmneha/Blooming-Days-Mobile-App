package com.nmneha.bloom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nmneha.bloom.IntegrationTest;
import com.nmneha.bloom.domain.Cabinet;
import com.nmneha.bloom.repository.CabinetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CabinetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CabinetResourceIT {

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_ID = 1;
    private static final Integer UPDATED_PRODUCT_ID = 2;

    private static final String ENTITY_API_URL = "/api/cabinets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CabinetRepository cabinetRepository;

    @Mock
    private CabinetRepository cabinetRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCabinetMockMvc;

    private Cabinet cabinet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cabinet createEntity(EntityManager em) {
        Cabinet cabinet = new Cabinet().product(DEFAULT_PRODUCT).productId(DEFAULT_PRODUCT_ID);
        return cabinet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cabinet createUpdatedEntity(EntityManager em) {
        Cabinet cabinet = new Cabinet().product(UPDATED_PRODUCT).productId(UPDATED_PRODUCT_ID);
        return cabinet;
    }

    @BeforeEach
    public void initTest() {
        cabinet = createEntity(em);
    }

    @Test
    @Transactional
    void createCabinet() throws Exception {
        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();
        // Create the Cabinet
        restCabinetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isCreated());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate + 1);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testCabinet.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
    }

    @Test
    @Transactional
    void createCabinetWithExistingId() throws Exception {
        // Create the Cabinet with an existing ID
        cabinet.setId(1L);

        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCabinets() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get all the cabinetList
        restCabinetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinet.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCabinetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cabinetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCabinetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cabinetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCabinetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cabinetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCabinetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cabinetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get the cabinet
        restCabinetMockMvc
            .perform(get(ENTITY_API_URL_ID, cabinet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cabinet.getId().intValue()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID));
    }

    @Test
    @Transactional
    void getNonExistingCabinet() throws Exception {
        // Get the cabinet
        restCabinetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet
        Cabinet updatedCabinet = cabinetRepository.findById(cabinet.getId()).get();
        // Disconnect from session so that the updates on updatedCabinet are not directly saved in db
        em.detach(updatedCabinet);
        updatedCabinet.product(UPDATED_PRODUCT).productId(UPDATED_PRODUCT_ID);

        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCabinet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testCabinet.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void putNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cabinet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testCabinet.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
    }

    @Test
    @Transactional
    void fullUpdateCabinetWithPatch() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet using partial update
        Cabinet partialUpdatedCabinet = new Cabinet();
        partialUpdatedCabinet.setId(cabinet.getId());

        partialUpdatedCabinet.product(UPDATED_PRODUCT).productId(UPDATED_PRODUCT_ID);

        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCabinet))
            )
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testCabinet.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cabinet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cabinet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();
        cabinet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCabinetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        int databaseSizeBeforeDelete = cabinetRepository.findAll().size();

        // Delete the cabinet
        restCabinetMockMvc
            .perform(delete(ENTITY_API_URL_ID, cabinet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
