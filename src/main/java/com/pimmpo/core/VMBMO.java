package com.pimmpo.core;

import com.pimmpo.core.hardware.CPU;
import com.pimmpo.core.hardware.MemoryController;
import com.pimmpo.core.model.MemoryObject;
import com.pimmpo.core.util.BitBMO;
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

    private CPU cpu = new CPU();                                        //Центральный процессор виратульной машины
    private MemoryController memoryController = new MemoryController(); //Контроллер для помощи взаимодействия с оперативной памятью
    private char memory[];                                              //Оперативная память

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
     * Конструктор для инициализации виртуальной машины внутри веб сервиса
     */
    public VMBMO() {
        log.info("(VMBMO) virtual machine is BMO created!");
        String[] input = new String[MEMORY_SIZE];

        String memoryDefault = "00000000";
        for(int i = 0; i < MEMORY_SIZE; i++) {
            input[i] = memoryDefault;
        }

        memory = IOBMO.memoryReadFromText(input);

    }

    /**
     *  Метод для запуска виртуальной машины
     */
    public void run() throws Exception {
        initDefaultValues();    //Нужно при использование внутри веб-сервиса

        boolean runVM = true;
        char instruction,   //stores current instruction
                operand;    //stores current operand
        BitBMO bitsAX = new BitBMO(16, 0);
        while (runVM == true && cpu.getIP() < MEMORY_SIZE) {
            instruction = memory[cpu.incramentIP()];
            operand = memory[cpu.incramentIP()];

            switch (instruction) {
                case (Command.END): {   //000
                    runVM = false;
                    break;
                } case (Command.DWN): { //001
                    cpu.installAX(memory[operand], memory[operand + 1]);
                    break;
                } case (Command.ADD): { //010
                    cpu.addingCell(memory[operand], memory[operand + 1]);
                    break;
                } case (Command.SUB): { //011
                    cpu.subtractionCell(memory[operand], memory[operand + 1]);
                    break;
                } case (Command.UDWN): {//100
                    MemoryObject object = memoryController.separationRegisterAX(cpu.getAX());
                    memory[operand] = object.getUpper();
                    memory[operand + 1] = object.getLower();
                    break;
                } case (Command.JUMP): {
                    cpu.absolutleJumpIP(operand);
                    break;
                } case (Command.EQZ): {
                    cpu.jumpAXequipZero(operand);
                    break;
                }
                default: {
                    runVM = false;
                    log.error("(VMBMO) don't understand command");
                    break;
                }
            }
            bitsAX.setBitsNumber(cpu.getAX());

            log.debug("AX = " + bitsAX.toString() + " IP = " + cpu.getIP() + " instruction = " + (int)instruction);
        }
    }

    /**
     * Метод приводит все значения, кроме оперативной памяти к исходным значениям
     * необходимо для корректной работы класса внутри веб приложения
     */
    private void initDefaultValues() {
        cpu.initDefaultValues();;
    }

    public CPU getCpu() {
        return cpu;
    }

    /**
     * @return strMemory - array String memory in bin code
     **/
    public String[] getMemoryStep() {
        String[] strMemory = new String[MEMORY_SIZE];
        for(int i = 0; i < MEMORY_SIZE; i++) {
            BitBMO bitBMO = new BitBMO(8, memory[i]); //todo refactoring
            strMemory[i] = bitBMO.toString();
        }
        return strMemory;
    }

    /**
     * Загрузка оперативной памяти в виртуальную машину с страницы браузера
     */
    public void downloadMemoryFromPage(String[] memoryCells) {
        memory = IOBMO.memoryReadFromText(memoryCells);
    }
}
