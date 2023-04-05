package com.epam.esm.commercetools.model;

import com.commercetools.api.models.product.Attribute;
import com.commercetools.api.models.product.Product;
import com.epam.esm.commercetools.graphql.responseModel.ResponseGraphQlModel;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.*;

@Component
public class GiftCertificateCommerceProductMapper {

    public CommerceGiftCertificate getGiftCertificateFromProductModel(Product product) {
        CommerceGiftCertificate giftCertificate = CommerceGiftCertificate.builder()
                .productId(product.getId())
                .name(product
                        .getMasterData()
                        .getStaged()
                        .getName()
                        .get(Locale.ENGLISH))
                .price(product
                        .getMasterData()
                        .getStaged()
                        .getMasterVariant()
                        .getPrices()
                        .get(0)
                        .getValue()
                        .getCentAmount()
                        .intValue())
                .description(product
                        .getMasterData()
                        .getStaged()
                        .getDescription()
                        .get(Locale.ENGLISH))
                .createDate(Date.from(product
                        .getCreatedAt().toInstant()))
                .lastUpdateDate(Date.from(product
                        .getLastModifiedAt().toInstant()))
                .build();

        List<Attribute> attributes = product.getMasterData()
                .getStaged()
                .getMasterVariant()
                .getAttributes();

        giftCertificate.setDurationDays(Integer.parseInt(attributes.get(0).getValue().toString()));

        attributes.remove(0);

        if (attributes.iterator().hasNext()) {
            Set<CommerceTag> tags = new HashSet<>();
            List<String> tagNames = (List<String>) attributes.iterator().next().getValue();

            tagNames.forEach(name -> tags.add(CommerceTag.builder().name(name).build()));

            giftCertificate.setTags(tags);
        } else {
            giftCertificate.setTags(Set.of());
        }
        return giftCertificate;
    }

    public List<CommerceGiftCertificate> getListGiftCertificatesFromProductModelsList(List<Product> products) {
        List<CommerceGiftCertificate> commerceGiftCertificates = new ArrayList<>();
        products.forEach(product -> commerceGiftCertificates.add(getGiftCertificateFromProductModel(product)));

        return commerceGiftCertificates;
    }

    public List<CommerceGiftCertificate> getGiftCertificateListFromGraphQlResponseModelList
            (List<ResponseGraphQlModel> responseGraphQlModels) {

        if (!responseGraphQlModels.isEmpty()) {
            List<CommerceGiftCertificate> commerceGiftCertificates = new ArrayList<>();

            responseGraphQlModels.forEach(
                    responseGraphQlModel -> {
                        CommerceGiftCertificate commerceGiftCertificate = CommerceGiftCertificate
                                .builder()
                                .productId(responseGraphQlModel.getId())
                                .name(responseGraphQlModel
                                        .getMasterData()
                                        .getCurrent()
                                        .getNameAllLocales()
                                        .get(0).getValue())
                                .description(responseGraphQlModel
                                        .getMasterData()
                                        .getCurrent()
                                        .getDescriptionAllLocales()
                                        .get(0).getValue())
                                .price(Integer.parseInt(String
                                        .valueOf(responseGraphQlModel
                                                .getMasterData()
                                                .getCurrent()
                                                .getMasterVariant()
                                                .getPrices().get(0).getValue().getCentAmount())))
                                .durationDays(Integer.parseInt(responseGraphQlModel
                                        .getMasterData()
                                        .getCurrent()
                                        .getMasterVariant()
                                        .getAttributesRaw()
                                        .get(0).getValue().toString()))
                                .createDate(Date
                                        .from(responseGraphQlModel
                                                .getCreatedAt()
                                                .toInstant()))
                                .lastUpdateDate(Date
                                        .from(responseGraphQlModel
                                                .getLastModifiedAt()
                                                .toInstant()))
                                .build();

                        List<String> tagNames = (List<String>) responseGraphQlModel
                                .getMasterData()
                                .getCurrent()
                                .getMasterVariant()
                                .getAttributesRaw()
                                .get(1).getValue();
                        Set<CommerceTag> tags = new HashSet<>();

                        tagNames.forEach(tagName ->
                                tags.add(CommerceTag.builder().name(tagName).build()));

                        commerceGiftCertificate
                                .setTags(tags);

                        commerceGiftCertificates.add(commerceGiftCertificate);
                    }
            );
            return commerceGiftCertificates;

        }
        return List.of();
    }
}
