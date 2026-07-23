package cr.ac.cenfotec.pixelforge.math;

/**
 * Operaciones fundamentales con matrices.
 *
 * No utiliza bibliotecas matemáticas externas.
 * Todos los cálculos se realizan mediante ciclos.
 */
public final class MatrixOperations {

    private static final double EPSILON = 1.0E-10;

    /**
     * Constructor privado para impedir crear objetos de esta clase.
     */
    private MatrixOperations() {
    }

    /**
     * Multiplica dos matrices:
     *
     * C = A × B
     *
     * Fórmula:
     *
     * C[i][j] = sumatoria de A[i][k] × B[k][j]
     */
    public static double[][] multiply(double[][] matrixA, double[][] matrixB) {
        validateMatrix(matrixA, "Matriz A");
        validateMatrix(matrixB, "Matriz B");

        int rowsA = matrixA.length;
        int columnsA = matrixA[0].length;

        int rowsB = matrixB.length;
        int columnsB = matrixB[0].length;

        if (columnsA != rowsB) {
            throw new IllegalArgumentException(
                    "No se pueden multiplicar las matrices: " +
                            "las columnas de A deben ser iguales a las filas de B."
            );
        }

        double[][] result = new double[rowsA][columnsB];

        for (int i = 0; i < rowsA; i++) {

            for (int j = 0; j < columnsB; j++) {

                double sum = 0;

                for (int k = 0; k < columnsA; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }

                result[i][j] = cleanSmallValue(sum);
            }
        }

        return result;
    }

    /**
     * Multiplica una matriz por un vector columna:
     *
     * resultado = matriz × vector
     */
    public static double[] multiply(double[][] matrix, double[] vector) {
        validateMatrix(matrix, "Matriz");

        if (vector == null || vector.length == 0) {
            throw new IllegalArgumentException(
                    "El vector no puede estar vacío."
            );
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        if (columns != vector.length) {
            throw new IllegalArgumentException(
                    "No se puede multiplicar la matriz por el vector: " +
                            "la cantidad de columnas debe coincidir con el tamaño del vector."
            );
        }

        double[] result = new double[rows];

        for (int i = 0; i < rows; i++) {

            double sum = 0;

            for (int j = 0; j < columns; j++) {
                sum += matrix[i][j] * vector[j];
            }

            result[i] = cleanSmallValue(sum);
        }

        return result;
    }

    /**
     * Genera una matriz identidad de cualquier tamaño.
     *
     * Ejemplo de identidad 3 × 3:
     *
     * | 1 0 0 |
     * | 0 1 0 |
     * | 0 0 1 |
     */
    public static double[][] identity(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(
                    "El tamaño debe ser mayor que cero."
            );
        }

        double[][] identityMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            identityMatrix[i][i] = 1;
        }

        return identityMatrix;
    }

    /**
     * Crea una copia independiente de una matriz.
     */
    public static double[][] copy(double[][] matrix) {
        validateMatrix(matrix, "Matriz");

        double[][] copy = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[i].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }

        return copy;
    }

    /**
     * Verifica que una matriz sea válida y rectangular.
     */
    private static void validateMatrix(
            double[][] matrix,
            String matrixName
    ) {
        if (
                matrix == null ||
                        matrix.length == 0 ||
                        matrix[0] == null ||
                        matrix[0].length == 0
        ) {
            throw new IllegalArgumentException(
                    matrixName + " no puede estar vacía."
            );
        }

        int expectedColumns = matrix[0].length;

        for (int i = 0; i < matrix.length; i++) {

            if (
                    matrix[i] == null ||
                            matrix[i].length != expectedColumns
            ) {
                throw new IllegalArgumentException(
                        matrixName + " debe ser rectangular."
                );
            }
        }
    }

    /**
     * Convierte resultados extremadamente pequeños en cero.
     *
     * Por ejemplo:
     * 6.123233995736766E-17 se mostrará como 0.
     */
    private static double cleanSmallValue(double value) {
        if (Math.abs(value) < EPSILON) {
            return 0;
        }

        return value;
    }
}