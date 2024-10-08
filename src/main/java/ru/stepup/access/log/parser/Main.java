package ru.stepup.access.log.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws IOException {
        Statistics statistics = new Statistics();
        HashSet<Integer> resp = new HashSet<>();
        String path = "C:\\Users\\Компутер\\IdeaProjects\\AccessLogParser\\access.log";
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int totalLines = 0;
            int googleBotCount = 0;
            int yandexBotCount = 0;
            while ((line = reader.readLine()) != null) {
                LogEntry logEntry = new LogEntry(line);
                statistics.addEntry(logEntry);
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
            System.out.println(statistics.userAgentOSInfo());
            System.out.println(statistics.userAgentBrowserInfo());
            System.out.println("Среднее количество посещений сайта в час (не боты):" + statistics.calculateAverageRequestsPerHour());
            System.out.println("Среднее количество ошибочных запросов в час: " + statistics.calculateAverageErroneousRequestsPerHour());
            System.out.println("Пиковая посещаемость сайта за одну секунду: " + statistics.calculatePeakVisitsPerSecond());
            System.out.println("Список доменов: " + statistics.getDomainSet());
            System.out.println("Максимальная посещаемость одним пользователем: " + statistics.maxVisitsBySingleUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

