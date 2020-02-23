package com.pimmpo.core.util.io;

import com.pimmpo.core.util.BitBMO;
import com.pimmpo.core.VMBMO;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * created by @author pimmpo on 23.02.2020.
 *
 */
public class IOBMO {
    private static final Logger log = Logger.getLogger(IOBMO.class);

    private IOBMO() {
        log.error("Class (type util) " + this.getClass().getName() + " created!");
        throw new RuntimeException("Class (type util) " + this.getClass().getName() + " created!");
    }

    /**
     * Метод считывающий значения ячеек оперативной памяти из файла
     * @param filename - file's name with values memory cells
     * @return initialization memory cells
     */
    public static char[] memoryReadFromFile(String filename) {
        log.info("(IOBMO)Memory initialization from file ' " + filename + " '");

        Scanner in = null;
        try {
            in = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            log.error("(IOBMO)Error initialization scanner for read values from file");
            throw new RuntimeException("(IOBMO)Error initialization scanner for read values from file");
        }

        List<String> memory = new ArrayList<String>();

        while (in.hasNextLine()) {
            memory.add(in.nextLine());
        }
        log.debug("(IOBMO)Values from file read!");

        char[] memoryChars = new char[VMBMO.MEMORY_SIZE]; //init char memory
        BitBMO word = new BitBMO(8);

        for(int i = 0; i < memory.size(); i++) {
            word.setBitsFromString(memory.get(i));
            memoryChars[i] = word.toChar();
        }

        return memoryChars;
    }


}
