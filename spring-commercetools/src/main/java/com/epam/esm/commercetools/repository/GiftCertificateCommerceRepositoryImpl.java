package com.epam.esm.commercetools.repository;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.common.PriceDraft;
import com.commercetools.api.models.graph_ql.GraphQLRequest;
import com.commercetools.api.models.product.*;
import com.commercetools.api.models.product_type.ProductTypeResourceIdentifier;
import com.epam.esm.commercetools.graphql.GraphQlQueries;
import com.epam.esm.commercetools.graphql.GraphQlResponseMapper;
import com.epam.esm.commercetools.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.model.GiftCertificateCommerceProductMapper;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateCommerceRepositoryImpl
        implements GiftCertificateCommerceRepository<CommerceGiftCertificate> {

    private final ProjectApiRoot apiRoot;
    private final GiftCertificateCommerceProductMapper giftCertificateCommerceProductMapper;
    private final GraphQlResponseMapper graphQlResponseMapper;

    private static final String CURRENCY_CODE = "UAH";
    private static final String DURATION = "Duration";
    private static final String TAGS = "Tags";
    private static final String PRODUCT_TYPE_ID = "a812e72c-c176-41a8-b710-9ec34fa8fe71";
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String WHERE = "where";

    private Attribute setDurationAttribute(int duration) {
        return Attribute
                .builder()
                .name(DURATION)
                .value(duration)
                .build();

    }

    private Attribute setTagsAttribute(Set<Tag> tags) {
        List<String> tagsName = new ArrayList<>();

        tags.forEach(tag -> tagsName.add(tag.getName()));

        return Attribute
                .builder()
                .name(TAGS)
                .value(tagsName)
                .build();
    }

    private Attribute setTagsAttributeWhenUpdateGiftCertificate(Set<Tag> tags, String giftCertificateId) {
        if (tags.isEmpty()) {
            return Attribute
                    .builder()
                    .name(TAGS)
                    .value(List.of())
                    .build();
        }

        Set<String> tagsNames = new HashSet<>();

        tags.forEach(tag -> tagsNames.add(tag.getName()));

        List<Attribute> attributes = apiRoot
                .products()
                .withId(giftCertificateId)
                .get()
                .executeBlocking()
                .getBody()
                .getMasterData()
                .getStaged()
                .getMasterVariant()
                .getAttributes();

        attributes.forEach(x1 -> {
            if (x1.getName().equals(TAGS)) {
                tagsNames.addAll((List<String>) x1.getValue());
            }
        });

        return Attribute
                .builder()
                .name(TAGS)
                .value(tagsNames)
                .build();
    }

    private List<ProductUpdateAction> preparingUpdatesForGiftCertificate(GiftCertificate giftCertificate, String id) {
        Map<Predicate<GiftCertificate>, Consumer<List<ProductUpdateAction>>> updatesMap = new HashMap<>();

        List<ProductUpdateAction> productUpdateActions = new ArrayList<>();

        updatesMap.put(cert -> giftCertificate.getName() != null,
                list -> list.add(ProductUpdateAction
                        .changeNameBuilder()
                        .name(LocalizedString
                                .ofEnglish(giftCertificate.getName()))
                        .build()));

        updatesMap.put(cert -> giftCertificate.getDescription() != null,
                list -> list.add(ProductUpdateAction
                        .setDescriptionBuilder()
                        .description(LocalizedString
                                .ofEnglish(giftCertificate.getDescription()))
                        .build()));

        updatesMap.put(cert -> giftCertificate.getPrice() != null,
                list -> list.add(ProductUpdateAction
                        .setPricesBuilder()
                        .variantId(1L)
                        .prices(PriceDraft
                                .builder()
                                .value(Money
                                        .builder()
                                        .currencyCode(CURRENCY_CODE)
                                        .centAmount(Long.parseLong(String.valueOf(giftCertificate.getPrice())))
                                        .build())
                                .build())
                        .build()));

        updatesMap.put(cert -> giftCertificate.getDurationDays() != null,
                list -> {
                    Attribute durationAttribute = setDurationAttribute(giftCertificate.getDurationDays());

                    list.add(ProductUpdateAction
                            .setAttributeBuilder()
                            .variantId(1L)
                            .name(durationAttribute.getName())
                            .value(durationAttribute.getValue())
                            .build());
                });
        updatesMap.put(cert -> giftCertificate.getTags() != null,
                list -> {
                    Attribute tagAttribute = setTagsAttributeWhenUpdateGiftCertificate(giftCertificate.getTags(), id);

                    list.add(ProductUpdateAction
                            .setAttributeBuilder()
                            .variantId(1L)
                            .name(tagAttribute.getName())
                            .value(tagAttribute.getValue())
                            .build());
                });

        updatesMap.forEach((key, value) -> {
            if (key.test(giftCertificate)) {
                value.accept(productUpdateActions);
            }
        });

        productUpdateActions.add(ProductUpdateAction.publishBuilder().build());

        return productUpdateActions;
    }


    @Override
    public CommerceGiftCertificate create(GiftCertificate giftCertificate) {
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(setDurationAttribute(giftCertificate.getDurationDays()));

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            attributes.add(setTagsAttribute(giftCertificate.getTags()));
        }

        return giftCertificateCommerceProductMapper
                .getGiftCertificateFromProductModel(apiRoot
                        .products()
                        .create(ProductDraft
                                .builder()
                                .productType(ProductTypeResourceIdentifier
                                        .builder()
                                        .id(PRODUCT_TYPE_ID)
                                        .build())
                                .slug(LocalizedString.ofEnglish(giftCertificate
                                        .getName().replaceAll("\\s", "")))
                                .name(LocalizedString.ofEnglish(giftCertificate
                                        .getName()))
                                .description(LocalizedString.ofEnglish(giftCertificate.getDescription()))
                                .publish(true)
                                .masterVariant(ProductVariantDraft
                                        .builder()
                                        .prices(PriceDraft
                                                .builder()
                                                .value(Money
                                                        .builder()
                                                        .currencyCode(CURRENCY_CODE)
                                                        .centAmount(giftCertificate.getPrice().longValue())
                                                        .build())
                                                .build())
                                        .attributes(
                                                attributes
                                        )
                                        .build())
                                .build())
                        .executeBlocking()
                        .getBody());
    }

    @Override
    public List<CommerceGiftCertificate> read(PageRequest pageRequest) {
        return giftCertificateCommerceProductMapper.getListGiftCertificatesFromProductModelsList(apiRoot
                .products()
                .get()
                .withOffset(pageRequest.getPageNumber())
                .withLimit(pageRequest.getPageSize())
                .executeBlocking()
                .getBody()
                .getResults());

    }

    @Override
    public CommerceGiftCertificate readById(String id) {
        return giftCertificateCommerceProductMapper
                .getGiftCertificateFromProductModel(apiRoot
                        .products()
                        .withId(id)
                        .get()
                        .executeBlocking()
                        .getBody());
    }

    @Override
    public CommerceGiftCertificate updatePrice(String id, long version, int price) {
        return giftCertificateCommerceProductMapper.getGiftCertificateFromProductModel(apiRoot
                .products()
                .withId(id)
                .post(ProductUpdate
                        .builder()
                        .version(version)
                        .actions(ProductUpdateAction.setPricesBuilder()
                                        .variantId(1L)
                                        .prices(PriceDraft
                                                .builder()
                                                .value(Money
                                                        .builder()
                                                        .currencyCode(CURRENCY_CODE)
                                                        .centAmount(Long.parseLong(String.valueOf(price)))
                                                        .build())
                                                .build())
                                        .build()
                                , ProductUpdateAction.publishBuilder().build())
                        .build()
                )
                .executeBlocking()
                .getBody());
    }

    @Override
    public CommerceGiftCertificate updateGiftCertificate(String id, long version, GiftCertificate giftCertificate) {
        return giftCertificateCommerceProductMapper.getGiftCertificateFromProductModel(apiRoot
                .products()
                .withId(id)
                .post(ProductUpdate
                        .builder()
                        .version(version)
                        .actions(preparingUpdatesForGiftCertificate(giftCertificate, id))
                        .build()
                )
                .executeBlocking()
                .getBody());
    }

    @Override
    public void delete(String id, long version) {
        apiRoot
                .products()
                .withId(id)
                .delete()
                .addVersion(version)
                .executeBlocking()
                .getBody();
    }

    @Override
    public void preDeleteActionSetPublishFalse(String id, long version) {
        apiRoot
                .products()
                .withId(id)
                .post(ProductUpdate
                        .builder()
                        .version(version)
                        .actions(ProductUpdateAction.unpublishBuilder().build())
                        .build()
                )
                .executeBlocking()
                .getBody();
    }

    @Override
    public long getProductVersion(String id) {
        return apiRoot
                .products()
                .withId(id)
                .get()
                .executeBlocking()
                .getBody().getVersion();
    }

    @Override
    public List<CommerceGiftCertificate> findByName(String name) {
        return giftCertificateCommerceProductMapper
                .getListGiftCertificatesFromProductModelsList(apiRoot
                        .products()
                        .get()
                        .executeBlocking()
                        .getBody()
                        .getResults())
                .stream()
                .filter(commerceGiftCertificate ->
                        commerceGiftCertificate
                                .getName()
                                .startsWith(name))
                .toList();
    }

    private String getVariableGetByTagName(String tagName) {
        return "masterData(current(masterVariant(attributes(value = \"" + tagName + "\"))))";
    }

    public List<CommerceGiftCertificate> findByTagName(String tagName, PageRequest pageRequest) {
        GraphQLRequest request =
                GraphQLRequest
                        .builder()
                        .query(GraphQlQueries.getAllGiftCertificatesByTagName)
                        .variables(builder
                                -> builder
                                .addValue(WHERE, getVariableGetByTagName(tagName))
                                .addValue(OFFSET, pageRequest.getPageNumber())
                                .addValue(LIMIT, pageRequest.getPageSize()))
                        .build();

        return giftCertificateCommerceProductMapper
                .getGiftCertificateListFromGraphQlResponseModelList(
                        graphQlResponseMapper
                                .getGiftCertificateModelFromGraphQlResponse(
                                        apiRoot
                                                .graphql()
                                                .post(request)
                                                .executeBlocking()
                                                .getBody()
                                                .getData()));
    }
}
