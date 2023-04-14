package com.epam.esm.commercetools.customer.model;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CommerceCustomer {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String companyName;
}
