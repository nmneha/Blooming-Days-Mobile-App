package com.nmn.bloom.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nmn.bloom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductFeedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductFeed.class);
        ProductFeed productFeed1 = new ProductFeed();
        productFeed1.setId(1L);
        ProductFeed productFeed2 = new ProductFeed();
        productFeed2.setId(productFeed1.getId());
        assertThat(productFeed1).isEqualTo(productFeed2);
        productFeed2.setId(2L);
        assertThat(productFeed1).isNotEqualTo(productFeed2);
        productFeed1.setId(null);
        assertThat(productFeed1).isNotEqualTo(productFeed2);
    }
}
