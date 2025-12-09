import java.io.*;
import java.util.*;

public class TextMoodAnalyzer {

    private static final Map<String, String> emotionDictionary = new HashMap<>();
    private static final Map<String, Integer> emotionCount = new HashMap<>();

    public static void main(String[] args) {
        loadEmotionDictionary();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingresa la ruta del archivo de texto: ");
        String filePath = scanner.nextLine();

        List<String> words = readWordsFromFile(filePath);
        analyzeMood(words);
        showResults();
    }

    private static void loadEmotionDictionary() {
        // Emociones: alegría, tristeza, enojo, miedo
        String[][] entries = {
            {"feliz", "alegría"}, {"contento", "alegría"}, {"genial", "alegría"},
            {"triste", "tristeza"}, {"llorar", "tristeza"}, {"deprimido", "tristeza"},
            {"molesto", "enojo"}, {"enojado", "enojo"}, {"furioso", "enojo"},
            {"asustado", "miedo"}, {"nervioso", "miedo"}, {"preocupado", "miedo"}
        };

        for (String[] entry : entries) {
            emotionDictionary.put(entry[0], entry[1]);
        }

        // Inicializa los contadores
        for (String emotion : new HashSet<>(emotionDictionary.values())) {
            emotionCount.put(emotion, 0);
        }
    }

    private static List<String> readWordsFromFile(String filePath) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.toLowerCase().replaceAll("[^a-záéíóúñü\\s]", "").split("\\s+");
                words.addAll(Arrays.asList(lineWords));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return words;
    }

    private static void analyzeMood(List<String> words) {
        for (String word : words) {
            String emotion = emotionDictionary.get(word);
            if (emotion != null) {
                emotionCount.put(emotion, emotionCount.get(emotion) + 1);
            }
        }
    }

    private static void showResults() {
        System.out.println("\n--- Resultado del Análisis de Emociones ---");
        emotionCount.forEach((emotion, count) -> {
            System.out.println("• " + emotion + ": " + count + " palabra(s)");
        });

        Optional<Map.Entry<String, Integer>> dominant = emotionCount.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue());

        if (dominant.isPresent() && dominant.get().getValue() > 0) {
            System.out.println("\n➡️ Emoción predominante: " + dominant.get().getKey().toUpperCase());
        } else {
            System.out.println("\nNo se detectó ninguna emoción predominante.");
        }
    }
}
