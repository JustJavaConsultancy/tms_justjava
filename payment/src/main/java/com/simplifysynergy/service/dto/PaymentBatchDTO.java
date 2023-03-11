package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.PaymentBatch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentBatchDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String batchId;

    private String narration;

    private LocalDate creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentBatchDTO)) {
            return false;
        }

        PaymentBatchDTO paymentBatchDTO = (PaymentBatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentBatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentBatchDTO{" +
            "id='" + getId() + "'" +
            ", batchId='" + getBatchId() + "'" +
            ", narration='" + getNarration() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
