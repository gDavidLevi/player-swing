package ru.davidlevi.swing.lessons.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Сохранение в файл строки через буффер-записи
 */
public class Homework3 {
    private static final String STRING_FOR_WRITE = "У лукоморья дуб зеленый";

    // Точка входа
    public static void main(String[] args) {
        try {
            File file = new File("filename.txt");

            // Если файл не существует, то создать его
//            if (!file.exists())
//                file.createNewFile();

            FileWriter out = new FileWriter(file, true); // запись в файл с добавлением
            // Записывать в файл будем через буффер-записи
            BufferedWriter buffer = new BufferedWriter(out);
            buffer.write(STRING_FOR_WRITE);
            buffer.flush();
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
