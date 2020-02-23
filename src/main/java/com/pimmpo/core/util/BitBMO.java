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

    public BitBMO(int size, int number) {
        this.size = size;
        bits = new byte[size];
        this.setBitsNumber(number);
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
        return (char)toInteger();
    }

    /**
     * Метод переводящий значение bits в int представление
     * @return int value from bits
     */
    public int toInteger() {
        int result = 0;
        for(int i = bits.length - 1, j = 1; i >= 0; i--, j *= 2) {
            if(bits[i] == 1) {
                result += j;
            }
        }
        return result;
    }

    /**
     * @param number - integer number
     * @result translate from integer in to bitBMO
     */
    public void setBitsNumber(int number) {
        for(int i = bits.length - 1, j = 0; i >= 0; i--, j++) {
            if(number - Math.pow(2, i) > -1) {
                bits[j] = 1;
                number -= Math.pow(2, i);
            } else {
                bits[j] = 0;
            }
        }
    }

    public void setBit(int index, byte choose) {
        if (choose >  1 || choose < 0) {
            return;
        }
        if(choose == 1) {
            this.bits[index] = 1;
        } else {
            this.bits[index] = 0;
        }
    }

    public byte getBit(int index) throws Exception {
        if(index > bits.length || index < 0) {
            throw new Exception("Failed index, working range [0," + bits.length + "]");
        } else {
            return bits[index];
        }
    }
}
