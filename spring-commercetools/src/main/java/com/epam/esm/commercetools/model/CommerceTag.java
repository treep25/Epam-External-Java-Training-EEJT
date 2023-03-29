package com.epam.esm.commercetools.model;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CommerceTag {

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CommerceTag that)) return false;

        return new EqualsBuilder().append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }
}
