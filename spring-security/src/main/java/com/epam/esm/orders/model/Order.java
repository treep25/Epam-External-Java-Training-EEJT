package com.epam.esm.orders.model;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "orders")
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    private GiftCertificate giftCertificate;
    private int cost;
    @CreatedDate
    private Date purchaseDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Order order)) return false;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(getId(), order.getId()).append(getCost(), order.getCost()).append(getGiftCertificate(), order.getGiftCertificate()).append(getPurchaseDate(), order.getPurchaseDate()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getId()).append(getGiftCertificate()).append(getCost()).append(getPurchaseDate()).toHashCode();
    }
}
