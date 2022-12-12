package com.epam;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void returnTrueWhenListOfStrAreNumbersAndMoreThenZeroTest() {
        String[] arrayTestObjs = new String[]{"123", "123442534", "92399324"};

        assertTrue(Utils.isAllPositiveNumbers(arrayTestObjs));
    }

    @Test
    void returnFalseWhenInListOfStrOneNumberIsNotFitButOthersAreNumbersAndMoreThenZeroTest() {
        String[] arrayTestObjs = new String[]{"123", "!@#$@%#^$^", "92399324"};

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));
    }

    @Test
    void returnFalseWhenListOfStrAreNotNumbers() {
        String[] arrayTestObjs = new String[]{"!@#$@%#^$%@@1", "$#@$#@!!!", "@#@#@#$$#@@##@4"};

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));
    }

    @Test
    void returnFalseWhenStrIsNumberLessThenZeroTest() {
        String[] arrayTestObjs = new String[]{"-12312", "-324342324324", "0"};

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));
    }

    @Test
    void returnFalseWhenStrIsSpaceTest() {
        String[] arrayTestObjs = new String[]{"     ", "   ", ""};

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));
    }

    @Test
    void returnFalseWhenEmptyCaseTest() {
        String[] arrayTestObjs = new String[]{};

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));

    }

    @Test
    void returnFalseWhenNullCaseTest() {
        String[] arrayTestObjs = null;

        assertFalse(Utils.isAllPositiveNumbers(arrayTestObjs));

    }
}