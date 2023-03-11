package com.simplifysynergy.service.dto;

import com.simplifysynergy.domain.enumeration.Frequency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.CollectionService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CollectionServiceDTO implements Serializable {

    private String id;

    private String code;

    private String description;

    private BigDecimal amount;

    private Frequency frequency;

    private ServiceTypeDTO serviceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public ServiceTypeDTO getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeDTO serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionServiceDTO)) {
            return false;
        }

        CollectionServiceDTO collectionServiceDTO = (CollectionServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collectionServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionServiceDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", frequency='" + getFrequency() + "'" +
            ", serviceType=" + getServiceType() +
            "}";
    }
}
