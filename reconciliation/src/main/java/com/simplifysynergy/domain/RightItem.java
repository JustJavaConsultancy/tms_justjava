package com.simplifysynergy.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A RightItem.
 */
@Document(collection = "right_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RightItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("narration")
    private String narration;

    @Field("reference")
    private String reference;

    @Field("external_reference")
    private String externalReference;

    @Field("amount")
    private BigDecimal amount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public RightItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNarration() {
        return this.narration;
    }

    public RightItem narration(String narration) {
        this.setNarration(narration);
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReference() {
        return this.reference;
    }

    public RightItem reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExternalReference() {
        return this.externalReference;
    }

    public RightItem externalReference(String externalReference) {
        this.setExternalReference(externalReference);
        return this;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public RightItem amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RightItem)) {
            return false;
        }
        return id != null && id.equals(((RightItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RightItem{" +
            "id=" + getId() +
            ", narration='" + getNarration() + "'" +
            ", reference='" + getReference() + "'" +
            ", externalReference='" + getExternalReference() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
