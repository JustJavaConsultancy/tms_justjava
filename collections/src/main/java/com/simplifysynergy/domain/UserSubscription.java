package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplifysynergy.domain.enumeration.SubscriptionStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UserSubscription.
 */
@Document(collection = "user_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("status")
    private SubscriptionStatus status;

    @Field("service")
    private CollectionService service;

    @Field("invoices")
    @JsonIgnoreProperties(value = { "invoicePayment", "userSubscription" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @Field("subscriber")
    private Subscriber subscriber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UserSubscription id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubscriptionStatus getStatus() {
        return this.status;
    }

    public UserSubscription status(SubscriptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public CollectionService getService() {
        return this.service;
    }

    public void setService(CollectionService collectionService) {
        this.service = collectionService;
    }

    public UserSubscription service(CollectionService collectionService) {
        this.setService(collectionService);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setUserSubscription(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setUserSubscription(this));
        }
        this.invoices = invoices;
    }

    public UserSubscription invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public UserSubscription addInvoices(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setUserSubscription(this);
        return this;
    }

    public UserSubscription removeInvoices(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setUserSubscription(null);
        return this;
    }

    public Subscriber getSubscriber() {
        return this.subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public UserSubscription subscriber(Subscriber subscriber) {
        this.setSubscriber(subscriber);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscription)) {
            return false;
        }
        return id != null && id.equals(((UserSubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscription{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
