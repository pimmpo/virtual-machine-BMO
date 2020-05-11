package com.pimmpo.core.hardware;

import com.pimmpo.core.util.BitBMO;
import org.apache.log4j.Logger;

/**
 * created by @author pimmpo on 23.02.2020.
 *
 * Класс реализующий центральный процессор виртуальной машины
 *      Размер машинного слова 16 бит
 *      Регистр AX сумматор (16 бит)
 *      Регистр IP счетчик команд(16 бит)
 *      Регистр overflow переполнения (8 бит)
 */
public class CPU {
    private static final Logger log = Logger.getLogger(CPU.class);
    private final static int BITS_SIZE_AX = 16;
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

    /**
     * Метод складывает значение хранящаяся в регистре AX
     * с некоторой ячейкой памяти
     */
    public void addingCell(final char upper, final char lower) throws Exception {
        BitBMO bitsAX = new BitBMO(16, this.AX);
        BitBMO bitsUpper = new BitBMO(8, upper);
        BitBMO bitsLower = new BitBMO(8, lower);

        //if the signs of the number are equal then add
        //else subtraction
        if(bitsAX.getBit(0) == bitsUpper.getBit(0)) {
            BitBMO bitsResult = new BitBMO(16, 0);

            for (int i = 15, j = 7; i > 0; i--, j--) {
                //when we adding all bit lower operand we change it on upper
                if(j < 0) {
                    j = 7;
                    bitsLower = bitsUpper;
                }

                if(bitsAX.getBit(i) + bitsLower.getBit(j) + bitsResult.getBit(i) == 1) {
                    bitsResult.setBit(i, (byte)1);
                } else if (bitsAX.getBit(i) + bitsLower.getBit(j) + bitsResult.getBit(i) == 2) {
                    bitsResult.setBit(i - 1, (byte)1);
                } else if (bitsAX.getBit(i) + bitsLower.getBit(j) + bitsResult.getBit(i) == 3) {
                    bitsResult.setBit(i, (byte)1);
                    bitsResult.setBit(i - 1, (byte)1);
                }
            }
            this.AX = bitsResult.toInteger();
        } else {
            subtractionCell(upper, lower);
        }
    }

    public void subtractionCell(final char upper, final char lower) throws Exception {
        BitBMO bitsAX = new BitBMO(16, AX);
        BitBMO bitsSubtracted = new BitBMO(16, 0);
        BitBMO bitsOperand = new BitBMO(8, upper);

        //Заполняем вычисляемое число значениями из верхней и нижней ячейки
        for(int i = 0, j = 0; i < 16; i++, j++) {
            if(i == 8) {
                j = 0;
                bitsOperand.setBitsNumber(lower);
            }
            bitsSubtracted.setBit(i, (byte)bitsOperand.getBit(j));
        }

        //change values sing
        if(bitsSubtracted.getBit(0) == 0) {
            bitsSubtracted.setBit(0, (byte) 1);
        } else {
            bitsSubtracted.setBit(0, (byte) 1);
        }

        if(bitsAX.getBit(0) != bitsSubtracted.getBit(0)) {
            int choose = biggerByModule(bitsAX, bitsSubtracted);
            if (choose == -1) {
                BitBMO template = bitsAX;
                bitsAX = bitsSubtracted;
                bitsSubtracted = template;
            } else if (choose == 0) {
                this.AX = 0;
                return;
            }

            for (int i = 15; i > 0; i--) {
                if (bitsAX.getBit(i) - bitsSubtracted.getBit(i) == -1) {
                    for (int j = i - 1; j > 0; j--) {
                        if (bitsAX.getBit(j) == 1) {
                            bitsAX.setBit(j, (byte) 0);
                            for (int z = j + 1; z <= i; z++) {
                                bitsAX.setBit(z, (byte) 1);
                            }
                            break;
                        }
                    }
                } else if (bitsAX.getBit(i) - bitsSubtracted.getBit(i) == 0) {
                    bitsAX.setBit(i, (byte) 0);
                }
            }

            //save AX
            this.AX = bitsAX.toInteger();
        } else {
            addingCell(upper, lower);
        }
    }

    /**
     * Метод сравнивает значение по модулю
     * @param AX - AX register
     * @param operand - number from memory 16 bit[upper and lower]
     * @return 1 if AX bigger, -1 else if operand bigger and 0 else AX == operand
     */
    private static int biggerByModule(BitBMO AX, BitBMO operand) throws Exception {
        //Start with number 1 because we don't compare signs

        if(AX.getSize() != operand.getSize()) {
            log.error("(CPU)Вifferent size of variables");
            throw new RuntimeException("(CPU)Вifferent size of variables");
        }

        for(int i = 1; i < BITS_SIZE_AX; i++) {
            if(AX.getBit(i) > operand.getBit(i)) {
                return 1;
            } else if(AX.getBit(i) < operand.getBit(i)) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Метод изменяющий регистр IP из операнда
     */
    public void absolutleJumpIP(final char operand) {
        this.IP = (int)operand;
    }

    public void jumpAXequipZero(final char operand) {
        BitBMO bitsZero = new BitBMO(16, 0);
        BitBMO bitsNegativeZero = new BitBMO(16, "1000000000000000");

        if(AX == bitsZero.toInteger() || AX == bitsNegativeZero.toInteger()) {
            IP = (int)operand;
        }
    }

    public String getAXtoBin() {
        BitBMO ax = new BitBMO(16, this.AX);
        return ax.toString();
    }
}
