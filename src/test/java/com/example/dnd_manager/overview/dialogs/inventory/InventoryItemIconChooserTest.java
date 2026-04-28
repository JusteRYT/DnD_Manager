package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.service.GlobalAssetService;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryItemIconChooserTest {

    @Test
    void chooseItemIcon_returnsCurrentPathWhenSelectionCancelled() {
        FakeGlobalAssetService assetService = new FakeGlobalAssetService("Assets/Items/imported.png");
        ItemIconFilePicker picker = owner -> null;
        InventoryItemIconChooser chooser = new InventoryItemIconChooser(assetService, picker);

        String result = chooser.chooseItemIcon(null, "icon/current.png");

        assertEquals("icon/current.png", result);
        assertFalse(assetService.importCalled);
    }

    @Test
    void chooseItemIcon_returnsImportedPathWhenImportSucceeded() {
        FakeGlobalAssetService assetService = new FakeGlobalAssetService("Assets/Items/imported.png");
        File source = new File("source.png");
        ItemIconFilePicker picker = owner -> source;
        InventoryItemIconChooser chooser = new InventoryItemIconChooser(assetService, picker);

        String result = chooser.chooseItemIcon(null, "icon/current.png");

        assertEquals("Assets/Items/imported.png", result);
        assertTrue(assetService.importCalled);
        assertSame(source, assetService.lastFile);
        assertEquals(AssetCategory.ITEMS, assetService.lastCategory);
    }

    @Test
    void chooseItemIcon_returnsCurrentPathWhenImportFailed() {
        FakeGlobalAssetService assetService = new FakeGlobalAssetService(null);
        File source = new File("source.png");
        ItemIconFilePicker picker = owner -> source;
        InventoryItemIconChooser chooser = new InventoryItemIconChooser(assetService, picker);

        String result = chooser.chooseItemIcon(null, "icon/current.png");

        assertEquals("icon/current.png", result);
        assertTrue(assetService.importCalled);
    }

    private static final class FakeGlobalAssetService extends GlobalAssetService {

        private final String importResult;
        private boolean importCalled;
        private File lastFile;
        private AssetCategory lastCategory;

        private FakeGlobalAssetService(String importResult) {
            this.importResult = importResult;
        }

        @Override
        public String importAsset(File sourceFile, AssetCategory category) {
            this.importCalled = true;
            this.lastFile = sourceFile;
            this.lastCategory = category;
            return importResult;
        }
    }
}












