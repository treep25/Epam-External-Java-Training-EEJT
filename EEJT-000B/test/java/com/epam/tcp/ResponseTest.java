package com.epam.tcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void getStringTest() {
        String expected = "qwerty";
        String actual = new Response("qwerty").getString();

        assertEquals(expected, actual);
    }
}