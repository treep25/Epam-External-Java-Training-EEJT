package com.epam.esm.user.model;

import com.epam.esm.jacoco.Generated;
import com.epam.esm.orders.model.Order;
import com.epam.esm.role.Role;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Generated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends RepresentationModel<UserResponse> {
    private long id;
    private String username;
    private Role role;
    private Set<Order> orders = Set.of();
}