"use strict";

document.addEventListener("DOMContentLoaded", function () {
    configureTransformationFields();
    drawCoordinatePlane();
});

/**
 * Muestra únicamente los campos requeridos por la
 * transformación seleccionada.
 */
function configureTransformationFields() {
    const typeSelect =
        document.getElementById("transformationType");

    const value1Group =
        document.getElementById("value1Group");

    const value2Group =
        document.getElementById("value2Group");

    const value1Label =
        document.getElementById("value1Label");

    const value2Label =
        document.getElementById("value2Label");

    const value1 =
        document.getElementById("value1");

    const value2 =
        document.getElementById("value2");

    if (!typeSelect) {
        return;
    }

    function updateFields(changeDefaults) {
        const type = typeSelect.value;

        value1Group.style.display = "block";
        value2Group.style.display = "block";

        if (type === "translation") {
            value1Label.textContent = "Desplazamiento X";
            value2Label.textContent = "Desplazamiento Y";

            if (changeDefaults) {
                value1.value = "1";
                value2.value = "1";
            }

            return;
        }

        if (type === "rotation") {
            value1Label.textContent = "Ángulo en grados";
            value2Group.style.display = "none";

            if (changeDefaults) {
                value1.value = "45";
            }

            return;
        }

        if (type === "scaling") {
            value1Label.textContent = "Escala X";
            value2Label.textContent = "Escala Y";

            if (changeDefaults) {
                value1.value = "2";
                value2.value = "2";
            }

            return;
        }

        /*
         * Las reflexiones no necesitan parámetros.
         */
        value1Group.style.display = "none";
        value2Group.style.display = "none";
    }

    typeSelect.addEventListener("change", function () {
        updateFields(true);
    });

    updateFields(false);
}

/**
 * Lee los puntos almacenados en atributos data-x y data-y.
 */
function readPoints(className) {
    const elements =
        document.querySelectorAll("." + className);

    return Array.from(elements).map(function (element) {
        return {
            x: Number(element.dataset.x),
            y: Number(element.dataset.y)
        };
    });
}

/**
 * Dibuja el plano cartesiano y ambas figuras.
 *
 * No utiliza bibliotecas gráficas.
 */
function drawCoordinatePlane() {
    const canvas =
        document.getElementById("coordinateCanvas");

    if (!canvas) {
        return;
    }

    const context = canvas.getContext("2d");

    const originalPoints = readPoints("original-point");
    const currentPoints = readPoints("current-point");

    const allPoints = [
        ...originalPoints,
        ...currentPoints,
        {x: 0, y: 0}
    ];

    const padding = 70;

    let minimumX =
        Math.min(...allPoints.map(point => point.x));

    let maximumX =
        Math.max(...allPoints.map(point => point.x));

    let minimumY =
        Math.min(...allPoints.map(point => point.y));

    let maximumY =
        Math.max(...allPoints.map(point => point.y));

    /*
     * Agregamos espacio alrededor de las figuras.
     */
    minimumX -= 1;
    maximumX += 1;
    minimumY -= 1;
    maximumY += 1;

    const rangeX = maximumX - minimumX;
    const rangeY = maximumY - minimumY;

    const scaleX =
        (canvas.width - padding * 2) / rangeX;

    const scaleY =
        (canvas.height - padding * 2) / rangeY;

    const scale = Math.min(scaleX, scaleY);

    function convertX(x) {
        return padding + (x - minimumX) * scale;
    }

    function convertY(y) {
        return canvas.height -
            padding -
            (y - minimumY) * scale;
    }

    context.clearRect(
        0,
        0,
        canvas.width,
        canvas.height
    );

    drawGrid(
        context,
        canvas,
        convertX,
        convertY,
        minimumX,
        maximumX,
        minimumY,
        maximumY
    );

    drawAxes(
        context,
        canvas,
        convertX,
        convertY
    );

    drawFigure(
        context,
        originalPoints,
        convertX,
        convertY,
        "#2563eb",
        "Original"
    );

    drawFigure(
        context,
        currentPoints,
        convertX,
        convertY,
        "#dc2626",
        "Actual"
    );
}

function drawGrid(
    context,
    canvas,
    convertX,
    convertY,
    minimumX,
    maximumX,
    minimumY,
    maximumY
) {
    context.save();
    context.strokeStyle = "#e2e8f0";
    context.lineWidth = 1;
    context.font = "12px Arial";
    context.fillStyle = "#64748b";

    const startX = Math.floor(minimumX);
    const endX = Math.ceil(maximumX);

    for (let x = startX; x <= endX; x++) {
        const screenX = convertX(x);

        context.beginPath();
        context.moveTo(screenX, 0);
        context.lineTo(screenX, canvas.height);
        context.stroke();

        context.fillText(
            String(x),
            screenX + 4,
            convertY(0) - 7
        );
    }

    const startY = Math.floor(minimumY);
    const endY = Math.ceil(maximumY);

    for (let y = startY; y <= endY; y++) {
        const screenY = convertY(y);

        context.beginPath();
        context.moveTo(0, screenY);
        context.lineTo(canvas.width, screenY);
        context.stroke();

        if (y !== 0) {
            context.fillText(
                String(y),
                convertX(0) + 7,
                screenY - 5
            );
        }
    }

    context.restore();
}

function drawAxes(
    context,
    canvas,
    convertX,
    convertY
) {
    context.save();
    context.strokeStyle = "#334155";
    context.lineWidth = 2;

    const originX = convertX(0);
    const originY = convertY(0);

    context.beginPath();
    context.moveTo(0, originY);
    context.lineTo(canvas.width, originY);
    context.stroke();

    context.beginPath();
    context.moveTo(originX, 0);
    context.lineTo(originX, canvas.height);
    context.stroke();

    context.restore();
}

function drawFigure(
    context,
    points,
    convertX,
    convertY,
    color,
    label
) {
    if (points.length === 0) {
        return;
    }

    context.save();
    context.strokeStyle = color;
    context.fillStyle = color;
    context.lineWidth = 3;

    context.beginPath();

    points.forEach(function (point, index) {
        const screenX = convertX(point.x);
        const screenY = convertY(point.y);

        if (index === 0) {
            context.moveTo(screenX, screenY);
        } else {
            context.lineTo(screenX, screenY);
        }
    });

    /*
     * Cerramos el polígono conectando el último punto
     * con el primero.
     */
    context.closePath();
    context.stroke();

    points.forEach(function (point, index) {
        const screenX = convertX(point.x);
        const screenY = convertY(point.y);

        context.beginPath();
        context.arc(
            screenX,
            screenY,
            5,
            0,
            Math.PI * 2
        );
        context.fill();

        context.font = "bold 13px Arial";

        context.fillText(
            "P" + (index + 1),
            screenX + 8,
            screenY - 8
        );
    });

    context.font = "bold 13px Arial";
    context.fillText(
        label,
        convertX(points[0].x) + 8,
        convertY(points[0].y) + 22
    );

    context.restore();
}