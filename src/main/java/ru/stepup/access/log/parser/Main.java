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
            int googleBotCount = 0;
            int yandexBotCount = 0;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                int length = line.length();
                if (length > 1024) {
                    throw new RuntimeException("В файле существует строка длиннее 1024 символов.");
                }
                String[] parts = line.split("\\(");
                if (parts.length >= 2) {
                    String fragment = parts[1];
                    String[] fragments = fragment.split(";");
                    if (fragments.length >= 2) {
                        String userAgent = fragments[1].trim();
                        String botName = userAgent.split("/")[0];
                        if (botName.equals("Googlebot")) {
                            googleBotCount++;
                        } else if (botName.equals("YandexBot")) {
                            yandexBotCount++;
                        }
                    }
                }
            }
            System.out.println("Общее количество строк в файле: " + totalLines);
            System.out.println("Количество запросов от Googlebot: " + googleBotCount);
            System.out.println("Количество запросов от YandexBot: " + yandexBotCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

