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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TemperatureDescriptor{");
        sb.append("value = ").append(value).append(", ");
        sb.append("provider = ").append(provider).append("}");
        return sb.toString();
    }
}