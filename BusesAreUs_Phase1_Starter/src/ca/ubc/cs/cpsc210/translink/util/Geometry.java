package ca.ubc.cs.cpsc210.translink.util;

/**
 * Compute relationships between points, lines, and rectangles represented by LatLon objects
 */
public class Geometry {
    /**
     * Return true if the point is inside of, or on the boundary of, the rectangle formed by northWest and southeast
     *
     * @param northWest the coordinate of the north west corner of the rectangle
     * @param southEast the coordinate of the south east corner of the rectangle
     * @param point     the point in question
     * @return true if the point is on the boundary or inside the rectangle
     */
    public static boolean rectangleContainsPoint(LatLon northWest, LatLon southEast, LatLon point) {
        if (northWest.getLatitude() >= point.getLatitude() &&
                point.getLatitude() >= southEast.getLatitude() &&
                (northWest.getLongitude() <= point.getLongitude() &&
                        point.getLongitude() <= southEast.getLongitude()))
            return true;
        return false;
    }

    /**
     * Return true if the rectangle intersects the line
     *
     * @param northWest the coordinate of the north west corner of the rectangle
     * @param southEast the coordinate of the south east corner of the rectangle
     * @param src       one end of the line in question
     * @param dst       the other end of the line in question
     * @return true if any point on the line is on the boundary or inside the rectangle
     */


    public static boolean rectangleIntersectsLine(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {

        if (Geometry.rectangleContainsPoint(northWest, southEast, src) ||
                Geometry.rectangleContainsPoint(northWest, southEast, dst))
            return true;

        if (src.getLatitude() == northWest.getLatitude() ||
                src.getLatitude() == southEast.getLatitude() ||
                dst.getLatitude() == northWest.getLatitude() ||
                dst.getLatitude() == southEast.getLatitude())
            return true;

        Geometry.containsSegment(northWest, southEast, src, dst);

        return false;
    }

    public static boolean containsSegment(LatLon northWest, LatLon southEast, LatLon src, LatLon dst) {
        double minX = northWest.getLongitude();
        double minY = northWest.getLatitude();
        double maxX = southEast.getLongitude();
        double maxY = southEast.getLatitude();
        double x1 = src.getLongitude();
        double y1 = src.getLatitude();
        double x2 = dst.getLongitude();
        double y2 = dst.getLatitude();

        if ((x1 <= minX && x2 <= minX) || (y1 <= minY && y2 <= minY) ||
                (x1 >= maxX && x2 >= maxX) || (y1 >= maxY && y2 >= maxY))
            return false;

        double m = (y2 - y1) / (x2 - x1);
        double y = m * (minX - x1) + y1;
        double x = (minY - y1) / m + x1;
        if (between(minY, maxY, y))
            return true;
        y = m * (maxX - x1) + y1;
        if (y > minY && y < maxY)
            return true;
        if (between(minX, maxX, x))
            return true;

        x = (maxY - y1) / m + x1;
        if (between(minX, maxX, x))
            return true;
        return false;
    }

    /**
     * A utility method that you might find helpful in implementing the two previous methods
     * Return true if x is >= lwb and <= upb
     *
     * @param lwb the lower boundary
     * @param upb the upper boundary
     * @param x   the value in question
     * @return true if x is >= lwb and <= upb
     */
    private static boolean between(double lwb, double upb, double x) {
        return lwb <= x && x <= upb;
    }
}
