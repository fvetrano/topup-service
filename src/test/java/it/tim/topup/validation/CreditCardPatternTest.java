package it.tim.topup.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardPatternTest {

    @Test
    public void matches() {
        assertTrue(CreditCardPattern.AMEX.matches("3470000000000"));
    }

    @Test
    public void anyMatch() {
        assertTrue(CreditCardPattern.anyMatch("3470000000000"));
    }

    @Test
    public void getMatch() {
        assertEquals(CreditCardPattern.AMEX, CreditCardPattern.getMatch("3470000000000"));
    }

    @Test
    public void getNoMatch() {
        assertEquals(CreditCardPattern.NONE, CreditCardPattern.getMatch(""));
        assertEquals(CreditCardPattern.NONE, CreditCardPattern.getMatch(null));
    }

    @Test
    public void getCvvSize() {
        for(CreditCardPattern p : CreditCardPattern.values()){
            if(p == CreditCardPattern.AMEX)
                assertEquals(4, p.getCvvSize());
            else
                assertEquals(3, p.getCvvSize());
        }
    }
}