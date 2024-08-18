package ru.stepup.access.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String path = "C:\\Users\\Компутер\\IdeaProjects\\AccessLogParser\\access.log";

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int totalLines = 0;
            int maxLength = 0;
            int minLength = 1024;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                int length = line.length();
                if (length > maxLength) {
                    maxLength = length;
                }
                if (length < minLength) {
                    minLength = length;
                }
                if (length > 1024) {
                    throw new RuntimeException("В файле существует строка длиннее 1024 символов.");
                }
            }
            System.out.println("Общее количество строк в файле: " + totalLines);
            System.out.println("Самая длинная строка: " + maxLength + " символов");
            System.out.println("Самая короткая строка: " + minLength + " символов");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}


