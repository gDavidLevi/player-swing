package ru.davidlevi.swing.player.interfaces;

import java.io.File;

/**
 * Интерфейс управления плейлистом
 *
 * @author David Levi
 */
public interface Playlist {

  void playlistNext();

  void playlistPrevious();

  void playlistDelete();

  void playlistClear();

  boolean playlistSearch(String name);

  void playlistSaveList(File file);

  void playlistOpenFiles(File[] files);

  void playlistOpenList(File file);

  void playlistPlayFile();
}
