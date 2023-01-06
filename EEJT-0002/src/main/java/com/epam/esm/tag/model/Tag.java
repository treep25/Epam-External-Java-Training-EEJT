package com.epam.esm.tag.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public class Tag {
    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Tag setId(Long id) {
        this.id = id;
        return this;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return new EqualsBuilder().append(getId(), tag.getId()).append(getName(), tag.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getName()).toHashCode();
    }
}
