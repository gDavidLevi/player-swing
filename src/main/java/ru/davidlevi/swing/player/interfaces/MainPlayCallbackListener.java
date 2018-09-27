package ru.davidlevi.swing.player.interfaces;

/**
 * Callback-интерфес для отправки сообщений из класса MainPlayer в MainFrame
 *
 * @author David Levi
 */
public interface MainPlayCallbackListener {

    void callbackPlayStarted(String name);

    void callbackProcessScroll(int position);

    void callbackPlayFinished();
}
