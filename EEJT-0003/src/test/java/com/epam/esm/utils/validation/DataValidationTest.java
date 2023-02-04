package com.epam.esm.utils.validation;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataValidationTest {
    private final List<GiftCertificate> listObjsCorrect = List.of(new GiftCertificate().builder().name("test1").description("test1 description").price(1).durationDays(1).build(),
            new GiftCertificate().builder().name("test2").description("test1 description").price(2).durationDays(2).build(),
            new GiftCertificate().builder().name("test3").tags(Set.of(new Tag().builder().name("tag test3").build(), new Tag().builder().name("tag1 test3").build())).description("test1 description").price(3).durationDays(3).build());
    private final List<GiftCertificate> listObjsNonCorrect = List.of(new GiftCertificate().builder().name("test1").description("").price(1).durationDays(1).build(),
            new GiftCertificate().builder().name("123123").description("test1 description").price(2).durationDays(2).build(),
            new GiftCertificate().builder().name("12312").tags(Set.of(new Tag().builder().name("tag test3").build(), new Tag().builder().name("tag1 test3").build())).description("test1 description").price(3).durationDays(3).build());

    @Test
    void verifyThatGiftCertificateValid_ShouldReturnTrueIfCertificateValid() {
        for (GiftCertificate testObj : listObjsCorrect) {
            assertTrue(DataValidation.isValidCertificate(testObj));
        }
    }

    @Test
    void verifyThatGiftCertificateValid_ShouldReturnFalseIfCertificateNOTValid() {
        for (GiftCertificate testObj : listObjsNonCorrect) {
            assertFalse(DataValidation.isValidCertificate(testObj));
        }
    }

    @Test
    void isCertificateContainsTagsOPTIONAL_ShouldReturnTrueIfTagsValid() {
        Set<Tag> testObj = Set.of(new Tag().builder().name("tag test3").build(), new Tag().builder().name("tag tag").build());
        assertTrue(DataValidation.isCertificateConsistsTagsOptionalValid(testObj));
    }

    @Test
    void isCertificateContainsTagsOPTIONAL_ShouldReturnFalseIfTagsNotValid() {
        Set<Tag> testObj = Set.of(new Tag().builder().name("tag test3").build(), new Tag().builder().name("124353212").build());
        assertFalse(DataValidation.isCertificateConsistsTagsOptionalValid(testObj));
    }

    @Test
    void isCertificateContainsTagsOPTIONAL_ShouldReturnTrueIfTagsAreNULL() {
        Set<Tag> testObj = null;
        assertTrue(DataValidation.isCertificateConsistsTagsOptionalValid(testObj));
    }

    @Test
    void moreThenZero_ShouldReturnTrue() {
        List<Long> testObjs = List.of(12L, 3432L, 542L);
        for (long id : testObjs) {
            assertTrue(DataValidation.moreThenZero(id));
        }
    }

    @Test
    void moreThenZero_ShouldReturnFalseLessThenZeroOrZero() {
        List<Long> testObjs = List.of(0L, -23L, -52L);
        for (long id : testObjs) {
            assertFalse(DataValidation.moreThenZero(id));

        }
    }

    @Test
    void isValidTag_ShouldReturnTrue() {
        List<Tag> testObj = List.of(new Tag().builder().name("tag test3").build(), new Tag().builder().name("tag tag").build());
        for (Tag tag : testObj) {
            assertTrue(DataValidation.isValidTag(tag));
        }
    }

    @Test
    void isValidTag_ShouldReturnFalseIfTagIsNull() {
        Tag testObj = null;
        assertFalse(DataValidation.isValidTag(testObj));
    }

    @Test
    void isValidTag_ShouldReturnFalseIfTagNameIsNumericOrNotValid() {
        List<Tag> testObj = List.of(new Tag().builder().name("      ").build(), new Tag().builder().name(null).build());
        for (Tag tag : testObj) {
            assertFalse(DataValidation.isValidTag(tag));
        }
    }

    @Test
    void isGiftCertificateValidForUpdating() {

    }

    @Test
    void isStringValid_ReturnTrue() {
        List<String> testObjs = List.of("test1", "test2", "43242test32423");
        for (String testObj : testObjs) {
            assertTrue(DataValidation.isStringValid(testObj));
        }
    }

    @Test
    void isStringValid_ReturnFalseWhenNull() {
        String testObj = null;
        assertFalse(DataValidation.isStringValid(testObj));

    }

    @Test
    void isStringValid_ReturnFalseWhenEmpty() {
        String testObj = "                           ";
        assertFalse(DataValidation.isStringValid(testObj));

    }

    @Test
    void isSortingTypeContain_ReturnTrueWhenDescOrAsc() {
        List<String> testObjs = List.of("DESC", "ASC", "desc", "asc", "dEsc", "AsC");
        for (String testObj : testObjs) {
            assertTrue(DataValidation.isSortingTypeContains(testObj));
        }
    }

    @Test
    void isSortingTypeContain_ReturnFalse() {
        List<String> testObjs = List.of("qweqwe", "qwe", "123", "ASCCC", "DESCEE", "e");
        for (String testObj : testObjs) {
            assertFalse(DataValidation.isSortingTypeContains(testObj));
        }
    }
}