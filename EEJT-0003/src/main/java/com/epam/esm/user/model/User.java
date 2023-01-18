package com.epam.esm.user.model;

import com.epam.esm.orders.model.Order;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Entity
@Data
public class User extends RepresentationModel<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Order> orders = Set.of();
}
