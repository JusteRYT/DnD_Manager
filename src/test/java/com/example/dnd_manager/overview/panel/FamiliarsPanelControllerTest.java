package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarsPanelControllerTest {

    @Test
    void openFamiliar_delegatesToLauncherWithProvidedDependencies() {
        Character owner = new Character();
        owner.setName("Owner");
        Character familiar = new Character();
        familiar.setName("Familiar");
        SaveCharacterUseCase saveCharacterUseCase = new SaveCharacterUseCase(new NoopGateway());
        FakeLauncher launcher = new FakeLauncher();

        FamiliarsPanelController controller = new FamiliarsPanelController(
                null,
                owner,
                saveCharacterUseCase,
                launcher
        );

        AtomicBoolean onUpdateCalled = new AtomicBoolean(false);
        controller.openFamiliar(familiar, () -> onUpdateCalled.set(true));

        assertTrue(launcher.called);
        assertEquals(owner, launcher.owner);
        assertEquals(familiar, launcher.familiar);
        assertEquals(saveCharacterUseCase, launcher.saveCharacterUseCase);
        assertTrue(onUpdateCalled.get());
    }

    private static final class FakeLauncher implements FamiliarInfoDialogLauncher {
        private boolean called;
        private Character familiar;
        private Character owner;
        private SaveCharacterUseCase saveCharacterUseCase;

        @Override
        public void show(
                javafx.stage.Stage parentStage,
                Character familiar,
                Character owner,
                SaveCharacterUseCase saveCharacterUseCase,
                Runnable onAnyUpdate
        ) {
            this.called = true;
            this.familiar = familiar;
            this.owner = owner;
            this.saveCharacterUseCase = saveCharacterUseCase;
            onAnyUpdate.run();
        }
    }

    private static final class NoopGateway implements CharacterGateway {
        @Override
        public void saveCharacter(Character character) {
        }

        @Override
        public Optional<Character> loadCharacter(String name) {
            return Optional.empty();
        }

        @Override
        public List<String> listCharacterNames() {
            return List.of();
        }

        @Override
        public void deleteCharacter(Character character) {
        }
    }
}
