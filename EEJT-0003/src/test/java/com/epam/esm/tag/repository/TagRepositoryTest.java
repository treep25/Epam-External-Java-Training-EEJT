package com.epam.esm.tag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.query.SqlQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TagRepositoryTest {

    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Test
    void isTagExistsByNameTest_ReturnTrueIfTagExists() {
        tagRepository.save(new Tag().builder().name("em db test").build());

        assertTrue(tagRepository.isTagExistsByName("em db test"));
    }

    @Test
    void isTagExistsByNameTest_ReturnFalseIfTagNotExists() {
        tagRepository.save(new Tag().builder().name("em db test").build());

        assertFalse(tagRepository.isTagExistsByName("em db test1"));
    }

    @Test
    void getIdByTagName() {
        tagRepository.save(new Tag().builder().name("em db test").build());

        assertEquals(tagRepository.getIdByTagName("em db test"), tagRepository.getIdByTagName("em db test"));

    }

    @Test
    void getTheMostWidelyUsedTag() {
        tagRepository.save(new Tag().builder().name("the most widely used tag").build());
        tagRepository.save(new Tag().builder().name("tag").build());
        tagRepository.save(new Tag().builder().name("tag1").build());
        tagRepository.save(new Tag().builder().name("tag tag").build());
        tagRepository.save(new Tag().builder().name("tag t").build());
        tagRepository.save(new Tag().builder().name("tag2").build());
        tagRepository.save(new Tag().builder().name("the most").build());

        GiftCertificate giftCertificate1 = new GiftCertificate().builder()
                .name("gc1")
                .description("gc1")
                .price(102)
                .durationDays(12)
                .tags(Set.of(
                        new Tag().builder().id(tagRepository.getIdByTagName("the most widely used tag")).name("the most widely used tag").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("tag")).name("tag").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("tag1")).name("tag1").build())).build();

        GiftCertificate giftCertificate2 = new GiftCertificate().builder()
                .name("gc2")
                .description("gc2")
                .price(12)
                .durationDays(21)
                .tags(Set.of(
                        new Tag().builder().id(tagRepository.getIdByTagName("tag tag")).name("tag tag").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("tag1")).name("tag1").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("the most widely used tag")).name("the most widely used tag").build())).build();

        GiftCertificate giftCertificate3 = new GiftCertificate().builder()
                .name("gc3")
                .description("gc3")
                .price(122)
                .durationDays(2)
                .tags(Set.of(
                        new Tag().builder().id(tagRepository.getIdByTagName("tag t")).name("tag t").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("tag2")).name("tag2").build(),
                        new Tag().builder().id(tagRepository.getIdByTagName("the most")).name("the most").build())).build();

        giftCertificateRepository.save(giftCertificate1);
        giftCertificateRepository.save(giftCertificate2);
        giftCertificateRepository.save(giftCertificate3);

        Order order1 = new Order().builder().cost(giftCertificate3.getPrice()).giftCertificate(giftCertificate3).build();
        Order order2 = new Order().builder().cost(giftCertificate2.getPrice()).giftCertificate(giftCertificate2).build();
        Order order3 = new Order().builder().cost(giftCertificate1.getPrice()).giftCertificate(giftCertificate1).build();
        Order order4 = new Order().builder().cost(giftCertificate3.getPrice()).giftCertificate(giftCertificate3).build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);


        User user1 = new User().builder().name("Name").orders(Set.of(order1, order2)).build();
        User user2 = new User().builder().name("Name").orders(Set.of(order3, order1)).build();
        userRepository.save(user1);
        userRepository.save(user2);


        assertEquals(tagRepository.findById(tagRepository.getIdByTagName("the most widely used tag")).get(), tagRepository.getTheMostWidelyUsedTag());
    }
}