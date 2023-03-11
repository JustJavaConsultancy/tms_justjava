package com.simplifysynergy.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A LeftItem.
 */
@Document(collection = "left_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeftItem implements Serializable {

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

    public LeftItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNarration() {
        return this.narration;
    }

    public LeftItem narration(String narration) {
        this.setNarration(narration);
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReference() {
        return this.reference;
    }

    public LeftItem reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExternalReference() {
        return this.externalReference;
    }

    public LeftItem externalReference(String externalReference) {
        this.setExternalReference(externalReference);
        return this;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public LeftItem amount(BigDecimal amount) {
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
        if (!(o instanceof LeftItem)) {
            return false;
        }
        return id != null && id.equals(((LeftItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeftItem{" +
            "id=" + getId() +
            ", narration='" + getNarration() + "'" +
            ", reference='" + getReference() + "'" +
            ", externalReference='" + getExternalReference() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
