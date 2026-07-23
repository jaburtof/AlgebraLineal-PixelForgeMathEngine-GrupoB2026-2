package cr.ac.cenfotec.pixelforge.math;

/**
 * Construye las matrices utilizadas en transformaciones 2D.
 */
public final class TransformationMatrices {

    private TransformationMatrices() {
    }

    /**
     * Matriz de traslación:
     *
     * | 1  0  tx |
     * | 0  1  ty |
     * | 0  0  1  |
     */
    public static double[][] translation(double tx, double ty) {
        return new double[][]{
                {1, 0, tx},
                {0, 1, ty},
                {0, 0, 1}
        };
    }

    /**
     * Matriz de rotación:
     *
     * | cos θ  -sin θ  0 |
     * | sin θ   cos θ  0 |
     * |   0       0     1 |
     */
    public static double[][] rotation(double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        double cosine = Math.cos(angleRadians);
        double sine = Math.sin(angleRadians);

        return new double[][]{
                {cosine, -sine, 0},
                {sine, cosine, 0},
                {0, 0, 1}
        };
    }

    /**
     * Matriz de escalamiento:
     *
     * | sx  0   0 |
     * | 0   sy  0 |
     * | 0   0   1 |
     */
    public static double[][] scaling(double sx, double sy) {
        return new double[][]{
                {sx, 0, 0},
                {0, sy, 0},
                {0, 0, 1}
        };
    }

    /**
     * Reflexión respecto al eje X:
     *
     * | 1   0  0 |
     * | 0  -1  0 |
     * | 0   0  1 |
     */
    public static double[][] reflectionX() {
        return new double[][]{
                {1, 0, 0},
                {0, -1, 0},
                {0, 0, 1}
        };
    }

    /**
     * Reflexión respecto al eje Y:
     *
     * | -1  0  0 |
     * |  0  1  0 |
     * |  0  0  1 |
     */
    public static double[][] reflectionY() {
        return new double[][]{
                {-1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
    }

    /**
     * Reflexión respecto al origen:
     *
     * | -1   0  0 |
     * |  0  -1  0 |
     * |  0   0  1 |
     */
    public static double[][] reflectionOrigin() {
        return new double[][]{
                {-1, 0, 0},
                {0, -1, 0},
                {0, 0, 1}
        };
    }

    /**
     * Reflexión respecto a la recta y = x:
     *
     * | 0  1  0 |
     * | 1  0  0 |
     * | 0  0  1 |
     */
    public static double[][] reflectionYEqualsX() {
        return new double[][]{
                {0, 1, 0},
                {1, 0, 0},
                {0, 0, 1}
        };
    }
}