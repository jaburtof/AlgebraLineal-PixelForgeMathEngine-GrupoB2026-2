package cr.ac.cenfotec.pixelforge.controller;

import cr.ac.cenfotec.pixelforge.dto.TransformationRequest;
import cr.ac.cenfotec.pixelforge.math.TransformationMatrices;
import cr.ac.cenfotec.pixelforge.model.Figure2D;
import cr.ac.cenfotec.pixelforge.model.Point2D;
import cr.ac.cenfotec.pixelforge.model.TransformationRecord;
import cr.ac.cenfotec.pixelforge.service.TransformationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class FigureController {

    private final TransformationService transformationService;

    public FigureController(
            TransformationService transformationService
    ) {
        this.transformationService = transformationService;
    }

    /**
     * Muestra la interfaz principal.
     */
    @GetMapping("/engine")
    public String showEngine(
            HttpSession session,
            Model model
    ) {
        Figure2D figure = getOrCreateFigure(session);
        List<TransformationRecord> history =
                getOrCreateHistory(session);

        model.addAttribute("figure", figure);
        model.addAttribute(
                "request",
                new TransformationRequest()
        );
        model.addAttribute("history", history);
        model.addAttribute(
                "lastMatrix",
                session.getAttribute("lastMatrix")
        );
        model.addAttribute(
                "lastFormula",
                session.getAttribute("lastFormula")
        );
        model.addAttribute(
                "lastTransformationName",
                session.getAttribute("lastTransformationName")
        );
        model.addAttribute(
                "lastCalculations",
                session.getAttribute("lastCalculations")
        );

        Object errorMessage =
                session.getAttribute("errorMessage");

        model.addAttribute("errorMessage", errorMessage);

        /*
         * El error se muestra una sola vez.
         */
        session.removeAttribute("errorMessage");

        return "index";
    }

    /**
     * Procesa la transformación seleccionada por el usuario.
     */
    @PostMapping("/engine/transform")
    public String transform(
            TransformationRequest request,
            HttpSession session
    ) {
        try {
            Figure2D figure = getOrCreateFigure(session);

            double[][] matrix;
            String transformationName;
            String formula;

            String type = request.getType();

            if (type == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar una transformación."
                );
            }

            switch (type) {
                case "translation" -> {
                    double tx = requireValue(
                            request.getValue1(),
                            "Desplazamiento X"
                    );

                    double ty = requireValue(
                            request.getValue2(),
                            "Desplazamiento Y"
                    );

                    matrix =
                            TransformationMatrices.translation(
                                    tx,
                                    ty
                            );

                    transformationName = "Traslación";

                    formula =
                            "x' = x + tx, y' = y + ty. " +
                                    "tx = " + format(tx) +
                                    ", ty = " + format(ty);
                }

                case "rotation" -> {
                    double angle = requireValue(
                            request.getValue1(),
                            "Ángulo"
                    );

                    matrix =
                            TransformationMatrices.rotation(
                                    angle
                            );

                    transformationName = "Rotación";

                    formula =
                            "x' = x cos(θ) - y sin(θ), " +
                                    "y' = x sin(θ) + y cos(θ). " +
                                    "θ = " + format(angle) + "°";
                }

                case "scaling" -> {
                    double sx = requireValue(
                            request.getValue1(),
                            "Escala X"
                    );

                    double sy = requireValue(
                            request.getValue2(),
                            "Escala Y"
                    );

                    matrix =
                            TransformationMatrices.scaling(
                                    sx,
                                    sy
                            );

                    transformationName = "Escalamiento";

                    formula =
                            "x' = sx × x, y' = sy × y. " +
                                    "sx = " + format(sx) +
                                    ", sy = " + format(sy);
                }

                case "reflectionX" -> {
                    matrix =
                            TransformationMatrices.reflectionX();

                    transformationName =
                            "Reflexión respecto al eje X";

                    formula = "x' = x, y' = -y";
                }

                case "reflectionY" -> {
                    matrix =
                            TransformationMatrices.reflectionY();

                    transformationName =
                            "Reflexión respecto al eje Y";

                    formula = "x' = -x, y' = y";
                }

                case "reflectionOrigin" -> {
                    matrix =
                            TransformationMatrices
                                    .reflectionOrigin();

                    transformationName =
                            "Reflexión respecto al origen";

                    formula = "x' = -x, y' = -y";
                }

                case "reflectionYEqualsX" -> {
                    matrix =
                            TransformationMatrices
                                    .reflectionYEqualsX();

                    transformationName =
                            "Reflexión respecto a y = x";

                    formula = "x' = y, y' = x";
                }

                default -> throw new IllegalArgumentException(
                        "La transformación seleccionada no existe."
                );
            }

            List<Point2D> beforePoints =
                    copyPoints(figure.getCurrentPoints());

            /*
             * Aplicamos la matriz sobre los puntos actuales.
             * Esto permite transformaciones consecutivas.
             */
            transformationService.applyToFigure(
                    figure,
                    matrix
            );

            List<Point2D> afterPoints =
                    copyPoints(figure.getCurrentPoints());

            List<String> calculations =
                    buildCalculations(
                            matrix,
                            beforePoints,
                            afterPoints
                    );

            TransformationRecord record =
                    new TransformationRecord(
                            transformationName,
                            formula,
                            matrix,
                            beforePoints,
                            afterPoints
                    );

            List<TransformationRecord> history =
                    getOrCreateHistory(session);

            history.add(record);

            session.setAttribute("figure", figure);
            session.setAttribute("history", history);
            session.setAttribute("lastMatrix", matrix);
            session.setAttribute("lastFormula", formula);
            session.setAttribute(
                    "lastTransformationName",
                    transformationName
            );
            session.setAttribute(
                    "lastCalculations",
                    calculations
            );

        } catch (IllegalArgumentException exception) {
            session.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }

        return "redirect:/engine";
    }

    /**
     * Restablece la figura original y elimina el historial.
     */
    @PostMapping("/engine/reset")
    public String reset(HttpSession session) {
        session.removeAttribute("figure");
        session.removeAttribute("history");
        session.removeAttribute("lastMatrix");
        session.removeAttribute("lastFormula");
        session.removeAttribute("lastTransformationName");
        session.removeAttribute("lastCalculations");
        session.removeAttribute("errorMessage");

        return "redirect:/engine";
    }

    /**
     * Crea un cuadrado inicial si todavía no existe una figura
     * guardada en la sesión.
     */
    private Figure2D getOrCreateFigure(HttpSession session) {
        Figure2D figure =
                (Figure2D) session.getAttribute("figure");

        if (figure == null) {
            List<Point2D> points = new ArrayList<>();

            points.add(new Point2D(0, 0));
            points.add(new Point2D(2, 0));
            points.add(new Point2D(2, 2));
            points.add(new Point2D(0, 2));

            figure = new Figure2D(
                    "Cuadrado",
                    points
            );

            session.setAttribute("figure", figure);
        }

        return figure;
    }

    /**
     * Obtiene o crea el historial de la sesión actual.
     */
    @SuppressWarnings("unchecked")
    private List<TransformationRecord> getOrCreateHistory(
            HttpSession session
    ) {
        List<TransformationRecord> history =
                (List<TransformationRecord>)
                        session.getAttribute("history");

        if (history == null) {
            history = new ArrayList<>();
            session.setAttribute("history", history);
        }

        return history;
    }

    /**
     * Construye una explicación matemática para cada punto.
     */
    private List<String> buildCalculations(
            double[][] matrix,
            List<Point2D> beforePoints,
            List<Point2D> afterPoints
    ) {
        List<String> calculations = new ArrayList<>();

        for (int index = 0; index < beforePoints.size(); index++) {
            Point2D before = beforePoints.get(index);
            Point2D after = afterPoints.get(index);

            String calculation =
                    "P" + (index + 1) + "': " +
                            "x' = (" + format(matrix[0][0]) +
                            " × " + format(before.getX()) + ") + (" +
                            format(matrix[0][1]) +
                            " × " + format(before.getY()) + ") + (" +
                            format(matrix[0][2]) +
                            " × 1) = " + format(after.getX()) +
                            "; y' = (" + format(matrix[1][0]) +
                            " × " + format(before.getX()) + ") + (" +
                            format(matrix[1][1]) +
                            " × " + format(before.getY()) + ") + (" +
                            format(matrix[1][2]) +
                            " × 1) = " + format(after.getY());

            calculations.add(calculation);
        }

        return calculations;
    }

    private List<Point2D> copyPoints(
            List<Point2D> points
    ) {
        List<Point2D> copy = new ArrayList<>();

        for (Point2D point : points) {
            copy.add(point.copy());
        }

        return copy;
    }

    private double requireValue(
            Double value,
            String fieldName
    ) {
        if (value == null) {
            throw new IllegalArgumentException(
                    fieldName + " es obligatorio."
            );
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    fieldName + " debe ser un número válido."
            );
        }

        return value;
    }

    private String format(double value) {
        if (Math.abs(value) < 1.0E-10) {
            value = 0;
        }

        String text = String.format(
                Locale.US,
                "%.4f",
                value
        );

        text = text.replaceAll("0+$", "");
        text = text.replaceAll("\\.$", "");

        return text;
    }
}