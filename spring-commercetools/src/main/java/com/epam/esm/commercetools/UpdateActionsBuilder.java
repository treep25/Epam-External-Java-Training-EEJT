package com.epam.esm.commercetools;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.common.PriceDraft;
import com.commercetools.api.models.product.Attribute;
import com.commercetools.api.models.product.ProductUpdateAction;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UpdateActionsBuilder {

    private static final String CURRENCY_CODE = "UAH";
    private static final String DURATION = "Duration";
    private static final String TAGS = "Tags";
    private final ProjectApiRoot apiRoot;


    private Attribute setDurationAttribute(int duration) {
        return Attribute
                .builder()
                .name(DURATION)
                .value(duration)
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

        return Attribute
                .builder()
                .name(TAGS)
                .value(tagsNames)
                .build();
    }

    public List<ProductUpdateAction> preparingUpdatesForGiftCertificate(GiftCertificate giftCertificate, String id) {
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
}
