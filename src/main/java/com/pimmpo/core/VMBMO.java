package com.pimmpo.core;

import com.pimmpo.core.util.Command;
import com.pimmpo.core.util.io.IOBMO;
import org.apache.log4j.Logger;

/**
 * created by @author pimmpo on 23.02.2020.
 *
 */
public class VMBMO {
    private static final Logger log = Logger.getLogger(VMBMO.class);

    public static final int MEMORY_SIZE = 256;

    private CPU cpu = new CPU();
    private char memory[];

    /**
     * Конуструктор для инициализации вирутальной машины и заполнения
     * ячеек оперативной памяти из файла
     *
     * @param filename - name's file with memory values
     */
    public VMBMO(String filename) {
        log.info("(VMBMO)Initialization virtual machine's memory from file");

        memory = new char[MEMORY_SIZE];
        memory = IOBMO.memoryReadFromFile(filename);

        log.debug("(VMBMO)Memory successfully read");
    }

    /**
     *  Метод для запуска виртуальной машины
     */
    public void run() throws Exception {
        initDefaultValues();

        boolean runVM = true;
        char instruction,   //stores current instruction
                operand;    //stores current operand

        while (runVM == true && cpu.getIP() < MEMORY_SIZE) {
            instruction = memory[cpu.incramentIP()];
            operand = memory[cpu.incramentIP()];

            switch (instruction) {
                case (Command.END): {
                    runVM = false;
                    break;
                } case (Command.DWN): {
                    cpu.installAX(memory[operand], memory[operand + 1]);
                    break;
                }
                default: {
                    runVM = false;
                    log.error("(VMBMO) don't understand command");
                    break;
                }
            }
            log.info("registers: AX = " + cpu.getAX() + " IP = " + cpu.getIP() + " instruction = " + (int)instruction);
        }
    }

    private void initDefaultValues() {
        cpu.initDefaultValues();;
    }

}
