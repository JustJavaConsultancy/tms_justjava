package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplifysynergy.domain.enumeration.InvoiceStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Invoice.
 */
@Document(collection = "invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("invoice_number")
    private String invoiceNumber;

    @Field("issue_date")
    private LocalDate issueDate;

    @Field("next_due_date")
    private LocalDate nextDueDate;

    @Field("amount")
    private BigDecimal amount;

    @Field("status")
    private InvoiceStatus status;

    @Field("invoicePayment")
    private InvoicePayment invoicePayment;

    @Field("userSubscription")
    @JsonIgnoreProperties(value = { "service", "invoices", "subscriber" }, allowSetters = true)
    private UserSubscription userSubscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Invoice id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    public Invoice issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getNextDueDate() {
        return this.nextDueDate;
    }

    public Invoice nextDueDate(LocalDate nextDueDate) {
        this.setNextDueDate(nextDueDate);
        return this;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Invoice amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public Invoice status(InvoiceStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public InvoicePayment getInvoicePayment() {
        return this.invoicePayment;
    }

    public void setInvoicePayment(InvoicePayment invoicePayment) {
        this.invoicePayment = invoicePayment;
    }

    public Invoice invoicePayment(InvoicePayment invoicePayment) {
        this.setInvoicePayment(invoicePayment);
        return this;
    }

    public UserSubscription getUserSubscription() {
        return this.userSubscription;
    }

    public void setUserSubscription(UserSubscription userSubscription) {
        this.userSubscription = userSubscription;
    }

    public Invoice userSubscription(UserSubscription userSubscription) {
        this.setUserSubscription(userSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", nextDueDate='" + getNextDueDate() + "'" +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
