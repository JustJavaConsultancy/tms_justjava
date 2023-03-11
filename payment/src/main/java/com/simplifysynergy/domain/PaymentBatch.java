package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PaymentBatch.
 */
@Document(collection = "payment_batch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("batch_id")
    private String batchId;

    @Field("narration")
    private String narration;

    @Field("creation_date")
    private LocalDate creationDate;

    @Field("paymentInstructions")
    @JsonIgnoreProperties(value = { "paymentBatch" }, allowSetters = true)
    private Set<PaymentInstruction> paymentInstructions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PaymentBatch id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchId() {
        return this.batchId;
    }

    public PaymentBatch batchId(String batchId) {
        this.setBatchId(batchId);
        return this;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getNarration() {
        return this.narration;
    }

    public PaymentBatch narration(String narration) {
        this.setNarration(narration);
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public PaymentBatch creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<PaymentInstruction> getPaymentInstructions() {
        return this.paymentInstructions;
    }

    public void setPaymentInstructions(Set<PaymentInstruction> paymentInstructions) {
        if (this.paymentInstructions != null) {
            this.paymentInstructions.forEach(i -> i.setPaymentBatch(null));
        }
        if (paymentInstructions != null) {
            paymentInstructions.forEach(i -> i.setPaymentBatch(this));
        }
        this.paymentInstructions = paymentInstructions;
    }

    public PaymentBatch paymentInstructions(Set<PaymentInstruction> paymentInstructions) {
        this.setPaymentInstructions(paymentInstructions);
        return this;
    }

    public PaymentBatch addPaymentInstructions(PaymentInstruction paymentInstruction) {
        this.paymentInstructions.add(paymentInstruction);
        paymentInstruction.setPaymentBatch(this);
        return this;
    }

    public PaymentBatch removePaymentInstructions(PaymentInstruction paymentInstruction) {
        this.paymentInstructions.remove(paymentInstruction);
        paymentInstruction.setPaymentBatch(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentBatch)) {
            return false;
        }
        return id != null && id.equals(((PaymentBatch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentBatch{" +
            "id=" + getId() +
            ", batchId='" + getBatchId() + "'" +
            ", narration='" + getNarration() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
