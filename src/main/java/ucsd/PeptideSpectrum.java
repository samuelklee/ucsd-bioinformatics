package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideSpectrum {
    /**
     * Returns linear spectrum (represented as a list of amino masses) of a peptide.
     * @param peptide   peptide to consider
     * @return          linear spectrum
     */
    public static List<Integer> getLinearSpectrum(String peptide) throws IOException {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();

        List<Integer> prefixMasses = new ArrayList<>(Arrays.asList(0));
        for (int i = 1; i <= peptide.length(); i++) {
            String amino = peptide.substring(i - 1, i);
            prefixMasses.add(aminoMassTable.get(amino) + prefixMasses.get(i - 1));
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
     * Returns cyclic spectrum (represented as a list of amino masses) of a cyclic peptide.
     * @param peptide   peptide to consider
     * @return          cyclic spectrum
     */
    public static List<Integer> getCyclicSpectrum(String peptide) throws IOException {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();

        List<Integer> prefixMasses = new ArrayList<>(Arrays.asList(0));
        for (int i = 1; i <= peptide.length(); i++) {
            String amino = peptide.substring(i - 1, i);
            prefixMasses.add(aminoMassTable.get(amino) + prefixMasses.get(i - 1));
        }

        int peptideMass = prefixMasses.get(peptide.length());

        List<Integer> spectrum = new ArrayList<>(Arrays.asList(0));

        for (int i = 0; i < peptide.length() - 1; i++) {
            for (int j = i + 1; j < peptide.length(); j++) {
                spectrum.add(prefixMasses.get(j) - prefixMasses.get(i));
                if (j < peptide.length()) {
                    spectrum.add(peptideMass - (prefixMasses.get(j) - prefixMasses.get(i)));
                }
            }
        }
        spectrum.add(peptideMass);
        Collections.sort(spectrum);
        return spectrum;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String peptide = br.readLine();

        List<Integer> peptideSpectrum = getLinearSpectrum(peptide);
//        List<Integer> peptideSpectrum = getCyclicSpectrum(peptide);

        System.out.println(peptideSpectrum.stream().map(i -> i.toString()).collect(Collectors.joining(" ")));
    }
}
