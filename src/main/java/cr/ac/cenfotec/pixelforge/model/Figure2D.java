package cr.ac.cenfotec.pixelforge.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una figura bidimensional formada por varios puntos.
 *
 * Mantiene:
 * - Los puntos originales.
 * - Los puntos actuales después de aplicar transformaciones.
 */
public class Figure2D {

    private String name;

    private List<Point2D> originalPoints;

    private List<Point2D> currentPoints;

    public Figure2D() {
        this.originalPoints = new ArrayList<>();
        this.currentPoints = new ArrayList<>();
    }

    public Figure2D(String name, List<Point2D> points) {
        this.name = name;
        this.originalPoints = copyPoints(points);
        this.currentPoints = copyPoints(points);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Point2D> getOriginalPoints() {
        return originalPoints;
    }

    public void setOriginalPoints(List<Point2D> originalPoints) {
        this.originalPoints = copyPoints(originalPoints);
    }

    public List<Point2D> getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(List<Point2D> currentPoints) {
        this.currentPoints = copyPoints(currentPoints);
    }

    /**
     * Agrega un punto a la figura original y a la figura actual.
     */
    public void addPoint(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException(
                    "El punto no puede ser nulo."
            );
        }

        originalPoints.add(point.copy());
        currentPoints.add(point.copy());
    }

    /**
     * Devuelve la figura a sus coordenadas originales.
     */
    public void reset() {
        this.currentPoints = copyPoints(originalPoints);
    }

    /**
     * Devuelve las coordenadas originales
     * como una matriz homogénea.
     *
     * Cada columna representa un punto:
     *
     * | x1 x2 x3 ... |
     * | y1 y2 y3 ... |
     * | 1  1  1  ... |
     */
    public double[][] getOriginalMatrix() {
        return toHomogeneousMatrix(originalPoints);
    }

    /**
     * Devuelve las coordenadas actuales
     * como una matriz homogénea.
     */
    public double[][] getCurrentMatrix() {
        return toHomogeneousMatrix(currentPoints);
    }

    /**
     * Convierte una lista de puntos en una matriz 3 × n.
     */
    private double[][] toHomogeneousMatrix(
            List<Point2D> points
    ) {
        if (points == null || points.isEmpty()) {
            return new double[3][0];
        }

        double[][] matrix =
                new double[3][points.size()];

        for (
                int column = 0;
                column < points.size();
                column++
        ) {
            Point2D point = points.get(column);

            matrix[0][column] = point.getX();
            matrix[1][column] = point.getY();
            matrix[2][column] = 1;
        }

        return matrix;
    }

    /**
     * Crea una copia profunda de una lista de puntos.
     *
     * Esto evita que la figura original y la figura actual
     * compartan las mismas instancias de Point2D.
     */
    private List<Point2D> copyPoints(
            List<Point2D> points
    ) {
        List<Point2D> copy = new ArrayList<>();

        if (points == null) {
            return copy;
        }

        for (Point2D point : points) {
            if (point != null) {
                copy.add(point.copy());
            }
        }

        return copy;
    }
}