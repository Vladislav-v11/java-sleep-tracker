package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analisys.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {

    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYSIS_FUNCTIONS = List.of(
            new CountSessionCalculator(),
            new MinDurationCalculator(),
            new MaxDurationCalculator(),
            new AverageDurationCalculator(),
            new CountBadQualitySession(),
            new SleeplessNightCalculator(),
            new ChronotypeAnalyzer()
            );

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Не указан путь к файлу.");
            return;
        }

        String filePath = args[0];
        FileLoader fileLoader = new FileLoader();
        Optional<List<SleepingSession>> sessions = fileLoader.loadSessions(filePath);

        sessions.ifPresent(sess -> ANALYSIS_FUNCTIONS.stream()
                .map(analysis -> analysis.apply(sess))
                .forEach(result ->
                        System.out.println(result.getDescription() + ": " + result.getValue())
                ));
    }
}
