package com.example.dnd_manager.updater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MegabytesProgressTextFormatterTest {

    @Test
    void format_returnsMegabytesMessage() {
        MegabytesProgressTextFormatter formatter = new MegabytesProgressTextFormatter();

        String text = formatter.format(1572864L, 3145728L);

        assertTrue(
                text.equals("Downloading: 1.50 MB / 3.00 MB")
                        || text.equals("Downloading: 1,50 MB / 3,00 MB")
        );
    }
}
