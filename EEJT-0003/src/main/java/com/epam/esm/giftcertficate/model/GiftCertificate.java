package com.epam.esm.giftcertficate.model;

import com.epam.esm.tag.model.Tag;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Set;

@Entity
@Data
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
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Tag> tags = Set.of();
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date lastUpdateDate;

    public GiftCertificate() {

    }
}
