package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.InvoicePayment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoicePaymentDTO implements Serializable {

    private String id;

    private String narration;

    private LocalDate paymentDate;

    private BigDecimal amount;

    private InvoiceDTO invoice;

    private UserAccountDTO sourceAccount;

    private UserAccountDTO destinationAccount;

    private PayerDTO payer;

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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public UserAccountDTO getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(UserAccountDTO sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public UserAccountDTO getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(UserAccountDTO destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public PayerDTO getPayer() {
        return payer;
    }

    public void setPayer(PayerDTO payer) {
        this.payer = payer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoicePaymentDTO)) {
            return false;
        }

        InvoicePaymentDTO invoicePaymentDTO = (InvoicePaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoicePaymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoicePaymentDTO{" +
            "id='" + getId() + "'" +
            ", narration='" + getNarration() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            ", invoice=" + getInvoice() +
            ", sourceAccount=" + getSourceAccount() +
            ", destinationAccount=" + getDestinationAccount() +
            ", payer=" + getPayer() +
            "}";
    }
}
