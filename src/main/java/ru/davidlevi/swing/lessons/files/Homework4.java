package ru.davidlevi.swing.lessons.files;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Фильтр
 */
public class Homework4 {
    public static void main(String[] args) {
        File folder = new File("MyFolder");
        FilenameFilter filter = new MyFilter("txt"); // с расширением txt
        System.out.println("Файлы с расширением *.txt:");
        for (String file : folder.list(filter)) {
            System.out.println(file);
        }
    }
}


