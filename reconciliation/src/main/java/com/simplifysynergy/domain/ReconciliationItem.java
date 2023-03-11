package com.simplifysynergy.domain;

import com.simplifysynergy.domain.enumeration.ReconciliationStatus;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ReconciliationItem.
 */
@Document(collection = "reconciliation_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReconciliationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("status")
    private ReconciliationStatus status;

    @Field("leftItem")
    private LeftItem leftItem;

    @Field("rightItem")
    private RightItem rightItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ReconciliationItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReconciliationStatus getStatus() {
        return this.status;
    }

    public ReconciliationItem status(ReconciliationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReconciliationStatus status) {
        this.status = status;
    }

    public LeftItem getLeftItem() {
        return this.leftItem;
    }

    public void setLeftItem(LeftItem leftItem) {
        this.leftItem = leftItem;
    }

    public ReconciliationItem leftItem(LeftItem leftItem) {
        this.setLeftItem(leftItem);
        return this;
    }

    public RightItem getRightItem() {
        return this.rightItem;
    }

    public void setRightItem(RightItem rightItem) {
        this.rightItem = rightItem;
    }

    public ReconciliationItem rightItem(RightItem rightItem) {
        this.setRightItem(rightItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReconciliationItem)) {
            return false;
        }
        return id != null && id.equals(((ReconciliationItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReconciliationItem{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
