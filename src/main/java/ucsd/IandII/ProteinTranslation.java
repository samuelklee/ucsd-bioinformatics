package ucsd.IandII;


import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class ProteinTranslation {
    /**
     * Returns peptide (represented as String of aminos) encoded by RNA sequence.
     * @param rna   sequence to consider
     * @return      peptide encoded by sequence
     */
    public static String translate(String rna) throws IOException {
        Map<String, String> codonToAminoTable = DataTableUtils.getCodonToAminoTable();
        StringBuilder protein = new StringBuilder();
        for (int i = 0; i < rna.length(); i += 3) {
            String codon = rna.substring(i, i + 3);
            String amino = codonToAminoTable.get(codon);
            if (amino.equals("Stop")) {
                break;
            }
            protein.append(amino);
        }
        return protein.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String rna = br.readLine();
        String protein = translate(rna);

        System.out.println(protein);
    }
}
