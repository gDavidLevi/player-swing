package ru.davidlevi.swing.lessons.files;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * Упорядочить результат вывода
 */
public class Homework5 {
    public static void main(String[] args) {
        File folder = new File("bin");

        // Набор будет хранить все найденные в папке расширения файлов
        Set<String> set = new HashSet<>();
        for (String name : folder.list()) {
            File file = new File(folder.getAbsolutePath() + "\\" + name);
            int indexOf = name.lastIndexOf(".");
            // Если это не папка и в имени файла есть точка (т.е. есть расширение файла)
            if (file.isFile() && indexOf != -1)
                // Добавляется расширение файла
                set.add(name.substring(indexOf + 1, name.length()).toLowerCase());
        }
        System.out.println("Статистика по расширениям файлов для папки " + folder.getAbsolutePath() + " :");

        // Можно было использовать итератор
        for (String extention : set) {
            // Для каждого расширения создаем фильтр для фильтрации файлов внутри папки
            FilenameFilter filter = new MyFilter(extention);
            // вывести статистику по каждому расширению
            System.out.println("." + extention + ": " + folder.listFiles(filter).length);
        }
    }
}
