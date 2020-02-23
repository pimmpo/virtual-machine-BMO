package com.pimmpo.test;

import com.pimmpo.core.VMBMO;

public class TestCase {
    public static void main(String[] args) throws Exception {
        VMBMO vm = new VMBMO("memory.txt");
        vm.run();
        System.out.println("WORK!");
    }
}
