package com.epam.esm.commercetools.graphql.responseModel;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Prices {
    private Value value;
}
