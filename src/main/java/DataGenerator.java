import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DataGenerator {

    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&()";
    private static final Random random = new Random();

    //создаем файлы с данными разного размера
    public void generateFiles() {
        int counterFiles = 100;

        for (int i = 0; i < counterFiles; i++) {
            int textSize = random.nextInt(100, 10001);
            int fragmentSize = random.nextInt(3, 20);

            String corpus = generateString(textSize);
            String fragment = generateString(fragmentSize);

            // С вероятностью 85% паттерн будет в тексте
            if (random.nextDouble() < 0.85) {
                int insertionPoint = random.nextInt(0, textSize - fragmentSize);
                fragment = corpus.substring(insertionPoint, insertionPoint + fragmentSize);
            }

            saver(corpus, fragment, i);
        }
    }

    public String generateString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private void saver(String mainText, String searchPattern, int fileSerial) {
        String outputName = String.format("test_%04d.txt", fileSerial);

        try (PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputName)))) {

            outputWriter.println(searchPattern);
            outputWriter.println(mainText);
            outputWriter.println(mainText.length());

        } catch (IOException e) {
            System.err.println("Не удалось записать файл " + outputName + ": " + e.getMessage());
        }
    }

}