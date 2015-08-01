package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class CyclopeptideSequencing {
    /**
     * Returns linear spectrum (masses of all possible subpeptides) of a peptide.
     * @param peptide   peptide to consider
     * @return          linear spectrum
     */
    public static List<Integer> getLinearSpectrum(List<Integer> peptide) {
        List<Integer> prefixMasses = new ArrayList<>(Arrays.asList(0));
        for (int i = 1; i <= peptide.size(); i++) {
            int aminoMass = peptide.get(i - 1);
            prefixMasses.add(aminoMass + prefixMasses.get(i - 1));
        }

        List<Integer> spectrum = new ArrayList<>(Arrays.asList(0));

        for (int i = 0; i < prefixMasses.size() - 1; i++) {
            for (int j = i + 1; j < prefixMasses.size(); j++) {
                spectrum.add(prefixMasses.get(j) - prefixMasses.get(i));
            }
        }

        Collections.sort(spectrum);
        return spectrum;
    }

    /**
     * Returns cyclic spectrum (masses of all possible subpeptides) of a cyclic peptide.
     * @param peptide   peptide to consider
     * @return          cyclic spectrum
     */
    public static List<Integer> getCyclicSpectrum(List<Integer> peptide) {
        List<Integer> prefixMasses = new ArrayList<>(Arrays.asList(0));
        for (int i = 1; i <= peptide.size(); i++) {
            int aminoMass = peptide.get(i - 1);
            prefixMasses.add(aminoMass + prefixMasses.get(i - 1));
        }

        int peptideMass = prefixMasses.get(peptide.size());

        List<Integer> spectrum = new ArrayList<>(Arrays.asList(0));

        for (int i = 0; i < peptide.size() - 1; i++) {
            for (int j = i + 1; j < peptide.size(); j++) {
                spectrum.add(prefixMasses.get(j) - prefixMasses.get(i));
                if (j < peptide.size()) {
                    spectrum.add(peptideMass - (prefixMasses.get(j) - prefixMasses.get(i)));
                }
            }
        }
        spectrum.add(peptideMass);
        Collections.sort(spectrum);
        return spectrum;
    }

    /**
     * Helper function to expand list of peptides by appending all amino masses in spectrum in turn to elements.
     * @param peptides              current list of peptides
     * @param aminoMassesInSpectrum amino masses present in spectrum
     * @return                      expanded list of peptides
     */
    public static List<List<Integer>> expand(List<List<Integer>> peptides, List<Integer> aminoMassesInSpectrum) {
        List<List<Integer>> expandedPeptides = new ArrayList<>();
        for (List<Integer> peptide : peptides) {
            for (int aminoMass : aminoMassesInSpectrum) {
                List<Integer> newPeptide = new ArrayList<>(peptide);
                newPeptide.add(aminoMass);
                expandedPeptides.add(newPeptide);
            }
        }
        return expandedPeptides;
    }

    /**
     * Returns possible peptides given a cyclic spectrum.
     * @param spectrum  cyclic spectrum to consider
     * @return          list of possible peptides (each represented by list of amino masses)
     */
    public static List<List<Integer>> getPeptides(List<Integer> spectrum) throws IOException {
        int parentMass = Collections.max(spectrum);

        List<List<Integer>> peptides = new ArrayList<>();
        List<List<Integer>> finalPeptides = new ArrayList<>();
        peptides.add(Arrays.asList());

        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicates();
        List<Integer> aminoMasses = new ArrayList<>(aminoMassTable.values());
        List<Integer> aminoMassesInSpectrum = new ArrayList<>();

        for (int aminoMass : aminoMasses) {
            if (spectrum.contains(aminoMass)) {
                aminoMassesInSpectrum.add(aminoMass);
            }
        }

        while (!peptides.isEmpty()) {
            peptides = expand(peptides, aminoMassesInSpectrum);

            for (Iterator<List<Integer>> iterator = peptides.iterator(); iterator.hasNext();) {
                List<Integer> peptide = iterator.next();
                if (peptide.stream().mapToInt(Integer::intValue).sum() == parentMass) {
                    if (getCyclicSpectrum(peptide).equals(spectrum)) {
                        finalPeptides.add(peptide);
                    }
                    iterator.remove();
                } else if (!spectrum.containsAll(getLinearSpectrum(peptide))) {
                    iterator.remove();
                }
            }
        }
        return finalPeptides;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] spectrumString = br.readLine().split("\\s+");
        List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        List<List<Integer>> peptides = getPeptides(spectrum);

        for (List<Integer> peptide : peptides) {
            System.out.print(peptide.stream().map(i -> i.toString()).collect(Collectors.joining("-")) + " ");
        }
        System.out.println();
    }
}
