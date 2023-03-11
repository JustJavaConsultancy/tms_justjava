package com.simplifysynergy.service.dto;

import com.simplifysynergy.domain.enumeration.SubscriptionStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.UserSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscriptionDTO implements Serializable {

    private String id;

    private SubscriptionStatus status;

    private CollectionServiceDTO service;

    private SubscriberDTO subscriber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public CollectionServiceDTO getService() {
        return service;
    }

    public void setService(CollectionServiceDTO service) {
        this.service = service;
    }

    public SubscriberDTO getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(SubscriberDTO subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscriptionDTO)) {
            return false;
        }

        UserSubscriptionDTO userSubscriptionDTO = (UserSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscriptionDTO{" +
            "id='" + getId() + "'" +
            ", status='" + getStatus() + "'" +
            ", service=" + getService() +
            ", subscriber=" + getSubscriber() +
            "}";
    }
}
