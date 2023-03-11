package com.simplifysynergy.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Receipt.
 */
@Document(collection = "receipt")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("description")
    private String description;

    @Field("generation_date")
    private LocalDate generationDate;

    @NotNull(message = "must not be null")
    @Field("receipt_number")
    private String receiptNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Receipt id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Receipt description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getGenerationDate() {
        return this.generationDate;
    }

    public Receipt generationDate(LocalDate generationDate) {
        this.setGenerationDate(generationDate);
        return this;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }

    public String getReceiptNumber() {
        return this.receiptNumber;
    }

    public Receipt receiptNumber(String receiptNumber) {
        this.setReceiptNumber(receiptNumber);
        return this;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receipt)) {
            return false;
        }
        return id != null && id.equals(((Receipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", generationDate='" + getGenerationDate() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            "}";
    }
}
