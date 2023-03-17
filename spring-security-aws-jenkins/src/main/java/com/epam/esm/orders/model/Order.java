package com.epam.esm.orders.model;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.jacoco.Generated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Generated
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
        if (!super.equals(o)) return false;

        if (getId() != order.getId()) return false;
        if (getCost() != order.getCost()) return false;
        if (getGiftCertificate() != null ? !getGiftCertificate().equals(order.getGiftCertificate()) : order.getGiftCertificate() != null)
            return false;
        return getPurchaseDate() != null ? getPurchaseDate().equals(order.getPurchaseDate()) : order.getPurchaseDate() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getGiftCertificate() != null ? getGiftCertificate().hashCode() : 0);
        result = 31 * result + getCost();
        result = 31 * result + (getPurchaseDate() != null ? getPurchaseDate().hashCode() : 0);
        return result;
    }
}
