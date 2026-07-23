package cr.ac.cenfotec.pixelforge.model;

/**
 * Representa un punto o vector en el plano cartesiano.
 */
public class Point2D {

    private double x;
    private double y;

    public Point2D() {
    }

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Convierte el punto a un vector columna
     * de coordenadas homogéneas:
     *
     * | x |
     * | y |
     * | 1 |
     */
    public double[] toHomogeneousVector() {
        return new double[]{x, y, 1};
    }

    /**
     * Crea una copia independiente del punto.
     */
    public Point2D copy() {
        return new Point2D(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}