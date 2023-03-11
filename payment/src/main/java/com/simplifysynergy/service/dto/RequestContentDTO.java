package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.RequestContent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestContentDTO implements Serializable {

    private String id;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestContentDTO)) {
            return false;
        }

        RequestContentDTO requestContentDTO = (RequestContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requestContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestContentDTO{" +
            "id='" + getId() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
