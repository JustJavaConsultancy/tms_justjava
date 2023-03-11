package com.simplifysynergy.service.dto;

import com.simplifysynergy.domain.enumeration.InvoiceStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Invoice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceDTO implements Serializable {

    private String id;

    private String invoiceNumber;

    private LocalDate issueDate;

    private LocalDate nextDueDate;

    private BigDecimal amount;

    private InvoiceStatus status;

    private InvoicePaymentDTO invoicePayment;

    private UserSubscriptionDTO userSubscription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public InvoicePaymentDTO getInvoicePayment() {
        return invoicePayment;
    }

    public void setInvoicePayment(InvoicePaymentDTO invoicePayment) {
        this.invoicePayment = invoicePayment;
    }

    public UserSubscriptionDTO getUserSubscription() {
        return userSubscription;
    }

    public void setUserSubscription(UserSubscriptionDTO userSubscription) {
        this.userSubscription = userSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id='" + getId() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", nextDueDate='" + getNextDueDate() + "'" +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", invoicePayment=" + getInvoicePayment() +
            ", userSubscription=" + getUserSubscription() +
            "}";
    }
}
