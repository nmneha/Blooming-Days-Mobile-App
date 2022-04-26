package com.nmn.bloom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductDirectory.
 */
@Entity
@Table(name = "product_directory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductDirectory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product")
    private String product;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "primary_ingredient")
    private String primaryIngredient;

    @ManyToMany
    @JoinTable(
        name = "rel_product_directory__cabinet",
        joinColumns = @JoinColumn(name = "product_directory_id"),
        inverseJoinColumns = @JoinColumn(name = "cabinet_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productfeed", "user", "productdirectories" }, allowSetters = true)
    private Set<Cabinet> cabinets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductDirectory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return this.product;
    }

    public ProductDirectory product(String product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductId() {
        return this.productId;
    }

    public ProductDirectory productId(String productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductBrand() {
        return this.productBrand;
    }

    public ProductDirectory productBrand(String productBrand) {
        this.setProductBrand(productBrand);
        return this;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getPrimaryIngredient() {
        return this.primaryIngredient;
    }

    public ProductDirectory primaryIngredient(String primaryIngredient) {
        this.setPrimaryIngredient(primaryIngredient);
        return this;
    }

    public void setPrimaryIngredient(String primaryIngredient) {
        this.primaryIngredient = primaryIngredient;
    }

    public Set<Cabinet> getCabinets() {
        return this.cabinets;
    }

    public void setCabinets(Set<Cabinet> cabinets) {
        this.cabinets = cabinets;
    }

    public ProductDirectory cabinets(Set<Cabinet> cabinets) {
        this.setCabinets(cabinets);
        return this;
    }

    public ProductDirectory addCabinet(Cabinet cabinet) {
        this.cabinets.add(cabinet);
        cabinet.getProductdirectories().add(this);
        return this;
    }

    public ProductDirectory removeCabinet(Cabinet cabinet) {
        this.cabinets.remove(cabinet);
        cabinet.getProductdirectories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDirectory)) {
            return false;
        }
        return id != null && id.equals(((ProductDirectory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDirectory{" +
            "id=" + getId() +
            ", product='" + getProduct() + "'" +
            ", productId='" + getProductId() + "'" +
            ", productBrand='" + getProductBrand() + "'" +
            ", primaryIngredient='" + getPrimaryIngredient() + "'" +
            "}";
    }
}
