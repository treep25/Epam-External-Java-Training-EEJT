package com.epam.esm.user.dto;

import com.epam.esm.orders.model.Order;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {
    private long id;
    private String username;
    private Role role;
    private Set<Order> orders = Set.of();
}
