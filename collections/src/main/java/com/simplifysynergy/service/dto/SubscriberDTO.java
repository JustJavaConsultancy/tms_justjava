package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Subscriber} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriberDTO implements Serializable {

    private String id;

    private String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriberDTO)) {
            return false;
        }

        SubscriberDTO subscriberDTO = (SubscriberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriberDTO{" +
            "id='" + getId() + "'" +
            ", fullName='" + getFullName() + "'" +
            "}";
    }
}
