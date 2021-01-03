package mqtt.model;

public class PressureDescpritor {

    public static final String FILE_TEMPERATURE_PROVIDER = "pressure_provider_file";

    private double value;

    public PressureDescpritor() {
    }

    public PressureDescpritor(double value) {
        this.value = value;

    }

    public static String getFileTemperatureProvider() {
        return FILE_TEMPERATURE_PROVIDER;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }



    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PressureDescriptor{");
        sb.append("value = ").append(value).append("} ");
        return sb.toString();
    }
}
