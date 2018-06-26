package it.tim.topup.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IdsGeneratorTest {

//    @Test
//    public void generateId10Distinct() {
//        List<String> strings = new ArrayList<>();
//        for(int i=0; i<100000; i++){
//            System.out.println("Iteration "+(i+1)+" - array size: "+strings.size());
//            String id = IdsGenerator.generateId10();
//            assertFalse(strings.contains(id));
//            strings.add(id);
//        }
//    }

    @Test
    public void generateId10() {
        assertEquals(10, IdsGenerator.generateId10().length());
    }

}