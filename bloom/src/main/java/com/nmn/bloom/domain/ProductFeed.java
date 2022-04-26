package com.nmn.bloom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductFeed.
 */
@Entity
@Table(name = "product_feed")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product")
    private String product;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "target")
    private String target;

    @Column(name = "primary_concern")
    private String primaryConcern;

    @OneToMany(mappedBy = "productFeed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "productFeed" }, allowSetters = true)
    private Set<Comments> comments = new HashSet<>();

    @ManyToOne
    private User user;

    @JsonIgnoreProperties(value = { "productfeed", "user", "productdirectories" }, allowSetters = true)
    @OneToOne(mappedBy = "productfeed")
    private Cabinet cabinet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductFeed id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return this.product;
    }

    public ProductFeed product(String product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public ProductFeed productId(Integer productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getTarget() {
        return this.target;
    }

    public ProductFeed target(String target) {
        this.setTarget(target);
        return this;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPrimaryConcern() {
        return this.primaryConcern;
    }

    public ProductFeed primaryConcern(String primaryConcern) {
        this.setPrimaryConcern(primaryConcern);
        return this;
    }

    public void setPrimaryConcern(String primaryConcern) {
        this.primaryConcern = primaryConcern;
    }

    public Set<Comments> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comments> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setProductFeed(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setProductFeed(this));
        }
        this.comments = comments;
    }

    public ProductFeed comments(Set<Comments> comments) {
        this.setComments(comments);
        return this;
    }

    public ProductFeed addComments(Comments comments) {
        this.comments.add(comments);
        comments.setProductFeed(this);
        return this;
    }

    public ProductFeed removeComments(Comments comments) {
        this.comments.remove(comments);
        comments.setProductFeed(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductFeed user(User user) {
        this.setUser(user);
        return this;
    }

    public Cabinet getCabinet() {
        return this.cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        if (this.cabinet != null) {
            this.cabinet.setProductfeed(null);
        }
        if (cabinet != null) {
            cabinet.setProductfeed(this);
        }
        this.cabinet = cabinet;
    }

    public ProductFeed cabinet(Cabinet cabinet) {
        this.setCabinet(cabinet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductFeed)) {
            return false;
        }
        return id != null && id.equals(((ProductFeed) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductFeed{" +
            "id=" + getId() +
            ", product='" + getProduct() + "'" +
            ", productId=" + getProductId() +
            ", target='" + getTarget() + "'" +
            ", primaryConcern='" + getPrimaryConcern() + "'" +
            "}";
    }
}
