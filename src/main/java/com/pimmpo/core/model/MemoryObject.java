package com.pimmpo.core.model;

/**
 * created by @author pimmpo on 26.02.2020.
 *
 * Вспомогательный класс для работы с машинным словом
 */
public class MemoryObject {
    private char upper;
    private char lower;

    public MemoryObject(char upper, char lower) {
        this.upper = upper;
        this.lower = lower;
    }

    public MemoryObject() {};

    public char getUpper() {
        return upper;
    }

    public char getLower() {
        return lower;
    }

    public void setUpper(char upper) {
        this.upper = upper;
    }

    public void setLower(char lower) {
        this.lower = lower;
    }
}
