package com.epam.esm.commercetools.graphql.responseModel;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Current {
    private List<NameWithLocale> nameAllLocales;
    private List<DescriptionWithLocale> descriptionAllLocales;
    private MasterVariant masterVariant;
}
