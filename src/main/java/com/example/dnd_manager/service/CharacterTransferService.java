package com.example.dnd_manager.service;


import java.io.File;
import java.io.IOException;

/**
 * Service responsible for importing and exporting character folders.
 */
public interface CharacterTransferService {

    /**
     * Exports character folder into zip archive.
     *
     * @param characterName name of character to export
     * @param targetZipFile target zip file
     * @throws IOException if export fails
     */
    void exportCharacter(String characterName, File targetZipFile) throws IOException;

    /**
     * Imports character from zip archive.
     *
     * @param zipFile source archive
     * @throws IOException if import fails
     */
    void importCharacter(File zipFile) throws IOException;
}
