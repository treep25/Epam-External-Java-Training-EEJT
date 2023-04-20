package com.epam.esm.commercetools.graphql.responseModel;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class AttributesRaw {
    private String name;
    private Object value;
}
