package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ChartOfAccount.
 */
@Document(collection = "chart_of_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChartOfAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("description")
    private String description;

    @Field("charts")
    @JsonIgnoreProperties(value = { "chartOfAccount" }, allowSetters = true)
    private Set<Chart> charts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ChartOfAccount id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public ChartOfAccount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Chart> getCharts() {
        return this.charts;
    }

    public void setCharts(Set<Chart> charts) {
        if (this.charts != null) {
            this.charts.forEach(i -> i.setChartOfAccount(null));
        }
        if (charts != null) {
            charts.forEach(i -> i.setChartOfAccount(this));
        }
        this.charts = charts;
    }

    public ChartOfAccount charts(Set<Chart> charts) {
        this.setCharts(charts);
        return this;
    }

    public ChartOfAccount addCharts(Chart chart) {
        this.charts.add(chart);
        chart.setChartOfAccount(this);
        return this;
    }

    public ChartOfAccount removeCharts(Chart chart) {
        this.charts.remove(chart);
        chart.setChartOfAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartOfAccount)) {
            return false;
        }
        return id != null && id.equals(((ChartOfAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccount{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
