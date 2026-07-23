package cr.ac.cenfotec.pixelforge.math;

import cr.ac.cenfotec.pixelforge.model.Point2D;
import cr.ac.cenfotec.pixelforge.service.TransformationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformationTest {

    private final TransformationService transformationService =
            new TransformationService();

    /**
     * Caso de prueba #1:
     *
     * Rotar el punto (2,0) un ángulo de 90 grados.
     *
     * Resultado esperado:
     * (0,2)
     */
    @Test
    void shouldRotatePointNinetyDegrees() {
        Point2D originalPoint = new Point2D(2, 0);

        double[][] rotationMatrix =
                TransformationMatrices.rotation(90);

        Point2D transformedPoint =
                transformationService.applyToPoint(
                        rotationMatrix,
                        originalPoint
                );

        assertEquals(
                0,
                transformedPoint.getX(),
                0.000001
        );

        assertEquals(
                2,
                transformedPoint.getY(),
                0.000001
        );
    }

    /**
     * Caso de prueba #2:
     *
     * Punto inicial: (1,1)
     *
     * 1. Escalar:
     *    sx = 2
     *    sy = 3
     *
     *    Resultado intermedio: (2,3)
     *
     * 2. Trasladar:
     *    tx = 4
     *    ty = -1
     *
     *    Resultado final: (6,2)
     */
    @Test
    void shouldApplyConsecutiveTransformations() {
        Point2D originalPoint = new Point2D(1, 1);

        double[][] scalingMatrix =
                TransformationMatrices.scaling(2, 3);

        double[][] translationMatrix =
                TransformationMatrices.translation(4, -1);

        /*
         * Matriz compuesta:
         *
         * T × S
         *
         * Primero se aplica S y después T.
         */
        double[][] combinedMatrix =
                MatrixOperations.multiply(
                        translationMatrix,
                        scalingMatrix
                );

        Point2D transformedPoint =
                transformationService.applyToPoint(
                        combinedMatrix,
                        originalPoint
                );

        assertEquals(
                6,
                transformedPoint.getX(),
                0.000001
        );

        assertEquals(
                2,
                transformedPoint.getY(),
                0.000001
        );
    }
}