package com.pimmpo.core.util;

/**
 *  created by @author pimmpo on 23.02.2020.
 */
public class BitBMO {
    private byte bits[];
    private int size;   //size word

    public BitBMO(int size) {
        this.size = size;
        bits = new byte[size];
    }

    /**
     * Метод позволяющий устнавливать значение каждого бита
     * из String
     * @param value - values cell
     */
    public void setBitsFromString(String value) {
        for(int i = 0; i < size; i++) {
            if(value.charAt(i) == '1') {
                bits[i] = 1;
            } else {
                bits[i] = 0;
            }
        }
    }

    /**
     * Метод переводящий значение bits в char представление
     * @return char value from bits
     */
    public char toChar() {
        int result = 0;
        for(int i = bits.length - 1, j = 1; i >= 0; i--, j *= 2) {
            if(bits[i] == 1) {
                result += j;
            }
        }
        return (char)result;
    }
}
