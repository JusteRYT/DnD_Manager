package com.example.dnd_manager.updater;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resolves install directory based on current running jar location.
 */
@Slf4j
public class JarApplicationDirectoryResolver implements ApplicationDirectoryResolver {

    @Override
    public Path resolveInstallDirectory() {
        try {
            Path jarPath = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            Path jarDirectory = jarPath.getParent();

            if (jarDirectory != null && jarDirectory.getParent() != null) {
                if ("app".equalsIgnoreCase(jarDirectory.getFileName().toString())) {
                    return jarDirectory.getParent();
                }
            }
            return jarDirectory != null ? jarDirectory : Paths.get(System.getProperty("user.dir"));
        } catch (Exception e) {
            log.error("Failed to resolve JAR directory dynamically. Falling back to user.dir", e);
            return Paths.get(System.getProperty("user.dir"));
        }
    }
}

