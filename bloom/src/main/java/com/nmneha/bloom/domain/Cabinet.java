package com.nmneha.bloom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cabinet.
 */
@Entity
@Table(name = "cabinet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cabinet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product")
    private String product;

    @Column(name = "product_id")
    private Integer productId;

    @JsonIgnoreProperties(value = { "comments", "cabinet" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ProductFeed productfeed;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "cabinets")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cabinets" }, allowSetters = true)
    private Set<ProductDirectory> productdirectories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cabinet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return this.product;
    }

    public Cabinet product(String product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public Cabinet productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public ProductFeed getProductfeed() {
        return this.productfeed;
    }

    public void setProductfeed(ProductFeed productFeed) {
        this.productfeed = productFeed;
    }

    public Cabinet productfeed(ProductFeed productFeed) {
        this.setProductfeed(productFeed);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cabinet user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<ProductDirectory> getProductdirectories() {
        return this.productdirectories;
    }

    public void setProductdirectories(Set<ProductDirectory> productDirectories) {
        if (this.productdirectories != null) {
            this.productdirectories.forEach(i -> i.removeCabinet(this));
        }
        if (productDirectories != null) {
            productDirectories.forEach(i -> i.addCabinet(this));
        }
        this.productdirectories = productDirectories;
    }

    public Cabinet productdirectories(Set<ProductDirectory> productDirectories) {
        this.setProductdirectories(productDirectories);
        return this;
    }

    public Cabinet addProductdirectory(ProductDirectory productDirectory) {
        this.productdirectories.add(productDirectory);
        productDirectory.getCabinets().add(this);
        return this;
    }

    public Cabinet removeProductdirectory(ProductDirectory productDirectory) {
        this.productdirectories.remove(productDirectory);
        productDirectory.getCabinets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cabinet)) {
            return false;
        }
        return id != null && id.equals(((Cabinet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cabinet{" +
            "id=" + getId() +
            ", product='" + getProduct() + "'" +
            ", productId=" + getProductId() +
            "}";
    }
}
