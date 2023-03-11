package com.simplifysynergy.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Request.
 */
@Document(collection = "request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("current_level")
    private Integer currentLevel;

    @Field("requestContent")
    private RequestContent requestContent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Request id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCurrentLevel() {
        return this.currentLevel;
    }

    public Request currentLevel(Integer currentLevel) {
        this.setCurrentLevel(currentLevel);
        return this;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public RequestContent getRequestContent() {
        return this.requestContent;
    }

    public void setRequestContent(RequestContent requestContent) {
        this.requestContent = requestContent;
    }

    public Request requestContent(RequestContent requestContent) {
        this.setRequestContent(requestContent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        return id != null && id.equals(((Request) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", currentLevel=" + getCurrentLevel() +
            "}";
    }
}
