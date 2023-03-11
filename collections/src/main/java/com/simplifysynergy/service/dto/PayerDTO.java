package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Payer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PayerDTO implements Serializable {

    private String id;

    private String email;

    private String phone;

    private String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(o instanceof PayerDTO)) {
            return false;
        }

        PayerDTO payerDTO = (PayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, payerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PayerDTO{" +
            "id='" + getId() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fullName='" + getFullName() + "'" +
            "}";
    }
}
