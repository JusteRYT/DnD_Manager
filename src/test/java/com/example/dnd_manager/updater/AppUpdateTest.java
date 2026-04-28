package com.example.dnd_manager.updater;

import com.example.dnd_manager.updater.version.SemanticVersionComparator;

import com.example.dnd_manager.updater.port.VersionComparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppUpdateTest {

    private final VersionComparator comparator = new SemanticVersionComparator();

    @Test
    @DisplayName("Сравнение версий: удаленный сервер имеет версию новее")
    void shouldReturnTrueWhenRemoteIsNewer() {
        assertTrue(comparator.isRemoteNewer("1.0.3", "1.0.4"));
        assertTrue(comparator.isRemoteNewer("1.0.3", "1.1.0"));
        assertTrue(comparator.isRemoteNewer("1.0.3", "2.0.0"));
        assertTrue(comparator.isRemoteNewer("1.0.3", "1.0.10")); // Важный кейс: 10 > 3
    }

    @Test
    @DisplayName("Сравнение версий: версии равны или локальная новее")
    void shouldReturnFalseWhenRemoteIsOldereOrEqual() {
        assertFalse(comparator.isRemoteNewer("1.0.3", "1.0.3"));
        assertFalse(comparator.isRemoteNewer("1.0.3", "1.0.2"));
        assertFalse(comparator.isRemoteNewer("1.1.0", "1.0.9"));
    }

    @Test
    @DisplayName("Сравнение версий: поддержка префикса 'v'")
    void shouldHandleVersionPrefix() {
        assertTrue(comparator.isRemoteNewer("1.0.3", "v1.0.4"));
        assertFalse(comparator.isRemoteNewer("v1.0.5", "1.0.5"));
    }
}














