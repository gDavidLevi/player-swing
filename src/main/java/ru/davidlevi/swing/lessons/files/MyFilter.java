package ru.davidlevi.swing.lessons.files;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Класс-фильтр по расширению файла
 */
public class MyFilter implements FilenameFilter {
    private String extension;

    /**
     * Конструктор
     *
     * @param extension String
     */
    public MyFilter(String extension) {
        this.extension = extension;
    }

    /**
     * Заканчивается ли файл на расширение?
     *
     * @param file File
     * @param name String
     * @return boolean
     */
    @Override
    public boolean accept(File file, String name) {
        return name.endsWith("." + extension);
    }
}
