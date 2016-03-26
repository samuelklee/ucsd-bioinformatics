package ucsd.V;

import com.google.common.primitives.Doubles;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
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

public class SoftKMeansClustering {
    public static List<List<Double>> readPoints(BufferedReader br) {
        List<List<Double>> points = new ArrayList<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null) {
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

    public static double findUnnormalizedResponsibility(List<Double> point, List<Double> center, double beta) {
        return Math.exp(-beta * findDistance(point, center));
    }

    public static List<List<Double>> findResponsibilities(List<List<Double>> points, List<List<Double>> centers, int k, double beta) {
        List<List<Double>> responsibilities = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            responsibilities.add(new ArrayList<>());
            List<Double> center = centers.get(i);
            for (List<Double> point : points) {
                double responsibility = findUnnormalizedResponsibility(point, center, beta);
                responsibilities.get(i).add(responsibility);
            }
        }

        int n = points.size();
        for (int j = 0; j < n; j++) {
            final int point = j;
            double normalization = IntStream.range(0, k).mapToDouble(i -> responsibilities.get(i).get(point)).sum();
            IntStream.range(0, k).forEach(i -> responsibilities.get(i).set(point, responsibilities.get(i).get(point) / normalization));
        }

        return responsibilities;
    }

    public static List<Double> findWeightedPoint(List<Double> responsibilitiesPerPoint, List<List<Double>> points, int m) {
        List<Double> center = new ArrayList<>(Collections.nCopies(m, 0.));
        int n = points.size();
        for (int i = 0; i < n; i++) {
            List<Double> point = points.get(i);
            double responsibility = responsibilitiesPerPoint.get(i);
            double responsibilityTotal = responsibilitiesPerPoint.stream().mapToDouble(Double::doubleValue).sum();
            for (int j = 0; j < m; j++) {
                center.set(j, center.get(j) + point.get(j) * responsibility / responsibilityTotal);
            }
        }
        return center;
    }

    public static List<List<Double>> findCenters(List<List<Double>> responsibilities, List<List<Double>> points, int k, int m) {
        List<List<Double>> newCenters = new ArrayList<>(k);

        for (List<Double> responsibilitiesPerCluster : responsibilities) {
            newCenters.add(findWeightedPoint(responsibilitiesPerCluster, points, m));
        }
        return newCenters;
    }


    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int maxIterations = 100;
            String[] kmString = br.readLine().split(" ");
            int k = Integer.parseInt(kmString[0]);
            int m = Integer.parseInt(kmString[1]);

            double beta = Double.parseDouble(br.readLine());

            List<List<Double>> points = readPoints(br);

            List<List<Double>> centers = points.subList(0, k);
            int iteration = 0;
            while (iteration < maxIterations) {
                List<List<Double>> responsibilities = findResponsibilities(points, centers, k, beta);
                List<List<Double>> newCenters = findCenters(responsibilities, points, k, m);
                if (centers == newCenters) {
                    break;
                }
                centers = newCenters;
                iteration++;
            }

            return ConsoleCapturer.toString(centers.stream().map(c -> c.stream().map(d -> String.format("%.3f", d)).collect(Collectors.joining(" "))).collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/V/dataset_10933_7.txt");
        System.out.println(result);
    }
}
