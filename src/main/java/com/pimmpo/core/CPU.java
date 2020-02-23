package com.pimmpo.core;

import com.pimmpo.core.util.BitBMO;
import org.apache.log4j.Logger;

/**
 * created by @author pimmpo on 23.02.2020.
 */
public class CPU {
    private static final Logger log = Logger.getLogger(CPU.class);

    private int AX = 0;
    private int IP = 0;
    private boolean overflow = false;

    public CPU() {
        log.debug("(CPU)Initialization CPU");
    }

    /**
     * Метод для возвращения CPU начальных значений
     */
    public void initDefaultValues() {
        this.IP = 0;
        this.AX = 0;
        this.overflow = false;
    }

    public int getAX() {
        return AX;
    }

    public int getIP() {
        return IP;
    }

    public boolean isOverflow() {
        return overflow;
    }

    /**
     * Метод инкранирует регист IP и возвращает предыдущее значение
     */
    public int incramentIP() {
        IP++;
        return IP - 1;
    }

    /**
     * Метод позволяющий задать значение регистра AX значениям
     * из ячеек оперативной памяти
     * @param upper - upper memory cell
     * @param lower - lower memory cell
     */
    public void installAX(final char upper, final char lower) throws Exception {
        BitBMO bitsAX = new BitBMO(16);
        BitBMO bitsCell = new BitBMO(8, upper);

        for(int i = 0, j = 0; i <= 15; i++, j ++) {
            if(j > 7) {
                bitsCell.setBitsNumber((int)lower);
                j = 0;
            }
            bitsAX.setBit(i, bitsCell.getBit(j));
        }

        this.AX = bitsAX.toInteger();
    }
}
