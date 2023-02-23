package com.epam.esm.giftcertficate.model;

import com.epam.esm.tag.model.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
