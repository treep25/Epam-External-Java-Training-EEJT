package com.epam.esm.commercetools.repository;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.common.PriceDraft;
import com.commercetools.api.models.product.*;
import com.commercetools.api.models.product_type.ProductTypeResourceIdentifier;
import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.UpdateActionsBuilder;
import com.epam.esm.commercetools.graphql.GraphQlRequest;
import com.epam.esm.commercetools.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.model.GiftCertificateCommerceProductMapper;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateCommerceRepositoryImpl
        implements GiftCertificateCommerceRepository<CommerceGiftCertificate> {

    private final ProjectApiRoot apiRoot;
    private final GiftCertificateCommerceProductMapper giftCertificateCommerceProductMapper;
    private final UpdateActionsBuilder updateActionsBuilder;
    private final GraphQlRequest graphQlRequest;
    private static final String CURRENCY_CODE = "UAH";
    private static final String DURATION = "Duration";
    private static final String TAGS = "Tags";
    private static final String PRODUCT_TYPE_ID = "a812e72c-c176-41a8-b710-9ec34fa8fe71";

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
    public List<CommerceGiftCertificate> read(PagePaginationBuilder pageRequest) {
        return giftCertificateCommerceProductMapper.getListGiftCertificatesFromProductModelsList(apiRoot
                .products()
                .get()
                .withOffset(pageRequest.getOffset())
                .withLimit(pageRequest.getLimit())
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
                        .actions(updateActionsBuilder.preparingUpdatesForGiftCertificate(giftCertificate, id))
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
    public List<CommerceGiftCertificate> findByName(String name, PagePaginationBuilder pageRequest) {
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
                .skip(pageRequest.getOffset())
                .limit(pageRequest.getLimit())
                .toList();
    }

    public List<CommerceGiftCertificate> findByTagName(String tagName, PagePaginationBuilder pageRequest) {
        return graphQlRequest
                .executeGetByTagName(tagName, pageRequest);
    }
}
