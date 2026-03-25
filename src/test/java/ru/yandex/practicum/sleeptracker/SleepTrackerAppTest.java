package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analisys.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private SleepingSession createSession(String start, String finish, SleepQuality SleepQuality) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return new SleepingSession(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(finish, formatter),
                SleepQuality
        );
    }

    @DisplayName("Подсчет общего числа сессий сна")
    @Test
    public void shouldBeReturnTotalCountOfSession() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD),
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD),
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL)
        );

        CountSessionCalculator calculator = new CountSessionCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(3, result.getValue());
    }

    @DisplayName("Максимальная продолжительность для сессии не пересекающей границы месяца")
    @Test
    public void shouldBeReturnMaxDurationInOneMonth() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD), // 300
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        MaxDurationCalculator calculator = new MaxDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(500L, result.getValue());
    }

    @DisplayName("Максимальная продолжительность для сессии пересекающей границы месяца")
    @Test
    public void shouldBeReturnMaxDurationForDifferentMonths() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 23:00", "01.04.26 07:21", SleepQuality.BAD), // 501
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        MaxDurationCalculator calculator = new MaxDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(501L, result.getValue());
    }

    @DisplayName("Максимальная продолжительность для сессии пересекающей границы года")
    @Test
    public void shouldBeReturnMaxDurationForDifferentYears() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("31.12.25 23:00", "01.01.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD), // 300
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        MaxDurationCalculator calculator = new MaxDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(500L, result.getValue());
    }

    @DisplayName("Минимальная продолжительность для сессии пересекающей границы месяца")
    @Test
    public void shouldBeReturnMinDurationForDifferentMonths() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD), // 300
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        MinDurationCalculator calculator = new MinDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(300L, result.getValue());
    }

    @DisplayName("Минимальная продолжительность для сессии не пересекающей границы месяца")
    @Test
    public void shouldBeReturnMinDurationForOneDay() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 18:00", "31.03.26 23:59", SleepQuality.BAD), // 359
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        MinDurationCalculator calculator = new MinDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(359L, result.getValue());
    }

    @DisplayName("Средняя продолжительность сессии")
    @Test
    public void shouldBeReturnAverageDuration() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD), // 500
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD), // 300
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL) // 400
        );

        AverageDurationCalculator calculator = new AverageDurationCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(400L, result.getValue());
    }

    @DisplayName("Количество сессий с плохим качеством сна")
    @Test
    public void shouldBeReturnTwoBadSession() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD),
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.BAD),
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.BAD)
        );

        CountBadQualitySession calculator = new CountBadQualitySession();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(2, result.getValue());
    }

    @DisplayName("Проверка отсутствия сессий с плохим качеством сна")
    @Test
    public void shouldBeReturnZeroBadSession() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD),
                createSession("31.03.26 23:00", "01.04.26 04:00", SleepQuality.NORMAL),
                createSession("01.04.26 23:00", "02.04.26 05:40", SleepQuality.NORMAL)
        );

        CountBadQualitySession calculator = new CountBadQualitySession();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(0, result.getValue());
    }

    @DisplayName("Проверка отсутствия бессонных ночей")
    @Test
    public void shouldBeReturnZeroSleeplessSession() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:00", "26.03.26 07:20", SleepQuality.GOOD),
                createSession("26.03.26 23:00", "27.04.26 04:00", SleepQuality.NORMAL),
                createSession("27.04.26 23:00", "28.04.26 05:40", SleepQuality.NORMAL)
        );

        SleeplessNightCalculator calculator = new SleeplessNightCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @DisplayName("Проверка расчета бессонных ночей с граничными значениями времени")
    @Test
    public void shouldBeReturnTwoSleeplessSession() {
        List<SleepingSession> sessions = Arrays.asList(
                // в списке только последняя сессия входит в интервал 00:00-06:00
                createSession("25.03.26 06:00", "25.03.26 07:00", SleepQuality.GOOD),
                createSession("25.03.26 22:00", "26.04.26 00:00", SleepQuality.NORMAL),
                createSession("27.04.26 00:00", "27.04.26 05:59", SleepQuality.NORMAL)
        );

        SleeplessNightCalculator calculator = new SleeplessNightCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(2L, result.getValue());
    }

    @DisplayName("Проверка расчета бессонных ночей на протяжении года")
    @Test
    public void shouldBeReturnSleeplessSessionForOneYear() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("01.01.26 06:00", "01.01.26 07:00", SleepQuality.GOOD),
                createSession("31.12.26 22:00", "31.12.26 00:00", SleepQuality.NORMAL)
        );

        SleeplessNightCalculator calculator = new SleeplessNightCalculator();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals(365L, result.getValue());
    }

    @DisplayName("Проверка определения хронотипа при условии равного количества сессий жаворонка и совы")
    @Test
    void shouldReturnDoveTypes() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:01", "26.03.26 09:01", SleepQuality.GOOD), // "Сова"
                createSession("26.03.26 21:59", "27.03.26 06:59", SleepQuality.NORMAL) // "Жаворонок"
        );

        ChronotypeAnalyzer calculator = new ChronotypeAnalyzer();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals("Голубь", result.getValue());
    }

    @DisplayName("Определение хронотипа по чаще встречающемуся хронотипу")
    @Test
    void shouldReturnMostRepeatedTypes() {
        List<SleepingSession> sessions = Arrays.asList(
                createSession("25.03.26 23:01", "26.03.26 09:01", SleepQuality.GOOD),
                createSession("26.03.26 21:59", "27.03.26 06:59", SleepQuality.NORMAL),
                createSession("27.03.26 21:00", "28.03.26 06:20", SleepQuality.GOOD),
                createSession("28.03.26 21:30", "29.03.26 04:00", SleepQuality.NORMAL)
        );

        ChronotypeAnalyzer calculator = new ChronotypeAnalyzer();
        SleepAnalysisResult result = calculator.apply(sessions);
        assertEquals("Жаворонок", result.getValue());
    }
}