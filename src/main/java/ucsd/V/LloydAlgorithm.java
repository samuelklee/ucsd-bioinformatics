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

public class LloydAlgorithm {
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

    public static int findIndexOfClosestCenter(List<Double> point, List<List<Double>> centers) {
        double minDistance = 10000000.;
        int index = 0;
        for (int i = 0; i < centers.size(); i++) {
            List<Double> center = centers.get(i);
            double proposedDistance = findDistance(center, point);
            if (proposedDistance < minDistance) {
                minDistance = proposedDistance;
                index = i;
            }
        }
        return index;
    }

    public static List<List<List<Double>>> findClusters(List<List<Double>> points, List<List<Double>> centers, int k) {
        List<List<List<Double>>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }
        for (List<Double> point : points) {
            int index = findIndexOfClosestCenter(point, centers);
            clusters.get(index).add(point);
        }
        return clusters;
    }

    public static List<Double> findCenterOfGravity(List<List<Double>> points, int m) {
        List<Double> center = new ArrayList<>(Collections.nCopies(m, 0.));
        int n = points.size();
        for (List<Double> point : points) {
            for (int i = 0; i < m; i++) {
                center.set(i, center.get(i) + point.get(i) / n);
            }
        }
        return center;
    }

    public static List<List<Double>> findNewCenters(List<List<Double>> points, List<List<Double>> centers, int k, int m) {
        List<List<Double>> newCenters = new ArrayList<>(k);
        List<List<List<Double>>> clusters = findClusters(points, centers, k);

        for (List<List<Double>> cluster : clusters) {
            newCenters.add(findCenterOfGravity(cluster, m));
        }
        return newCenters;
    }


    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int maxIterations = 1000;
            String[] kmString = br.readLine().split(" ");
            int k = Integer.parseInt(kmString[0]);
            int m = Integer.parseInt(kmString[1]);

            List<List<Double>> points = readPoints(br);

            List<List<Double>> centers = points.subList(0, k);
            int iteration = 0;
            while (iteration < maxIterations) {
                List<List<Double>> newCenters = findNewCenters(points, centers, k, m);

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
//        List<List<Double>> pointsQuiz = Arrays.asList(Doubles.asList(1, 3, -1), Doubles.asList(9, 8, 14), Doubles.asList(6, 2, 10), Doubles.asList(4, 3, 1));
//        System.out.println(findCenterOfGravity(pointsQuiz, 3));

        String result = doWork("src/test/resources/V/dataset_10928_3.txt");
        System.out.println(result);
    }
}
