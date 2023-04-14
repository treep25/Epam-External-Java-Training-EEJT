package com.epam.esm.commercetools.certificate;

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
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UpdateActionsBuilder {

    private static final String CURRENCY_CODE = "UAH";
    private static final String DURATION = "Duration";
    private static final String TAGS = "Tags";
    private final Map<Predicate<GiftCertificate>, Consumer<List<ProductUpdateAction>>> updatesMap = new HashMap<>();
    private Attribute setDurationAttribute(int duration) {
        return Attribute
                .builder()
                .name(DURATION)
                .value(duration)
                .build();

    }

    private Attribute setTagsAttributeWhenUpdateGiftCertificate(Set<Tag> tags) {
        if (tags.isEmpty()) {
            return Attribute
                    .builder()
                    .name(TAGS)
                    .value(List.of())
                    .build();
        }

        return Attribute
                .builder()
                .name(TAGS)
                .value(
                        tags.stream().map(Tag::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public List<ProductUpdateAction> preparingUpdatesForGiftCertificate(GiftCertificate giftCertificateUpdates) {
        List<ProductUpdateAction> productUpdateActions = new ArrayList<>();

        updatesMap.put(giftCertificate -> giftCertificateUpdates.getName() != null,
                list -> list.add(ProductUpdateAction
                        .changeNameBuilder()
                        .name(LocalizedString
                                .ofEnglish(giftCertificateUpdates.getName()))
                        .build()));

        updatesMap.put(giftCertificate -> giftCertificateUpdates.getDescription() != null,
                list -> list.add(ProductUpdateAction
                        .setDescriptionBuilder()
                        .description(LocalizedString
                                .ofEnglish(giftCertificateUpdates.getDescription()))
                        .build()));

        updatesMap.put(giftCertificate -> giftCertificateUpdates.getPrice() != null,
                list -> list.add(ProductUpdateAction
                        .setPricesBuilder()
                        .variantId(1L)
                        .prices(PriceDraft
                                .builder()
                                .value(Money
                                        .builder()
                                        .currencyCode(CURRENCY_CODE)
                                        .centAmount(Long.parseLong(String.valueOf(giftCertificateUpdates.getPrice())))
                                        .build())
                                .build())
                        .build()));

        updatesMap.put(giftCertificate -> giftCertificateUpdates.getDurationDays() != null,
                list -> {
                    Attribute durationAttribute = setDurationAttribute(giftCertificateUpdates.getDurationDays());

                    list.add(ProductUpdateAction
                            .setAttributeBuilder()
                            .variantId(1L)
                            .name(durationAttribute.getName())
                            .value(durationAttribute.getValue())
                            .build());
                });
        updatesMap.put(giftCertificate -> giftCertificateUpdates.getTags() != null,
                list -> {
                    Attribute tagAttribute = setTagsAttributeWhenUpdateGiftCertificate(giftCertificateUpdates.getTags());

                    list.add(ProductUpdateAction
                            .setAttributeBuilder()
                            .variantId(1L)
                            .name(tagAttribute.getName())
                            .value(tagAttribute.getValue())
                            .build());
                });

        updatesMap.forEach((key, value) -> {
            if (key.test(giftCertificateUpdates)) {
                value.accept(productUpdateActions);
            }
        });

        productUpdateActions.add(ProductUpdateAction.publishBuilder().build());

        return productUpdateActions;
    }
}
