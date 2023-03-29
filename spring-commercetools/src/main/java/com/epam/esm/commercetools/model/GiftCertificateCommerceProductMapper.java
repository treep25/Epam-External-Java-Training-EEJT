package com.epam.esm.commercetools.model;

import com.commercetools.api.models.product.Attribute;
import com.commercetools.api.models.product.Product;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import org.apache.commons.lang.NotImplementedException;
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
        }
        return giftCertificate;
    }

    public List<CommerceGiftCertificate> getListGiftCertificatesFromProductModelsList(List<Product> products) {
        List<CommerceGiftCertificate> giftCertificate = new ArrayList<>();
        products.forEach(product -> giftCertificate.add(getGiftCertificateFromProductModel(product)));

        return giftCertificate;
    }

    public Product getProductFromGiftCertificate(GiftCertificate giftCertificate) {
        throw new NotImplementedException();

    }
}
