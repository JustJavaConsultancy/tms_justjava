package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A InvoicePayment.
 */
@Document(collection = "invoice_payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoicePayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("narration")
    private String narration;

    @Field("payment_date")
    private LocalDate paymentDate;

    @Field("amount")
    private BigDecimal amount;

    @Field("invoice")
    private Invoice invoice;

    @Field("sourceAccount")
    private UserAccount sourceAccount;

    @Field("destinationAccount")
    private UserAccount destinationAccount;

    @Field("payer")
    private Payer payer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public InvoicePayment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNarration() {
        return this.narration;
    }

    public InvoicePayment narration(String narration) {
        this.setNarration(narration);
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public InvoicePayment paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public InvoicePayment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoicePayment invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public UserAccount getSourceAccount() {
        return this.sourceAccount;
    }

    public void setSourceAccount(UserAccount userAccount) {
        this.sourceAccount = userAccount;
    }

    public InvoicePayment sourceAccount(UserAccount userAccount) {
        this.setSourceAccount(userAccount);
        return this;
    }

    public UserAccount getDestinationAccount() {
        return this.destinationAccount;
    }

    public void setDestinationAccount(UserAccount userAccount) {
        this.destinationAccount = userAccount;
    }

    public InvoicePayment destinationAccount(UserAccount userAccount) {
        this.setDestinationAccount(userAccount);
        return this;
    }

    public Payer getPayer() {
        return this.payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public InvoicePayment payer(Payer payer) {
        this.setPayer(payer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoicePayment)) {
            return false;
        }
        return id != null && id.equals(((InvoicePayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoicePayment{" +
            "id=" + getId() +
            ", narration='" + getNarration() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
