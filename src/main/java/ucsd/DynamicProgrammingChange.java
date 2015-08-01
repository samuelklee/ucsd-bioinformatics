package ucsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicProgrammingChange {
    private static final int LARGE_NUMBER = 100000;

    /**
     * Returns the minimum number of coins needed to make change.
     * @param money amount of money to change
     * @param coins list of coin denominations
     * @return      minimum number of coins
     */
    public static int getMinimumNumberOfCoins(int money, List<Integer> coins) {
        List<Integer> minimumNumberOfCoins = new ArrayList<>(money + 1);
        minimumNumberOfCoins.add(0);
        for (int m = 1; m <= money; m++) {
            minimumNumberOfCoins.add(LARGE_NUMBER);
            for (int coin : coins) {
                if (m >= coin) {
                    int numberOfCoins = minimumNumberOfCoins.get(m - coin) + 1;
                    if (numberOfCoins < minimumNumberOfCoins.get(m)) {
                        minimumNumberOfCoins.set(m, numberOfCoins);
                    }
                }
            }
        }
        return minimumNumberOfCoins.get(money);
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            int money = Integer.parseInt(br.readLine());
            String[] inputIntegerStrings = br.readLine().split(",");

            List<Integer> coins = Arrays.asList(inputIntegerStrings).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            int minimumNumberOfCoins = getMinimumNumberOfCoins(money, coins);

            return ConsoleCapturer.toString(minimumNumberOfCoins);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_243_9.txt");
        System.out.println(result);
    }
}
