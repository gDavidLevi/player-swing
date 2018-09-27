package ru.davidlevi.swing.lessons.collections.homework1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Массив
 */
public class Array {
    /**
     * Печать массива
     *
     * @param array Object[]
     */
    private static void printArray(Object[] array) {
        for (Object object : array)
            System.out.print(object + " ");
    }

    /**
     * Вычисляем сумму сумму
     *
     * @param array Integer[]
     * @return int
     */
    private static int calcSum(Integer[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++)
            result = result + array[i];
        return result;
    }

    /**
     * Вычисление среднего значения
     *
     * @param array Integer[]
     * @param sum   int
     * @return double
     */
    private static double calcAvg(Integer[] array, int sum) {
        return sum / array.length;
    }

    /**
     * Метод отображает четные элементы
     *
     * @param array Integer[]
     */
    private static void showEven(Integer[] array) {
        for (int i = 0; i < array.length; i++)
            if (array[i] % 2 == 0)
                System.out.print(array[i] + " ");
    }

    /**
     * Метод отображает нечетные элементы
     *
     * @param array Integer[]
     */
    private static void showOdd(Integer[] array) {
        for (int i = 0; i < array.length; i++)
            if (array[i] % 2 != 0)
                System.out.print(array[i] + " ");
    }

    // Точка входа
    public static void main(String[] args) {
        System.out.print("Введите размер массива: ");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        scanner.close();
        Integer[] array = new Integer[size];
        System.out.println("Введите поочереди элементы массива (размер массива равен " + size + "):");
        for (int i = 0; i < size; i++)
            array[i] = scanner.nextInt();
        int sum = calcSum(array);
        System.out.println("--------------");
        System.out.println("Сумма: " + sum);
        System.out.println("Среднее значение: " + calcAvg(array, sum));
        Integer[] tmpArray = new Integer[size]; // массив для сортировки, чтобы не трогать первоначальный
        System.arraycopy(array, 0, tmpArray, 0, array.length); // копирование массива
        System.out.print("Сортировка по возрастанию: ");
        Arrays.sort(tmpArray);
        printArray(tmpArray);
        System.out.println();
        System.out.print("Сортировка по убыванию: ");
        Arrays.sort(tmpArray, Collections.reverseOrder());
        printArray(tmpArray);
        System.out.println();
        System.out.print("Четные:");
        showEven(array);
        System.out.println();
        System.out.print("Нечетные:");
        showOdd(array);
        System.out.println();
        System.out.println("--------------");
    }
}
