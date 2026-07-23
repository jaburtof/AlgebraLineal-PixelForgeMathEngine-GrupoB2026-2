package cr.ac.cenfotec.pixelforge.service;

import cr.ac.cenfotec.pixelforge.math.MatrixOperations;
import cr.ac.cenfotec.pixelforge.model.Figure2D;
import cr.ac.cenfotec.pixelforge.model.Point2D;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de aplicar matrices de transformación
 * a puntos y figuras bidimensionales.
 */
@Service
public class TransformationService {

    private static final double EPSILON = 1.0E-10;

    /**
     * Aplica una matriz 3 × 3 a un punto.
     */
    public Point2D applyToPoint(
            double[][] transformationMatrix,
            Point2D point
    ) {
        if (point == null) {
            throw new IllegalArgumentException(
                    "El punto no puede ser nulo."
            );
        }

        double[] originalVector = point.toHomogeneousVector();

        double[] transformedVector =
                MatrixOperations.multiply(
                        transformationMatrix,
                        originalVector
                );

        double homogeneousValue = transformedVector[2];

        if (Math.abs(homogeneousValue) < EPSILON) {
            throw new IllegalArgumentException(
                    "La coordenada homogénea no puede ser cero."
            );
        }

        double transformedX =
                transformedVector[0] / homogeneousValue;

        double transformedY =
                transformedVector[1] / homogeneousValue;

        return new Point2D(transformedX, transformedY);
    }

    /**
     * Aplica una matriz de transformación a una lista de puntos.
     */
    public List<Point2D> applyToPoints(
            double[][] transformationMatrix,
            List<Point2D> points
    ) {
        if (points == null) {
            throw new IllegalArgumentException(
                    "La lista de puntos no puede ser nula."
            );
        }

        List<Point2D> transformedPoints = new ArrayList<>();

        for (Point2D point : points) {
            Point2D transformedPoint =
                    applyToPoint(transformationMatrix, point);

            transformedPoints.add(transformedPoint);
        }

        return transformedPoints;
    }

    /**
     * Aplica una transformación a los puntos actuales de la figura.
     *
     * Esto permite aplicar varias transformaciones consecutivas.
     */
    public void applyToFigure(
            Figure2D figure,
            double[][] transformationMatrix
    ) {
        if (figure == null) {
            throw new IllegalArgumentException(
                    "La figura no puede ser nula."
            );
        }

        List<Point2D> transformedPoints =
                applyToPoints(
                        transformationMatrix,
                        figure.getCurrentPoints()
                );

        figure.setCurrentPoints(transformedPoints);
    }
}