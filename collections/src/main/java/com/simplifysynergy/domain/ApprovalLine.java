package com.simplifysynergy.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ApprovalLine.
 */
@Document(collection = "approval_line")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("level")
    private Integer level;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ApprovalLine id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return this.level;
    }

    public ApprovalLine level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalLine)) {
            return false;
        }
        return id != null && id.equals(((ApprovalLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalLine{" +
            "id=" + getId() +
            ", level=" + getLevel() +
            "}";
    }
}
