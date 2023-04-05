package com.epam.esm.commercetools.graphql.responseModel;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseGraphQlModel {
    private String id;
    private Date createdAt;
    private Date lastModifiedAt;
    private MasterData masterData;
}
