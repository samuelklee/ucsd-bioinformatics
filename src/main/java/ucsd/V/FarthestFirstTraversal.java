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

public class FarthestFirstTraversal {
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

    public static double findDistanceToClosestCenter(List<Double> point, List<List<Double>> centers) {
        double minDistance = 10000000.;
        for (List<Double> center : centers) {
            minDistance = Math.min(findDistance(center, point), minDistance);
        }
        return minDistance;
    }

    public static List<Double> findCenter(List<List<Double>> points, List<List<Double>> centers) {
        double maxDistance = 0;
        List<Double> center = points.get(0);
        for (List<Double> point : points) {
            double proposedDistance = findDistanceToClosestCenter(point, centers);
            if (proposedDistance > maxDistance) {
                maxDistance = proposedDistance;
                center = point;
            }
        }
        return center;
    }

    public static List<List<Double>> findCenters(List<List<Double>> points, int k) {
        List<List<Double>> centers = new ArrayList<>();

        while (centers.size() < k) {
            List<Double> center = findCenter(points, centers);
            centers.add(center);
        }
        return centers;
    }


    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int k = Integer.parseInt(br.readLine().split(" ")[0]);

            List<List<Double>> points = readPoints(br);

            List<List<Double>> centers = findCenters(points, k);
            return ConsoleCapturer.toString(centers.stream().map(c -> c.stream().map(Object::toString).collect(Collectors.joining(" "))).collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/V/dataset_10926_14.txt");
        System.out.println(result);
    }
}
