package ro.fmi.unibuc.licitatie_curieri.common.distancecalculator;

public class DistanceCalculator {
    private static final double SEARCH_RANGE = 10;
    private static final double EARTH_RADIUS = 6371;

    private DistanceCalculator() {}

    public static boolean isWithinRange(Double latitudeA, Double longitudeA, Double latitudeB, Double longitudeB) {
        return SEARCH_RANGE >= calculateDistance(latitudeA, longitudeA, latitudeB, longitudeB);
    }

    // Source https://www.baeldung.com/java-find-distance-between-points
    private static double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
