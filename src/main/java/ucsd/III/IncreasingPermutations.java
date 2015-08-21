package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IncreasingPermutations {
    public static int count = 0;
    public static final int n = 3;
    public static final String format = "%" + n + "s";

    public static boolean isIncreasing(List<Integer> permutation) {
        for (int i = 0; i < n - 1; i++) {
            final int first = permutation.get(i);
            final int second = permutation.get(i + 1);
            if ((first > 0 && second > 0 && first == second + 1) || (first < 0 && second < 0 && first == second - 1)) {
                return false;
            }
        }
        return true;
    }

    public static List<List<Integer>> getSigned(List<Integer> permutation) {
        final List<List<Integer>> signedPermutations = new ArrayList<>();

        for (int i = 0; i < Math.pow(2, n); i++) {
            String iBinary = String.format(format, Integer.toBinaryString(i)).replace(' ', '0');
            signedPermutations.add(IntStream.range(0, n).boxed()
                    .map(j -> permutation.get(j) * ((int) Math.signum(2 * Integer.parseInt(Character.toString(iBinary.charAt(j))) - 1)))
                    .collect(Collectors.toList()));
        }
        return signedPermutations;
    }

    public static void generate(int n, List<Integer> permutation) {
        if (n == 1) {
            final List<List<Integer>> signedPermutations = getSigned(permutation);
            for (List<Integer> signedPermutation : signedPermutations) {
                if (isIncreasing(signedPermutation)) {
                    System.out.println(signedPermutation);
                    count++;
                }
            }
        } else {
            for (int i = 0; i < n - 1; i ++) {
                generate(n - 1, permutation);
                if (n % 2 == 0) {
                    Collections.swap(permutation, i, n - 1);
                } else {
                    Collections.swap(permutation, 0, n - 1);
                }
            }
            generate(n - 1, permutation);
        }
    }

    public static void main(String[] args) throws IOException {
        final List<Integer> permutation = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
        generate(n, permutation);
        System.out.println(count);
    }
}
