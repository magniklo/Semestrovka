import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmTester {

    private static class TestResult {
        int textLength;
        int patternLength;
        long timeNanos;
        int comparisons;
        int position;
        boolean found;

        public TestResult(int textLength, int patternLength, long timeNanos,
                          int comparisons, int position, boolean found) {
            this.textLength = textLength;
            this.patternLength = patternLength;
            this.timeNanos = timeNanos;
            this.comparisons = comparisons;
            this.position = position;
            this.found = found;
        }
    }

    public void runTests() {
        List<TestResult> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            TestData data = readTestFile(String.format("test_%04d.txt", i));
            if (data != null) {
                TestResult result = testBoyerMoore(data.text, data.pattern);
                results.add(result);
            }
        }

        saveResultsToFile(results, "results.txt");
    }

    private void saveResultsToFile(List<TestResult> results, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("длина_текста время сравнений");
            for (TestResult result : results) {
                writer.printf("%d %d %d%n",
                        result.textLength,
                        result.timeNanos,
                        result.comparisons);
            }
            System.out.println("Результаты сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private TestResult testBoyerMoore(String text, String pattern) {
        BoyerMooreSearch searcher = new BoyerMooreSearch(pattern);

        // !Измеряем время только алгоритма (без учета чтения файла)
        long startTime = System.nanoTime();
        int position = searcher.search(text);
        long endTime = System.nanoTime();

        return new TestResult(
                text.length(),
                pattern.length(),
                endTime - startTime,
                searcher.getComparisons(),
                position,
                position != -1
        );
    }


    private TestData readTestFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String pattern = reader.readLine();
            String text = reader.readLine();
            return new TestData(text, pattern);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла " + filename + ": " + e.getMessage());
            return null;
        }
    }


    private static class TestData {
        String text;
        String pattern;

        TestData(String text, String pattern) {
            this.text = text;
            this.pattern = pattern;
        }
    }

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        generator.generateFiles();

        AlgorithmTester tester = new AlgorithmTester();
        tester.runTests();
    }
}