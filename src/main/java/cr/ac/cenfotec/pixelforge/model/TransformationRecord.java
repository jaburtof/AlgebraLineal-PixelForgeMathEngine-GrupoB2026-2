package cr.ac.cenfotec.pixelforge.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Registra una transformación aplicada a una figura.
 */
public class TransformationRecord {

    private final String transformationName;
    private final String formula;
    private final double[][] matrix;
    private final List<Point2D> beforePoints;
    private final List<Point2D> afterPoints;

    public TransformationRecord(
            String transformationName,
            String formula,
            double[][] matrix,
            List<Point2D> beforePoints,
            List<Point2D> afterPoints
    ) {
        this.transformationName = transformationName;
        this.formula = formula;
        this.matrix = copyMatrix(matrix);
        this.beforePoints = copyPoints(beforePoints);
        this.afterPoints = copyPoints(afterPoints);
    }

    public String getTransformationName() {
        return transformationName;
    }

    public String getFormula() {
        return formula;
    }

    public double[][] getMatrix() {
        return copyMatrix(matrix);
    }

    public List<Point2D> getBeforePoints() {
        return copyPoints(beforePoints);
    }

    public List<Point2D> getAfterPoints() {
        return copyPoints(afterPoints);
    }

    private double[][] copyMatrix(double[][] original) {
        double[][] copy =
                new double[original.length][original[0].length];

        for (int row = 0; row < original.length; row++) {
            for (
                    int column = 0;
                    column < original[row].length;
                    column++
            ) {
                copy[row][column] = original[row][column];
            }
        }

        return copy;
    }

    private List<Point2D> copyPoints(List<Point2D> original) {
        List<Point2D> copy = new ArrayList<>();

        for (Point2D point : original) {
            copy.add(point.copy());
        }

        return copy;
    }
}