package ru.davidlevi.swing.lessons.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Homework1 {
    public static void main(String[] args) {
        // Первый вариант решения
        try {
            System.out.println(1 / 0);
            
        } catch (RuntimeException e) {
            Logger.getLogger(RuntimeException.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception ex) {
            Logger.getLogger(Exception.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Error ex) {
            Logger.getLogger(Error.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Второй вариант решения
        try {
            System.out.println(1 / 0);
        } catch (Throwable throwable) {
            Logger.getLogger(Throwable.class.getName()).log(Level.SEVERE, null, throwable);
        } 
    }
}
