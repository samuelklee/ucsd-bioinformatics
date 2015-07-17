package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountingPeptides {
    /**
     * Returns number of peptides possible given total mass.  Uses recursion.
     * @param cache                 for memoization
     * @param aminoMasses           list of amino masses
     * @param numberOfAminosToUse   parameter indicating number of aminos to try to use in current solution
     * @param mass                  total mass
     * @return                      number of peptides
     */
    public static long getNumber(long[][] cache, List<Integer> aminoMasses,
                                 int numberOfAminosToUse, int mass) throws IOException {
        if (mass < 0) {
            return 0;
        }

        if (numberOfAminosToUse < 0 && mass > 0) {
            return 0;
        }

        if (cache[numberOfAminosToUse][mass] != 0) {
            return cache[numberOfAminosToUse][mass];
        }

        if (mass == 0) {
            cache[numberOfAminosToUse][mass] = 1;
            return 1;
        }

        long total = 0;
        for (int i = 0; i < numberOfAminosToUse; i++) {
            total += getNumber(cache, aminoMasses, numberOfAminosToUse, mass - aminoMasses.get(i));
        }
        cache[numberOfAminosToUse][mass] = total;
        return total;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicates();
        List<Integer> aminoMasses = new ArrayList<>(aminoMassTable.values());
        int numberOfAminos = aminoMasses.size();

        int mass = Integer.parseInt(br.readLine());

        long[][] cache = new long[numberOfAminos + 1][mass + 1];

        long numberOfPeptides = getNumber(cache, aminoMasses, numberOfAminos, mass);

        System.out.println(numberOfPeptides);
    }
}
