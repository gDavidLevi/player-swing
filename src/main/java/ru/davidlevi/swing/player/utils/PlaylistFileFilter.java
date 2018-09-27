package ru.davidlevi.swing.player.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Фильтр для компонента JFileChooser
 *
 * @author David Levi
 */

public class PlaylistFileFilter extends FileFilter {
    private final String fileExtension;
    private final String fileDescription;

    public PlaylistFileFilter(String fileExtension, String fileDescription) {
        this.fileExtension = fileExtension;
        this.fileDescription = fileDescription;
    }

    @Override
    public boolean accept(File file) {
        // Разрешить только папки, а также файлы с расширением mp3
        return file.isDirectory() || file.getAbsolutePath().endsWith(fileExtension);
    }

    @Override
    public String getDescription() {
        // Описание для формата mp3 при выборе в диалоговом окне
        return fileDescription + " (*." + fileExtension + ")";
    }
}
