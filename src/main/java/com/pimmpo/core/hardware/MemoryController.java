package com.pimmpo.core.hardware;

import com.pimmpo.core.model.MemoryObject;
import com.pimmpo.core.util.BitBMO;

/**
 * created by @author pimmpo on 26.02.2020
 *
 * Класс контроллер для работы с манипуляциями проводимыми
 * для оперативной памяти виртуальной машины
 */
public class MemoryController {
    public MemoryController() {
        //TODO: Singleton?
    }

    /**
     * Метод позволяющий перевести 16 битный регистр в две последовательные
     * ячейки по 8 бит
     *
     * @param AX - AX register
     * @return MemoryObject - for save into memory
     */
    public MemoryObject separationRegisterAX(int AX) {
        BitBMO bitsAX = new BitBMO(16, AX);
        BitBMO bitsOperand = new BitBMO(8,0);

        MemoryObject memObject = new MemoryObject();
        for(int i = 0, j = 0; i < 16; i++) {
            if(i < 8) {
                bitsOperand.setBit(i, bitsAX.getBit(i));
                memObject.setUpper(bitsOperand.toChar());
            } else {
                bitsOperand.setBit(j, bitsAX.getBit(i));
                j++;
            }
        }
        memObject.setLower(bitsOperand.toChar());

        return memObject;
    }
}
