package cr.ac.cenfotec.pixelforge.dto;

/**
 * Datos recibidos desde el formulario HTML.
 */
public class TransformationRequest {

    private String type = "rotation";
    private Double value1 = 45.0;
    private Double value2 = 1.0;

    public TransformationRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }
}