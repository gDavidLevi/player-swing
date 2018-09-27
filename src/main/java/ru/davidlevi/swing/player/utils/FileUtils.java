package ru.davidlevi.swing.player.utils;

import ru.davidlevi.swing.player.objects.Mp3File;

import java.io.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * Утилита для работы с файлами
 *
 * @author David Levi
 */
public class FileUtils {

  /**
   * Метод возвращает имя файла без расширения
   *
   * @param fileName String
   * @return String
   */
  public static String getFileNameWithoutExtension(String fileName) {
    File file = new File(fileName);
    int indexCh = file.getName().lastIndexOf('.');
    if (indexCh > 0 && indexCh <= file.getName().length() - 2) {
      return file.getName().substring(0, indexCh);
    }
    return "the file does not have a name";
  }

  /**
   * Метод возвращает расширение файла
   *
   * @param file File
   * @return String
   */
  public static String getFileExtension(File file) {
    String result = null;
    String name = file.getName();
    int indexCh = name.lastIndexOf('.');
    if (indexCh > 0 && indexCh < name.length() - 1) {
      result = name.substring(indexCh + 1).toLowerCase();
    }
    return result;
  }

  /**
   * Метод удаляет текущий файл-фильтр и установить новый переданный
   *
   * @param fileChooser JFileChooser
   * @param filter FileFilter
   */
  public static void addFileFilter(JFileChooser fileChooser, FileFilter filter) {
    fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
    fileChooser.setFileFilter(filter);
    // Удалить последнее имя открываемого/сохраняемого файла
    fileChooser.setSelectedFile(new File(""));
  }

  /**
   * Метод сохраняет плейлист в файл
   *
   * @param listModel DefaultListModel<Mp3File>
   * @param fileName String
   * @throws Exception Exception
   */
  public static void saveTo(DefaultListModel<Mp3File> listModel, String fileName) throws Exception {
    try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
      objectOutputStream.writeObject(listModel);
      objectOutputStream.flush();
    }
  }

  /**
   * Метод возвращает объект DefaultListModel<Mp3File> из файла
   *
   * @param fileName String
   * @return DefaultListModel<Mp3File>
   * @throws Exception Exception
   */
  public static DefaultListModel<Mp3File> loadFrom(String fileName) throws Exception {
    Object object;
    try (FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
      object = objectInputStream.readObject();
    }
    if (object instanceof DefaultListModel) {
      return (DefaultListModel<Mp3File>) object;
    } else {
      throw new RuntimeException("Это не плейлист");
    }
  }
}
