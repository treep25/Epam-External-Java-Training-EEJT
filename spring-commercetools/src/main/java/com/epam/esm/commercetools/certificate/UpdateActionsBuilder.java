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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UpdateActionsBuilder {

    private static final String CURRENCY_CODE = "UAH";
    private static final String DURATION = "Duration";
    private static final String TAGS = "Tags";
    private final Map<Predicate<GiftCertificate>, Function<GiftCertificate, ProductUpdateAction>> updatesMap =
            Map.of(
                    giftCertificate -> giftCertificate.getName() != null,
                    giftCertificate -> ProductUpdateAction
                            .changeNameBuilder()
                            .name(LocalizedString
                                    .ofEnglish(giftCertificate.getName()))
                            .build(),

                    giftCertificate -> giftCertificate.getDescription() != null,
                    giftCertificate -> ProductUpdateAction
                            .setDescriptionBuilder()
                            .description(LocalizedString
                                    .ofEnglish(giftCertificate.getDescription()))
                            .build(),

                    giftCertificate -> giftCertificate.getPrice() != null,
                    giftCertificate -> ProductUpdateAction
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
                            .build(),

                    giftCertificate -> giftCertificate.getDurationDays() != null,
                    giftCertificate -> {
                        Attribute durationAttribute = setDurationAttribute(giftCertificate.getDurationDays());

                        return ProductUpdateAction
                                .setAttributeBuilder()
                                .variantId(1L)
                                .name(durationAttribute.getName())
                                .value(durationAttribute.getValue())
                                .build();
                    },

                    giftCertificate -> giftCertificate.getTags() != null,
                    giftCertificate -> {
                        Attribute tagAttribute = setTagsAttributeWhenUpdateGiftCertificate(giftCertificate.getTags());

                        return ProductUpdateAction
                                .setAttributeBuilder()
                                .variantId(1L)
                                .name(tagAttribute.getName())
                                .value(tagAttribute.getValue())
                                .build();
                    }
            );

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
        updatesMap.forEach((key, value) -> {
            if (key.test(giftCertificateUpdates)) {
                productUpdateActions
                        .add(value.apply(giftCertificateUpdates));
            }
        });

        productUpdateActions.add(ProductUpdateAction.publishBuilder().build());

        return productUpdateActions;
    }
}
