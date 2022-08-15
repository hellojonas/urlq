package dev.hellojonas;

public interface Parser<T> {
    T parse(String str);
}
