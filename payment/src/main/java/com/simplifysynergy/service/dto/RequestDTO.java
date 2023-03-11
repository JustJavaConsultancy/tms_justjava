package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Request} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private Integer currentLevel;

    private RequestContentDTO requestContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public RequestContentDTO getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(RequestContentDTO requestContent) {
        this.requestContent = requestContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestDTO)) {
            return false;
        }

        RequestDTO requestDTO = (RequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestDTO{" +
            "id='" + getId() + "'" +
            ", currentLevel=" + getCurrentLevel() +
            ", requestContent=" + getRequestContent() +
            "}";
    }
}
