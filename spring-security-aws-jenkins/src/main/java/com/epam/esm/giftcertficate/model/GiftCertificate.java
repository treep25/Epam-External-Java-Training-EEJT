package com.epam.esm.giftcertficate.model;

import com.epam.esm.jacoco.JacocoCoverageExcludesGenerated;
import com.epam.esm.tag.model.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@JacocoCoverageExcludesGenerated
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GiftCertificate extends RepresentationModel<GiftCertificate> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer price;
    @Column(name = "duration")
    private Integer durationDays;
    @ManyToMany(cascade = CascadeType.MERGE)
    @ToString.Exclude
    private Set<Tag> tags;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getDurationDays(), that.getDurationDays()) && Objects.equals(getTags(), that.getTags()) && Objects.equals(getCreateDate(), that.getCreateDate()) && Objects.equals(getLastUpdateDate(), that.getLastUpdateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getName(), getDescription(), getPrice(), getDurationDays(), getTags(), getCreateDate(), getLastUpdateDate());
    }
}
