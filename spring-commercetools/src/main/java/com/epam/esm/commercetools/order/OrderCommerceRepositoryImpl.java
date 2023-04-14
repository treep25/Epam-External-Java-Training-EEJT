package com.epam.esm.commercetools.order;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderCommerceRepositoryImpl implements OrderCommerceRepository<Order> {

    private final ProjectApiRoot apiRoot;

    @Override
    public Order create() {
//        return apiRoot
//                .orders()
//                .create(OrderFromCartDraft
//                        .builder()
//                        .paymentState(PaymentState.PENDING)
//                        .build())
        return null;
    }
}
