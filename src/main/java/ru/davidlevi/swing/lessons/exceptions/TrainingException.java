package ru.davidlevi.swing.lessons.exceptions;

/**
 * Наше исключение
 */
class TrainingException extends Exception /*ArithmeticException*/ {
    public TrainingException() {
        super();
    }

    public TrainingException(String s) {
        super(s);
    }
}