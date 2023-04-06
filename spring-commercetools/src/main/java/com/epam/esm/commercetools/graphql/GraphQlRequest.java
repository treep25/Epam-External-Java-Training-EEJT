package com.epam.esm.commercetools.graphql;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.epam.esm.commercetools.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.model.GiftCertificateCommerceProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    private GraphQLRequest getRequestGetByTagName(PageRequest pageRequest, String tagName) {
        return GraphQLRequest
                .builder()
                .query(GraphQlQueries.getAllGiftCertificatesByTagName)
                .variables(builder
                        -> builder
                        .addValue(WHERE, GraphQlQueries.getVariableForTagName(tagName))
                        .addValue(OFFSET, pageRequest.getPageNumber())
                        .addValue(LIMIT, pageRequest.getPageSize()))
                .build();
    }

    public List<CommerceGiftCertificate> executeGetByTagName(String tagName, PageRequest pageRequest) {
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
