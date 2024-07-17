package ru.stepup.access.log.parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Введите первое число:");
        int firstValue = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int secondValue = new Scanner(System.in).nextInt();
        double sum = firstValue + secondValue;

        double difference = firstValue - secondValue;
        double multiplication = firstValue * secondValue;
        double split = firstValue / secondValue;
        System.out.println("Сумма: " + sum);
        System.out.println("Разность: " + difference);
        System.out.println("Произведение: " + multiplication);
        System.out.println("Частное: " + split);

    }

}