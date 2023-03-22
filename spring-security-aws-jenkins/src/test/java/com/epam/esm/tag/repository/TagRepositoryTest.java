package com.epam.esm.tag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
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
        tagRepository.save(Tag.builder().name("em db test").build());

        assertTrue(tagRepository.existsByName("em db test"));
    }

    @Test
    void isTagExistsByNameTest_ReturnFalseIfTagNotExists() {
        tagRepository.save(Tag.builder().name("em db test").build());

        assertFalse(tagRepository.existsByName("em db test1"));
    }

    @Test
    void getIdByTagName() {
        tagRepository.save(Tag.builder().name("em db test").build());

        assertEquals(tagRepository.getIdByTagName("em db test"), tagRepository.getIdByTagName("em db test"));

    }

    @Test
    void getTheMostWidelyUsedTag() {
        tagRepository.save(Tag.builder().name("the most widely used tag").build());
        tagRepository.save(Tag.builder().name("tag").build());
        tagRepository.save(Tag.builder().name("tag1").build());
        tagRepository.save(Tag.builder().name("tag tag").build());
        tagRepository.save(Tag.builder().name("tag t").build());
        tagRepository.save(Tag.builder().name("tag2").build());
        tagRepository.save(Tag.builder().name("the most").build());

        GiftCertificate giftCertificate1 = GiftCertificate.builder()
                .name("gc1")
                .description("gc1")
                .price(102)
                .durationDays(12)
                .tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("the most widely used tag")).name("the most widely used tag").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag")).name("tag").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag1")).name("tag1").build())).build();

        GiftCertificate giftCertificate2 = GiftCertificate.builder()
                .name("gc2")
                .description("gc2")
                .price(12)
                .durationDays(21)
                .tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("tag tag")).name("tag tag").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag1")).name("tag1").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("the most widely used tag")).name("the most widely used tag").build())).build();

        GiftCertificate giftCertificate3 = GiftCertificate.builder()
                .name("gc3")
                .description("gc3")
                .price(1228)
                .durationDays(2)
                .tags(Set.of(
                        Tag.builder().id(tagRepository.getIdByTagName("tag t")).name("tag t").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("tag2")).name("tag2").build(),
                        Tag.builder().id(tagRepository.getIdByTagName("the most widely used tag")).name("the most widely used tag").build())).build();

        giftCertificateRepository.save(giftCertificate1);
        giftCertificateRepository.save(giftCertificate2);
        giftCertificateRepository.save(giftCertificate3);

        Order order1 = Order.builder().cost(giftCertificate3.getPrice()).giftCertificate(giftCertificate3).build();
        Order order2 = Order.builder().cost(giftCertificate2.getPrice()).giftCertificate(giftCertificate2).build();
        Order order3 = Order.builder().cost(giftCertificate1.getPrice()).giftCertificate(giftCertificate1).build();
        Order order4 = Order.builder().cost(giftCertificate3.getPrice()).giftCertificate(giftCertificate3).build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);


        User user1 = User.builder().name("Name").orders(Set.of(order1, order2)).build();
        User user2 = User.builder().name("Name").orders(Set.of(order3, order1, order4)).build();
        userRepository.save(user1);
        userRepository.save(user2);


        assertEquals(tagRepository.findById(tagRepository.getIdByTagName("the most widely used tag")).get(), tagRepository.getTheMostWidelyUsedTag());
    }
}