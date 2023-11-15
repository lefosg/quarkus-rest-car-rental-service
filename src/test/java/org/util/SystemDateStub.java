package org.util;

import org.util.SystemDate;

import java.time.LocalDate;

public class SystemDateStub {

    public static void setStub(LocalDate stub) {
        SystemDate.setStub(stub);
    }
    
    public static void reset() {
        SystemDate.removeStub();
    }
    
}