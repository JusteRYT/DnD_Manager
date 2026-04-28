package com.example.dnd_manager.screen;

import com.example.dnd_manager.screen.assets.AssetManagerController;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetManagerControllerTest {

    @Test
    void managerMode_handleExit_runsBackAction() {
        AtomicBoolean backCalled = new AtomicBoolean(false);
        AssetManagerController controller = new AssetManagerController(false, () -> backCalled.set(true));

        controller.handleExit(null);

        assertTrue(backCalled.get());
    }

    @Test
    void pickerMode_andManagerMode_resolveDifferentLabels() {
        AssetManagerController managerController = new AssetManagerController(false, () -> {});
        AssetManagerController pickerController = new AssetManagerController(true, () -> {});

        String managerTitle = managerController.resolveTitle();
        String pickerTitle = pickerController.resolveTitle();
        String managerExit = managerController.resolveExitButtonLabel();
        String pickerExit = pickerController.resolveExitButtonLabel();

        assertFalse(managerTitle.isBlank());
        assertFalse(pickerTitle.isBlank());
        assertFalse(managerExit.isBlank());
        assertFalse(pickerExit.isBlank());
        assertNotEquals(managerTitle, pickerTitle);
        assertNotEquals(managerExit, pickerExit);
    }
}














