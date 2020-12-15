package mqtt.model;

public class TemperatureDescriptor {
    public static final String FILE_TEMPERATURE_PROVIDER = "temperature_provider_file";

    private double value;

    private String provider;

    public TemperatureDescriptor() {
    }

    public TemperatureDescriptor(double value, String provider) {
        this.value = value;
        this.provider = provider;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TemperatureDescriptor{");
        sb.append("value = ").append(value).append(", ");
        sb.append("provider = ").append(provider).append("}");
        return sb.toString();
    }
}