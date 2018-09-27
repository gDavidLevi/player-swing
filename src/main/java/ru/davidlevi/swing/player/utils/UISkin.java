package ru.davidlevi.swing.player.utils;

import ru.davidlevi.swing.player.ui.MainFrame;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Класс UISkin изменяет скин приложения
 *
 * @author David Levi
 */
public class UISkin {
    /**
     * Метод устнанавливает Nimbus-скин UI
     */
    public static void setDefault() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Метод изменяет скин UI
     *
     * @param component Component
     * @param laf       LookAndFeel
     */
    public static void set(Component component, LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
        }
        SwingUtilities.updateComponentTreeUI(component);
    }

    /**
     * Метод изменяет скин UI
     *
     * @param component Component
     * @param laf       String
     */
    public static void set(Component component, String laf) {
        try {
            try {
                UIManager.setLookAndFeel(laf);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                Logger.getLogger(UISkin.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (UnsupportedLookAndFeelException e) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
        }
        SwingUtilities.updateComponentTreeUI(component);
    }
}
