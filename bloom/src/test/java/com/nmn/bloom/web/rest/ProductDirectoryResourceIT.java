package com.nmn.bloom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nmn.bloom.IntegrationTest;
import com.nmn.bloom.domain.ProductDirectory;
import com.nmn.bloom.repository.ProductDirectoryRepository;
import com.nmn.bloom.service.ProductDirectoryService;
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
 * Integration tests for the {@link ProductDirectoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductDirectoryResourceIT {

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_INGREDIENT = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_INGREDIENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-directories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductDirectoryRepository productDirectoryRepository;

    @Mock
    private ProductDirectoryRepository productDirectoryRepositoryMock;

    @Mock
    private ProductDirectoryService productDirectoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductDirectoryMockMvc;

    private ProductDirectory productDirectory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDirectory createEntity(EntityManager em) {
        ProductDirectory productDirectory = new ProductDirectory()
            .product(DEFAULT_PRODUCT)
            .productId(DEFAULT_PRODUCT_ID)
            .productBrand(DEFAULT_PRODUCT_BRAND)
            .primaryIngredient(DEFAULT_PRIMARY_INGREDIENT);
        return productDirectory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDirectory createUpdatedEntity(EntityManager em) {
        ProductDirectory productDirectory = new ProductDirectory()
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .productBrand(UPDATED_PRODUCT_BRAND)
            .primaryIngredient(UPDATED_PRIMARY_INGREDIENT);
        return productDirectory;
    }

    @BeforeEach
    public void initTest() {
        productDirectory = createEntity(em);
    }

    @Test
    @Transactional
    void createProductDirectory() throws Exception {
        int databaseSizeBeforeCreate = productDirectoryRepository.findAll().size();
        // Create the ProductDirectory
        restProductDirectoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isCreated());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductDirectory testProductDirectory = productDirectoryList.get(productDirectoryList.size() - 1);
        assertThat(testProductDirectory.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testProductDirectory.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductDirectory.getProductBrand()).isEqualTo(DEFAULT_PRODUCT_BRAND);
        assertThat(testProductDirectory.getPrimaryIngredient()).isEqualTo(DEFAULT_PRIMARY_INGREDIENT);
    }

    @Test
    @Transactional
    void createProductDirectoryWithExistingId() throws Exception {
        // Create the ProductDirectory with an existing ID
        productDirectory.setId(1L);

        int databaseSizeBeforeCreate = productDirectoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDirectoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductDirectories() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        // Get all the productDirectoryList
        restProductDirectoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDirectory.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].productBrand").value(hasItem(DEFAULT_PRODUCT_BRAND)))
            .andExpect(jsonPath("$.[*].primaryIngredient").value(hasItem(DEFAULT_PRIMARY_INGREDIENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductDirectoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productDirectoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductDirectoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productDirectoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductDirectoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productDirectoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductDirectoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productDirectoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProductDirectory() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        // Get the productDirectory
        restProductDirectoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productDirectory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productDirectory.getId().intValue()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.productBrand").value(DEFAULT_PRODUCT_BRAND))
            .andExpect(jsonPath("$.primaryIngredient").value(DEFAULT_PRIMARY_INGREDIENT));
    }

    @Test
    @Transactional
    void getNonExistingProductDirectory() throws Exception {
        // Get the productDirectory
        restProductDirectoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductDirectory() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();

        // Update the productDirectory
        ProductDirectory updatedProductDirectory = productDirectoryRepository.findById(productDirectory.getId()).get();
        // Disconnect from session so that the updates on updatedProductDirectory are not directly saved in db
        em.detach(updatedProductDirectory);
        updatedProductDirectory
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .productBrand(UPDATED_PRODUCT_BRAND)
            .primaryIngredient(UPDATED_PRIMARY_INGREDIENT);

        restProductDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductDirectory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductDirectory))
            )
            .andExpect(status().isOk());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
        ProductDirectory testProductDirectory = productDirectoryList.get(productDirectoryList.size() - 1);
        assertThat(testProductDirectory.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testProductDirectory.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductDirectory.getProductBrand()).isEqualTo(UPDATED_PRODUCT_BRAND);
        assertThat(testProductDirectory.getPrimaryIngredient()).isEqualTo(UPDATED_PRIMARY_INGREDIENT);
    }

    @Test
    @Transactional
    void putNonExistingProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDirectory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductDirectoryWithPatch() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();

        // Update the productDirectory using partial update
        ProductDirectory partialUpdatedProductDirectory = new ProductDirectory();
        partialUpdatedProductDirectory.setId(productDirectory.getId());

        partialUpdatedProductDirectory
            .product(UPDATED_PRODUCT)
            .productBrand(UPDATED_PRODUCT_BRAND)
            .primaryIngredient(UPDATED_PRIMARY_INGREDIENT);

        restProductDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDirectory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductDirectory))
            )
            .andExpect(status().isOk());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
        ProductDirectory testProductDirectory = productDirectoryList.get(productDirectoryList.size() - 1);
        assertThat(testProductDirectory.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testProductDirectory.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductDirectory.getProductBrand()).isEqualTo(UPDATED_PRODUCT_BRAND);
        assertThat(testProductDirectory.getPrimaryIngredient()).isEqualTo(UPDATED_PRIMARY_INGREDIENT);
    }

    @Test
    @Transactional
    void fullUpdateProductDirectoryWithPatch() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();

        // Update the productDirectory using partial update
        ProductDirectory partialUpdatedProductDirectory = new ProductDirectory();
        partialUpdatedProductDirectory.setId(productDirectory.getId());

        partialUpdatedProductDirectory
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .productBrand(UPDATED_PRODUCT_BRAND)
            .primaryIngredient(UPDATED_PRIMARY_INGREDIENT);

        restProductDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDirectory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductDirectory))
            )
            .andExpect(status().isOk());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
        ProductDirectory testProductDirectory = productDirectoryList.get(productDirectoryList.size() - 1);
        assertThat(testProductDirectory.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testProductDirectory.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductDirectory.getProductBrand()).isEqualTo(UPDATED_PRODUCT_BRAND);
        assertThat(testProductDirectory.getPrimaryIngredient()).isEqualTo(UPDATED_PRIMARY_INGREDIENT);
    }

    @Test
    @Transactional
    void patchNonExistingProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDirectory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductDirectory() throws Exception {
        int databaseSizeBeforeUpdate = productDirectoryRepository.findAll().size();
        productDirectory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDirectory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDirectory in the database
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductDirectory() throws Exception {
        // Initialize the database
        productDirectoryRepository.saveAndFlush(productDirectory);

        int databaseSizeBeforeDelete = productDirectoryRepository.findAll().size();

        // Delete the productDirectory
        restProductDirectoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productDirectory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductDirectory> productDirectoryList = productDirectoryRepository.findAll();
        assertThat(productDirectoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
