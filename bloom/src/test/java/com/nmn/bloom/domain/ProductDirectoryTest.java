package com.nmn.bloom.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nmn.bloom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDirectoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDirectory.class);
        ProductDirectory productDirectory1 = new ProductDirectory();
        productDirectory1.setId(1L);
        ProductDirectory productDirectory2 = new ProductDirectory();
        productDirectory2.setId(productDirectory1.getId());
        assertThat(productDirectory1).isEqualTo(productDirectory2);
        productDirectory2.setId(2L);
        assertThat(productDirectory1).isNotEqualTo(productDirectory2);
        productDirectory1.setId(null);
        assertThat(productDirectory1).isNotEqualTo(productDirectory2);
    }
}
