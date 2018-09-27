package ru.davidlevi.swing.lessons.collections.homework3;

import java.util.Scanner;

/**
 * Вывод треугольников
 */
public class Triangle {
    private static String symbol;
    private static int number;
    private static String[][] triangle;

    /**
     * Заполняет весь массив символом
     *
     * @param symbol String
     */
    private static void fillTriangleBySymbol(String symbol) {
        for (int i = 0; i < number; i++)
            for (int j = 0; j < number; j++)
                triangle[i][j] = symbol;
    }

    private static void createPatternA() {
        fillTriangleBySymbol(symbol); // заполняем весь массив символом
        // Затем заполняем необходимые ячейки пробелами
        for (int i = 0; i < triangle.length; i++)
            for (int j = 0; j < i; j++)   // (j < i) - условие для построения треугольника
                triangle[j][i] = " ";
    }

    private static void createPatternB() {
        fillTriangleBySymbol(" "); // заполняем весь массив пробелами
        // Затем заполняем необходимые ячейки символом
        for (int j = 0; j < triangle.length; j++)
            for (int i = 0; i < triangle.length - j; i++)
                triangle[i][j] = symbol;
    }

    private static void createPatternC() {
        fillTriangleBySymbol(symbol); // заполняем весь массив символом
        // Затем заполняем необходимые ячейки пробелами
        for (int j = 0; j < triangle.length; j++)
            for (int i = 0; i < triangle.length - j - 1; i++)
                triangle[j][i] = " ";
    }

    private static void createPatternD() {
        fillTriangleBySymbol(symbol); // заполняем весь массив символами
        // Затем заполняем необходимые ячейки пробелами
        for (int i = 0; i < triangle.length; i++)
            for (int j = 0; j < i; j++)  // j < i - условие для построения треугольника
                triangle[i][j] = " "; // как в паттерне А только i j поменялись местами
    }

    /**
     *
     * @return String
     */
    private static String getTriangle() {
        System.out.println();
        String output = "";
        for (int i = 0; i < triangle.length; i++) {
            for (int j = 0; j < triangle.length; j++)
                output += triangle[i][j];
            output += "\n";
        }
        return output; // конечный треугольник записывается в эту переменную
    }

    // Точка входа
    public static void main(String[] args) {
        // Считываем количество элементов
        System.out.print("Введите количество элементов: ");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        System.out.println();

        // Считываем символ
        System.out.print("Введите символ для отображения: ");
        String input = scanner.next();
        scanner.close();

        symbol = input;
        number = size;
        triangle = new String[size][size];
        System.out.println();

        createPatternA();
        System.out.println("Pattern A");
        System.out.println(getTriangle());

        createPatternB();
        System.out.println("Pattern B");
        System.out.println(getTriangle());

        createPatternC();
        System.out.println("Pattern C");
        System.out.println(getTriangle());

        createPatternD();
        System.out.println("Pattern D");
        System.out.println(getTriangle());
    }
}
