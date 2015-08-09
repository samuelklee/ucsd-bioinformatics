package ucsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataTableUtils {
    public static Map<String, String> getCodonToAminoTable() throws IOException {
        Map<String, String> rnaCodonTable = new HashMap<>();
        try (BufferedReader data = new BufferedReader(new FileReader("/home/slee/working/ucsd-bioinformatics/test/resources/RNACodonTable.txt"))) {
            for(String line = data.readLine(); line != null; line = data.readLine()) {
                String[] tokens = line.split("\\s+");
                for (int i = 0; i < tokens.length; i += 2) {
                    String codon = tokens[i];
                    String amino = tokens[i + 1];
                    rnaCodonTable.put(codon, amino);
                }
            }
        }
        return rnaCodonTable;
    }

    public static Map<String, Integer> getAminoCountsTable() throws IOException {
        Map<String, Integer> aminoCountsTable = new HashMap<>();
        try (BufferedReader data = new BufferedReader(new FileReader("/home/slee/working/ucsd-bioinformatics/test/resources/RNACodonTable.txt"))) {
            for(String line = data.readLine(); line != null; line = data.readLine()){
                String[] tokens = line.split("\\s+");
                for (int i = 0; i < tokens.length; i += 2) {
                    String amino = tokens[i + 1];
                    Integer currentCount = aminoCountsTable.getOrDefault(amino, 0);
                    aminoCountsTable.put(amino, currentCount + 1);
                }
            }
        }
        return aminoCountsTable;
    }

    public static Map<String, Integer> getAminoMassTable() throws IOException {
        Map<String, Integer> aminoMassTable = new HashMap<>();
        try (BufferedReader data = new BufferedReader(new FileReader("/home/slee/working/ucsd-bioinformatics/test/resources/AminoMassTable.txt"))) {
            for(String line = data.readLine(); line != null; line = data.readLine()) {
                String[] tokens = line.split("\\s+");
                String amino = tokens[0];
                int mass = Integer.parseInt(tokens[1]);
                aminoMassTable.put(amino, mass);
            }
        }
        return aminoMassTable;
    }

    public static Map<String, Integer> getAminoMassTableNoDuplicates() throws IOException {
        Map<String, Integer> aminoMassTable = new HashMap<>();
        try (BufferedReader data = new BufferedReader(new FileReader("/home/slee/working/ucsd-bioinformatics/test/resources/AminoMassTableNoDuplicates.txt"))) {
            for(String line = data.readLine(); line != null; line = data.readLine()) {
                String[] tokens = line.split("\\s+");
                String amino = tokens[0];
                int mass = Integer.parseInt(tokens[1]);
                aminoMassTable.put(amino, mass);
            }
        }
        return aminoMassTable;
    }

    public static Map<Map.Entry, Integer> getScoringMatrix(String fileName) throws IOException {
        Map<Map.Entry, Integer> scoringMatrix = new HashMap<>();
        try (BufferedReader data = new BufferedReader(new FileReader(fileName))) {
            String headerRow = data.readLine();
            List<String> aminoColumns = new ArrayList<>(Arrays.asList(headerRow.split("\\s+")));
            aminoColumns.remove(0);
            for(String line = data.readLine(); line != null; line = data.readLine()) {
                List<String> tokens = Arrays.asList(line.split("\\s+"));
                String aminoRow = tokens.get(0);
                for (int i = 1; i < tokens.size(); i++) {
                    int score = Integer.parseInt(tokens.get(i));
                    scoringMatrix.put(
                            new AbstractMap.SimpleEntry(aminoRow.charAt(0), aminoColumns.get(i - 1).charAt(0)),
                            score);
                }
            }
        }
        return scoringMatrix;
    }
}
