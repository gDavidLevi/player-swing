package ru.davidlevi.swing.player.interfaces.implementation;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import ru.davidlevi.swing.player.interfaces.Playlist;
import ru.davidlevi.swing.player.interfaces.Player;
import ru.davidlevi.swing.player.objects.Mp3File;
import ru.davidlevi.swing.player.interfaces.adapter.JListDndAdapter;
import ru.davidlevi.swing.player.utils.FileUtils;

/**
 * Плейлист на основе компонента JList
 *
 * @author David Levi
 */
public class MainPlaylist implements Playlist {
    public static final String PLS_FILE_EXTENSION = "pls";
    public static final String PLS_FILE_DESCRIPTION = "Файлы плейлиста";
    private static final String EMPTY_STRING = "";
    //
    private Player player;
    // DefaultModel
    private JList<Mp3File> playlist;
    private DefaultListModel<Mp3File> listModel = new DefaultListModel<>();

    /**
     * Конструктор
     *
     * @param playlist JList
     * @param player   Player
     */
    public MainPlaylist(JList<Mp3File> playlist, Player player) {
        this.playlist = playlist;
        this.player = player;
        //
        initDragDrop();
        initPlayList();
    }

    /**
     * Инициализация Drag&Drop
     */
    private void initDragDrop() {
        try {
            DropTarget dropTarget = new DropTarget(playlist, DnDConstants.ACTION_COPY_OR_MOVE, null);
            dropTarget.addDropTargetListener(new JListDndAdapter(playlist));
        } catch (TooManyListenersException e) {
            Logger.getLogger(MainPlaylist.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void initPlayList() {
        playlist.setModel(listModel);
        playlist.setToolTipText("Список песен");
        // Слушатель на двойной клик по песне
        playlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == InputEvent.BUTTON1_MASK && e.getClickCount() == 2)
                    playlistPlayFile();
            }
        });
        // Слушатель на кнопку Enter по песне
        playlist.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    playlistPlayFile();
            }
        });
    }

    @Override
    public void playlistNext() {
        int i = playlist.getSelectedIndex() + 1;
        // Если не вышли за пределы плейлиста
        if (i <= listModel.getSize() - 1) {
            playlist.setSelectedIndex(i);
            playlistPlayFile();
        }
    }

    @Override
    public void playlistPrevious() {
        int i = playlist.getSelectedIndex() - 1;
        // Если не вышли за пределы плейлиста
        if (i >= 0) {
            playlist.setSelectedIndex(i);
            playlistPlayFile();
        }
    }

    @Override
    public boolean playlistSearch(String name) {
        // Если в поиске ничего не ввели, то выйти из метода и не производить поиск
        if (name == null || name.trim().equals(EMPTY_STRING))
            return false;
        // Все индексы объектов, найденных по поиску, будут храниться в коллекции
        ArrayList<Integer> listIndexes = new ArrayList<>();
        // Проходим по коллекции и ищем соответствия имен песен со строкой поиска
        for (int i = 0; i < listModel.getSize(); i++) {
            Mp3File file = listModel.getElementAt(i);
            // Поиск вхождения строки в название песни без учета регистра букв
            if (file.getName().toUpperCase().contains(name.toUpperCase()))
                listIndexes.add(i); // найденный индексы добавляем в коллекцию
        }
        // Коллекцию индексов сохраняем в массив
        int[] selectedIndexes = new int[listIndexes.size()];
        // Если не найдено ни одной песни, удовлетворяющей условию поиска
        if (selectedIndexes.length == 0)
            return false;
        // Преобразовать коллекцию в массив, т.к. метод для выделения строк в JList работает только с массивом
        for (int index = 0; index < selectedIndexes.length; index++)
            selectedIndexes[index] = listIndexes.get(index);
        // Выделить в плелисте найдные песни по массиву индексов, найденных ранее
        playlist.setSelectedIndices(selectedIndexes);
        return true;
    }

    @Override
    public void playlistSaveList(File toFile) {
        try {
            String fileExtension = FileUtils.getFileExtension(toFile);
            // Имя файла (нужно ли добавлять раширение к имени файлу при сохранении)
            String result;
            if (fileExtension != null && fileExtension.equals(PLS_FILE_EXTENSION))
                result = toFile.getPath();
            else
                result = toFile.getPath() + "." + PLS_FILE_EXTENSION;
            FileUtils.saveTo(listModel, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playlistOpenFiles(File[] arrayFiles) {
        for (File file : arrayFiles) {
            Mp3File element = new Mp3File(file.getName(), file.getPath());
            // Если эта песня уже есть в списке, то не добавлять ее
            if (!listModel.contains(element))
                listModel.addElement(element);
        }
    }

    @Override
    public void playlistOpenList(File file) {
        try {
            DefaultListModel<Mp3File> load = FileUtils.loadFrom(file.getPath());
            this.listModel = load;
            playlist.setModel(load);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playlistPlayFile() {
        // Получаем выбранные индексы(порядковый номер) песен
        int[] selectedIndexes = playlist.getSelectedIndices();
        // Если выбрали хотя бы одну песню
        if (selectedIndexes.length > 0) {
            Mp3File mp3File = listModel.getElementAt(selectedIndexes[0]);
            if (mp3File == null) return;
            player.playerPlay(mp3File.getPath());
        }
    }

    @Override
    public void playlistDelete() {
        // Получаем выбранные индексы (порядковый номер) песни
        int[] selectedIndexes = playlist.getSelectedIndices();
        // Если выбрали хотя бы одну песню
        if (selectedIndexes.length > 0) {
            // Создадим список удаления
            ArrayList<Mp3File> files = new ArrayList<>();
            for (int index : selectedIndexes) {
                Mp3File mp3File = listModel.getElementAt(index);
                files.add(mp3File);
            }
            // Удаление
            for (Mp3File file : files)
                listModel.removeElement(file);
        }
    }

    @Override
    public void playlistClear() {
        listModel.clear();
    }
}
