package ru.davidlevi.swing.player.interfaces;

/**
 * Интерфейс для управления плеером
 *
 * @author David Levi
 */
public interface Player {

  void playerPlay(String fileName);

  void playerStop();

  void playerPause();

  void playerSetVolume(double value);

  void playerRewind(double rewindPosition);
}
