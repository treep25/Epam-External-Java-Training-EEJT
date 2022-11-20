package task2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputValidationTest {
    private List<String> trueListName = List.of("name1","nNAME1_Q","qwewr123");
    private List<String> falseListName = List.of("!@#$%^","1231#");
    private List<String> trueListExt = List.of(".teeeeee",".txt",".docx");
    private List<String> falseListExt = List.of("   ","#","/%$%45");
    private String trueSizeMin = "1";
    private String trueSizeMax = "1000";
    private String falseSizeMin="-123";
    private String falseSizeMin1="1000";
    private String falseSizeMax="1";

    @Test
    void isNameValidTest() {
        trueListName.forEach(x1->assertTrue(InputValidation.isNameValid(x1)));
        falseListName.forEach(x1->assertFalse(InputValidation.isNameValid(x1)));
    }

    @Test
    void isExtValidTest() {
        trueListExt.forEach(x1->assertTrue(InputValidation.isExtValid(x1)));
        falseListExt.forEach(x1->assertFalse(InputValidation.isExtValid(x1)));
    }

    @Test
    void isSizeValidTest() {
        assertTrue(InputValidation.isSizeValid(trueSizeMin,trueSizeMax));
        assertFalse(InputValidation.isSizeValid(falseSizeMin,falseSizeMax));
        assertFalse(InputValidation.isSizeValid(falseSizeMin1,falseSizeMax));
    }
}