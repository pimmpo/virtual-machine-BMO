package com.pimmpo.core;

import com.pimmpo.core.util.io.IOBMO;
import org.apache.log4j.Logger;

/**
 * created by @author pimmpo on 23.02.2020.
 *
 */
public class VMBMO {
    private static final Logger log = Logger.getLogger(VMBMO.class);

    public static final int MEMORY_SIZE = 256;

    private int AX = 0;
    private int IP = 0;
    private boolean overflow = false;
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

}
