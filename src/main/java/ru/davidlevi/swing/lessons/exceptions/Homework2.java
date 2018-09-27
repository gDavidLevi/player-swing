package ru.davidlevi.swing.lessons.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Homework2 {
    /**
     * Метод деления двух чисел
     *
     * @param a int
     * @param b int
     * @return double
     * @throws TrainingException Exception
     */
    private static double divide(int a, int b) throws TrainingException {
        try {
            // Один вариант решения
            if (b == 0)
                throw new TrainingException("Argument can not be 0!");
            return a / b;
            // Второе решение
        } catch (ArithmeticException e) { // если будет ошибка деления на ноль (или любая ошибка ArithmeticException)
            // Так делать не рекомендуется,
            // но для решения дом.задания так можно сделать
            throw new TrainingException(e.getMessage());
        } catch (Exception e1) {
            Logger.getLogger(Exception.class.getName()).log(Level.SEVERE, null, e1);
        }
        return 0;
    }

    // Точка входа
    public static void main(String[] args) {
        try {
            System.out.println(divide(6, 0));
        } catch (TrainingException e) { // перехватываем TrainingException
            Logger.getLogger(TrainingException.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}