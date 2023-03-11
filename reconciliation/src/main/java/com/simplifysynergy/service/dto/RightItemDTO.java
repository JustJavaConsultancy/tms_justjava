package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.RightItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RightItemDTO implements Serializable {

    private String id;

    private String narration;

    private String reference;

    private String externalReference;

    private BigDecimal amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RightItemDTO)) {
            return false;
        }

        RightItemDTO rightItemDTO = (RightItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rightItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RightItemDTO{" +
            "id='" + getId() + "'" +
            ", narration='" + getNarration() + "'" +
            ", reference='" + getReference() + "'" +
            ", externalReference='" + getExternalReference() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
