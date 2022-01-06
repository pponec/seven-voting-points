package net.ponec.demo.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ButtonModelTest {

    @Test
    void buttonClick() {
        ButtonModel model = ButtonModel.of(0, 3);
        int result = model.getStatus();
        int expected = 2;
        assertEquals(expected, result);

        model.buttonClick(2);
        result = model.getStatus();
        expected = 3;
        assertEquals(expected, result);

        model.buttonClick(0);
        result = model.getStatus();
        expected = -2;
        assertEquals(expected, result);

        model.buttonClick(1);
        result = model.getStatus();
        expected = -3;
        assertEquals(expected, result);

        model.buttonClick(0);
        result = model.getStatus();
        expected = 0;
        assertEquals(expected, result);
    }

    @Test
    void getStatus() {
    }
}