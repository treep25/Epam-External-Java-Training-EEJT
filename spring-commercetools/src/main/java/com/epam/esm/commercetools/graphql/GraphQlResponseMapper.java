package com.epam.esm.commercetools.graphql;

import com.epam.esm.commercetools.graphql.responseModel.ResponseGraphQlModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GraphQlResponseMapper {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String PRODUCTS = "products";
    private static final String RESULTS = "results";

    public List<ResponseGraphQlModel> getGiftCertificateModelFromGraphQlResponse(Object response) {
        Map<?, ?> responseMapWithResults = (Map<?, ?>) response;
        Map<?, ?> mapOfProducts = (Map<?, ?>) responseMapWithResults.get(PRODUCTS);
        List<?> listOfPureProducts = (List<?>) mapOfProducts.get(RESULTS);

        return getResponseGraphQlModelListOfProductsList(listOfPureProducts);
    }

    private List<ResponseGraphQlModel> getResponseGraphQlModelListOfProductsList(List<?> responseList) {
        List<ResponseGraphQlModel> responseGraphQlModelList = new ArrayList<>();

        responseList
                .forEach(
                        attribute -> {
                            try {
                                responseGraphQlModelList.add(mapper
                                        .readValue(mapper
                                                .writeValueAsString(attribute), ResponseGraphQlModel.class));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        return responseGraphQlModelList;
    }

}
