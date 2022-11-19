package task2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static task2.InputValidation.*;

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
        trueListName.forEach(x1->assertTrue(isNameValid(x1)));
        falseListName.forEach(x1->assertFalse(isNameValid(x1)));
    }

    @Test
    void isExtValidTest() {
        trueListExt.forEach(x1->assertTrue(isExtValid(x1)));
        falseListExt.forEach(x1->assertFalse(isExtValid(x1)));
    }

    @Test
    void isSizeValidTest() {
        assertTrue(isSizeValid(trueSizeMin,trueSizeMax));
        assertFalse(isSizeValid(falseSizeMin,falseSizeMax));
        assertFalse(isSizeValid(falseSizeMin1,falseSizeMax));
    }
}