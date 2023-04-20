package com.epam.esm.commercetools.graphql;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.certificate.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.certificate.model.GiftCertificateCommerceProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GraphQlRequest {

    private final ProjectApiRoot apiRoot;
    private final GiftCertificateCommerceProductMapper giftCertificateCommerceProductMapper;
    private final GraphQlResponseMapper graphQlResponseMapper;
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String WHERE = "where";

    private GraphQLRequest getRequestGetByTagName(PagePaginationBuilder pageRequest, String tagName) {
        return GraphQLRequest
                .builder()
                .query(GraphQlQueries.getAllGiftCertificatesByTagName)
                .variables(builder
                        -> builder
                        .addValue(WHERE, GraphQlQueries.getVariableForTagName(tagName))
                        .addValue(OFFSET, pageRequest.getOffset())
                        .addValue(LIMIT, pageRequest.getLimit()))
                .build();

    }

    public List<CommerceGiftCertificate> executeGetByTagName(String tagName, PagePaginationBuilder pageRequest) {
        return giftCertificateCommerceProductMapper
                .getGiftCertificateListFromGraphQlResponseModelList(
                        graphQlResponseMapper
                                .getGiftCertificateModelFromGraphQlResponse(
                                        apiRoot
                                                .graphql()
                                                .post(getRequestGetByTagName(pageRequest, tagName))
                                                .executeBlocking()
                                                .getBody()
                                                .getData()));
    }

}
