package com.epam.esm.utils.validation;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataValidationTest {
    private final List<GiftCertificate> listObjsCorrect = List.of(new GiftCertificate().setName("test1").setDescription("test1 description").setPrice(1).setDuration(1),
            new GiftCertificate().setName("test2").setDescription("test1 description").setPrice(2).setDuration(2),
            new GiftCertificate().setName("test3").setTags(List.of(new Tag().setName("tag test3"), new Tag().setName("tag1 test3"))).setDescription("test1 description").setPrice(3).setDuration(3));
    private final List<GiftCertificate> listObjsNonCorrect = List.of(new GiftCertificate().setName("test1").setDescription("").setPrice(1).setDuration(1),
            new GiftCertificate().setName("123123").setDescription("test1 description").setPrice(2).setDuration(2),
            new GiftCertificate().setName("12312").setTags(List.of(new Tag().setName("tag test3"), new Tag().setName("tag1 test3"))).setDescription("test1 description").setPrice(3).setDuration(3));

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
        List<Tag> testObj = List.of(new Tag().setName("tag1"), new Tag().setName("tag2932"));
        assertTrue(DataValidation.isCertificateConsistsTagsOptionalValid(testObj));
    }

    @Test
    void isCertificateContainsTagsOPTIONAL_ShouldReturnFalseIfTagsNotValid() {
        List<Tag> testObj = List.of(new Tag().setName("tag1"), new Tag().setName("123123123"));
        assertFalse(DataValidation.isCertificateConsistsTagsOptionalValid(testObj));
    }

    @Test
    void isCertificateContainsTagsOPTIONAL_ShouldReturnTrueIfTagsAreNULL() {
        List<Tag> testObj = null;
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
        List<Tag> testObj = List.of(new Tag().setName("oo123"), new Tag().setName("test"));
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
        List<Tag> testObj = List.of(new Tag().setName("               "), new Tag().setName(null));
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