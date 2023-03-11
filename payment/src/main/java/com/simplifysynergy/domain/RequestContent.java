package com.simplifysynergy.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A RequestContent.
 */
@Document(collection = "request_content")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("content")
    private String content;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public RequestContent id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public RequestContent content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestContent)) {
            return false;
        }
        return id != null && id.equals(((RequestContent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestContent{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
