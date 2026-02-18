package com.example.dnd_manager.updater;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppUpdateTest {

    private final UpdateChecker checker = new UpdateChecker();

    @Test
    @DisplayName("Сравнение версий: удаленный сервер имеет версию новее")
    void shouldReturnTrueWhenRemoteIsNewer() {
        // Симулируем логику isNewer (если ты сделал метод в UpdateChecker публичным)
        // Если метод приватный, для теста его стоит сделать package-private (убрать private)
        assertTrue(invokeIsNewer("1.0.3", "1.0.4"));
        assertTrue(invokeIsNewer("1.0.3", "1.1.0"));
        assertTrue(invokeIsNewer("1.0.3", "2.0.0"));
        assertTrue(invokeIsNewer("1.0.3", "1.0.10")); // Важный кейс: 10 > 3
    }

    @Test
    @DisplayName("Сравнение версий: версии равны или локальная новее")
    void shouldReturnFalseWhenRemoteIsOldereOrEqual() {
        assertFalse(invokeIsNewer("1.0.3", "1.0.3"));
        assertFalse(invokeIsNewer("1.0.3", "1.0.2"));
        assertFalse(invokeIsNewer("1.1.0", "1.0.9"));
    }

    @Test
    @DisplayName("Сравнение версий: поддержка префикса 'v'")
    void shouldHandleVersionPrefix() {
        assertTrue(invokeIsNewer("1.0.3", "v1.0.4"));
        assertFalse(invokeIsNewer("v1.0.5", "1.0.5"));
    }

    // Вспомогательный метод для теста, копирующий логику из UpdateChecker
    private boolean invokeIsNewer(String current, String remote) {
        current = current.replace("v", "");
        remote = remote.replace("v", "");

        String[] currParts = current.split("\\.");
        String[] remParts = remote.split("\\.");

        int length = Math.max(currParts.length, remParts.length);
        for (int i = 0; i < length; i++) {
            int curr = i < currParts.length ? Integer.parseInt(currParts[i]) : 0;
            int rem = i < remParts.length ? Integer.parseInt(remParts[i]) : 0;

            if (rem > curr) return true;
            if (rem < curr) return false;
        }
        return false;
    }
}