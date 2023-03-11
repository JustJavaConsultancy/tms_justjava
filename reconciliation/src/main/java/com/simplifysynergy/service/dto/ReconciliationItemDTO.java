package com.simplifysynergy.service.dto;

import com.simplifysynergy.domain.enumeration.ReconciliationStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.ReconciliationItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReconciliationItemDTO implements Serializable {

    private String id;

    private ReconciliationStatus status;

    private LeftItemDTO leftItem;

    private RightItemDTO rightItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationStatus status) {
        this.status = status;
    }

    public LeftItemDTO getLeftItem() {
        return leftItem;
    }

    public void setLeftItem(LeftItemDTO leftItem) {
        this.leftItem = leftItem;
    }

    public RightItemDTO getRightItem() {
        return rightItem;
    }

    public void setRightItem(RightItemDTO rightItem) {
        this.rightItem = rightItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReconciliationItemDTO)) {
            return false;
        }

        ReconciliationItemDTO reconciliationItemDTO = (ReconciliationItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reconciliationItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReconciliationItemDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", leftItem=" + getLeftItem() +
            ", rightItem=" + getRightItem() +
            "}";
    }
}
