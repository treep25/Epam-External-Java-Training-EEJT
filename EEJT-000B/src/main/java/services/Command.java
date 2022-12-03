package services;

public interface Command<T> {
    T execute();
}
