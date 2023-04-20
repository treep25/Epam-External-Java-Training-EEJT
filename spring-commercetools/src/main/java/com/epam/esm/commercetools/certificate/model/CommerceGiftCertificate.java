package com.epam.esm.commercetools.certificate.model;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CommerceGiftCertificate {

    private String productId;
    private String name;
    private String description;
    private Integer price;
    private final String currency = "UAH";
    private Integer durationDays;
    private Set<CommerceTag> tags;
    private Date createDate;
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CommerceGiftCertificate that)) return false;

        return new EqualsBuilder().append(getProductId(), that.getProductId()).append(getName(), that.getName()).append(getDescription(), that.getDescription()).append(getPrice(), that.getPrice()).append(getDurationDays(), that.getDurationDays()).append(getTags(), that.getTags()).append(getCreateDate(), that.getCreateDate()).append(getLastUpdateDate(), that.getLastUpdateDate()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProductId()).append(getName()).append(getDescription()).append(getPrice()).append(getDurationDays()).append(getTags()).append(getCreateDate()).append(getLastUpdateDate()).toHashCode();
    }
}
