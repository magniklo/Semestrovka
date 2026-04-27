public class BoyerMooreSearch {

    private static final int ALPHABET_SIZE = 256;

    private String pattern; // образец для поиска
    private int comparisons; // счётчик сравнений
    private long executionTime; // время выполнения в наносекундах

    public BoyerMooreSearch(String pattern) {
        this.pattern = pattern;
    }

    private int[] buildBadCharTable(String pattern) {
        int[] badChar = new int[ALPHABET_SIZE];
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            badChar[i] = -1;
        }
        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i;
        }
        return badChar;
    }

    private int[] buildGoodSuffixTable(String pattern) {
        int m = pattern.length();
        int[] goodSuffix = new int[m];
        int[] borderPos = new int[m + 1];

        for (int i = 0; i < m; i++) {
            goodSuffix[i] = m;
        }

        int i = m;
        int j = m + 1;
        borderPos[i] = j;

        while (i > 0) {
            while (j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (goodSuffix[j - 1] == m) {
                    goodSuffix[j - 1] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }

        j = borderPos[0];
        for (i = 0; i < m; i++) {
            if (goodSuffix[i] == m) {
                goodSuffix[i] = j;
            }
            if (i == j) {
                j = borderPos[j];
            }
        }
        return goodSuffix;
    }

    public int search(String text) {
        return search(text, this.pattern);
    }

    private int search(String text, String pattern) {
        comparisons = 0;

        if (pattern.isEmpty() || text.length() < pattern.length()) {
            return -1;
        }

        int[] badChar = buildBadCharTable(pattern);
        int[] goodSuffix = buildGoodSuffixTable(pattern);

        int n = text.length();
        int m = pattern.length();
        int shift = 0;

        long startTime = System.nanoTime();

        while (shift <= (n - m)) {
            int j = m - 1;
            comparisons++;

            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
                comparisons++;
            }

            if (j < 0) {
                executionTime = System.nanoTime() - startTime;
                return shift;
            } else {
                char badCharCurrent = text.charAt(shift + j);
                int badCharShift = j - badChar[badCharCurrent];
                int goodSuffixShift = goodSuffix[j];
                shift += Math.max(1, Math.max(badCharShift, goodSuffixShift));
            }
        }

        executionTime = System.nanoTime() - startTime;
        return -1;
    }

    public int getComparisons() {
        return comparisons;
    }

}