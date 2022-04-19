package com.nmneha.bloom.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nmneha.bloom.IntegrationTest;
import com.nmneha.bloom.domain.ProductFeed;
import com.nmneha.bloom.repository.ProductFeedRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductFeedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductFeedResourceIT {

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_ID = 1;
    private static final Integer UPDATED_PRODUCT_ID = 2;

    private static final String DEFAULT_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_TARGET = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_CONCERN = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_CONCERN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-feeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductFeedRepository productFeedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductFeedMockMvc;

    private ProductFeed productFeed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeed createEntity(EntityManager em) {
        ProductFeed productFeed = new ProductFeed()
            .product(DEFAULT_PRODUCT)
            .productId(DEFAULT_PRODUCT_ID)
            .target(DEFAULT_TARGET)
            .primaryConcern(DEFAULT_PRIMARY_CONCERN);
        return productFeed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductFeed createUpdatedEntity(EntityManager em) {
        ProductFeed productFeed = new ProductFeed()
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .target(UPDATED_TARGET)
            .primaryConcern(UPDATED_PRIMARY_CONCERN);
        return productFeed;
    }

    @BeforeEach
    public void initTest() {
        productFeed = createEntity(em);
    }

    @Test
    @Transactional
    void createProductFeed() throws Exception {
        int databaseSizeBeforeCreate = productFeedRepository.findAll().size();
        // Create the ProductFeed
        restProductFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productFeed)))
            .andExpect(status().isCreated());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeCreate + 1);
        ProductFeed testProductFeed = productFeedList.get(productFeedList.size() - 1);
        assertThat(testProductFeed.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testProductFeed.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductFeed.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testProductFeed.getPrimaryConcern()).isEqualTo(DEFAULT_PRIMARY_CONCERN);
    }

    @Test
    @Transactional
    void createProductFeedWithExistingId() throws Exception {
        // Create the ProductFeed with an existing ID
        productFeed.setId(1L);

        int databaseSizeBeforeCreate = productFeedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productFeed)))
            .andExpect(status().isBadRequest());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductFeeds() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        // Get all the productFeedList
        restProductFeedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET)))
            .andExpect(jsonPath("$.[*].primaryConcern").value(hasItem(DEFAULT_PRIMARY_CONCERN)));
    }

    @Test
    @Transactional
    void getProductFeed() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        // Get the productFeed
        restProductFeedMockMvc
            .perform(get(ENTITY_API_URL_ID, productFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productFeed.getId().intValue()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET))
            .andExpect(jsonPath("$.primaryConcern").value(DEFAULT_PRIMARY_CONCERN));
    }

    @Test
    @Transactional
    void getNonExistingProductFeed() throws Exception {
        // Get the productFeed
        restProductFeedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductFeed() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();

        // Update the productFeed
        ProductFeed updatedProductFeed = productFeedRepository.findById(productFeed.getId()).get();
        // Disconnect from session so that the updates on updatedProductFeed are not directly saved in db
        em.detach(updatedProductFeed);
        updatedProductFeed
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .target(UPDATED_TARGET)
            .primaryConcern(UPDATED_PRIMARY_CONCERN);

        restProductFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductFeed.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductFeed))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
        ProductFeed testProductFeed = productFeedList.get(productFeedList.size() - 1);
        assertThat(testProductFeed.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testProductFeed.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductFeed.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testProductFeed.getPrimaryConcern()).isEqualTo(UPDATED_PRIMARY_CONCERN);
    }

    @Test
    @Transactional
    void putNonExistingProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productFeed.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productFeed))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productFeed))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productFeed)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductFeedWithPatch() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();

        // Update the productFeed using partial update
        ProductFeed partialUpdatedProductFeed = new ProductFeed();
        partialUpdatedProductFeed.setId(productFeed.getId());

        partialUpdatedProductFeed.primaryConcern(UPDATED_PRIMARY_CONCERN);

        restProductFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductFeed))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
        ProductFeed testProductFeed = productFeedList.get(productFeedList.size() - 1);
        assertThat(testProductFeed.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testProductFeed.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductFeed.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testProductFeed.getPrimaryConcern()).isEqualTo(UPDATED_PRIMARY_CONCERN);
    }

    @Test
    @Transactional
    void fullUpdateProductFeedWithPatch() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();

        // Update the productFeed using partial update
        ProductFeed partialUpdatedProductFeed = new ProductFeed();
        partialUpdatedProductFeed.setId(productFeed.getId());

        partialUpdatedProductFeed
            .product(UPDATED_PRODUCT)
            .productId(UPDATED_PRODUCT_ID)
            .target(UPDATED_TARGET)
            .primaryConcern(UPDATED_PRIMARY_CONCERN);

        restProductFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductFeed))
            )
            .andExpect(status().isOk());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
        ProductFeed testProductFeed = productFeedList.get(productFeedList.size() - 1);
        assertThat(testProductFeed.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testProductFeed.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductFeed.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testProductFeed.getPrimaryConcern()).isEqualTo(UPDATED_PRIMARY_CONCERN);
    }

    @Test
    @Transactional
    void patchNonExistingProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productFeed))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productFeed))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductFeed() throws Exception {
        int databaseSizeBeforeUpdate = productFeedRepository.findAll().size();
        productFeed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductFeedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productFeed))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductFeed in the database
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductFeed() throws Exception {
        // Initialize the database
        productFeedRepository.saveAndFlush(productFeed);

        int databaseSizeBeforeDelete = productFeedRepository.findAll().size();

        // Delete the productFeed
        restProductFeedMockMvc
            .perform(delete(ENTITY_API_URL_ID, productFeed.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductFeed> productFeedList = productFeedRepository.findAll();
        assertThat(productFeedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
