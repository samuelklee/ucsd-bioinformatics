package ucsd.V;

import com.google.common.primitives.Doubles;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SquaredErrorDistortion {
    public static List<List<Double>> readPoints(BufferedReader br) {
        List<List<Double>> points = new ArrayList<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null && !inputString.contains("-")) {
                points.add(Arrays.asList(inputString.split(" ")).stream().map(Double::parseDouble)
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return points;
    }

    public static double findDistance(List<Double> point1, List<Double> point2) {
        return new EuclideanDistance().compute(Doubles.toArray(point1), Doubles.toArray(point2));
    }

    public static double findDistanceToClosestCenter(List<Double> point, List<List<Double>> centers) {
        double minDistance = 10000000.;
        for (List<Double> center : centers) {
            minDistance = Math.min(findDistance(center, point), minDistance);
        }
        return minDistance;
    }

    public static double findDistortion(List<List<Double>> centers, List<List<Double>> points) {
        double totalSquaredDistance = 0.;
        for (List<Double> point : points) {
            double distance = findDistanceToClosestCenter(point, centers);
            totalSquaredDistance += distance * distance;
        }
        return totalSquaredDistance / points.size();
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            br.readLine();

            List<List<Double>> centers = readPoints(br);
            List<List<Double>> points = readPoints(br);

            double distortion = findDistortion(centers, points);
            return ConsoleCapturer.toString(String.format("%.3f", distortion));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
//        List<List<Double>> pointsQuiz = Arrays.asList(Doubles.asList(2, 6), Doubles.asList(4, 9), Doubles.asList(5, 7), Doubles.asList(6, 5), Doubles.asList(8, 3));
//        List<List<Double>> centersQuiz = Arrays.asList(Doubles.asList(4, 5), Doubles.asList(7, 4));
//        System.out.println(findDistortion(centersQuiz, pointsQuiz));

        String result = doWork("src/test/resources/V/dataset_10927_3.txt");
        System.out.println(result);
    }
}
