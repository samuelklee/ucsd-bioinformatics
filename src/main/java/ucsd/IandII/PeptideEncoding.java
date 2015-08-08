package ucsd.IandII;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PeptideEncoding {
    /**
     * Returns list of patterns in a given sequence that can code for a given peptide (including reverse complement).
     * @param dna       sequence to consider
     * @param peptide   peptide to search for
     * @return          list of patterns encoding peptide
     */
    public static List<String> getPatterns(String dna, String peptide) throws IOException {
        List<String> patterns = new ArrayList<>();
        int patternLength = 3*peptide.length();
        for (int i = 0; i < dna.length() - patternLength; i++) {
            String pattern = dna.substring(i, i + patternLength);
            String rnaPatten = pattern.replace('T', 'U');
            String rnaReverseComplementPattern = ReverseComplement.getReverseComplement(pattern).replace('T', 'U');
            if (ProteinTranslation.translate(rnaPatten).equals(peptide) ||
                    ProteinTranslation.translate(rnaReverseComplementPattern).equals(peptide)) {

                patterns.add(pattern);
            }
        }
        return patterns;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String dna = br.readLine();
        String peptide = br.readLine();

        List<String> patternsEncodingPeptide = getPatterns(dna, peptide);

        System.out.println(patternsEncodingPeptide.stream().collect(Collectors.joining("\n")));
    }
}
