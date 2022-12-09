package com.epam;

public interface Command<T> {
    T execute(String request);
}
