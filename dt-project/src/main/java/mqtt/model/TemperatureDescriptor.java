package mqtt.model;

public class TemperatureDescriptor {

    //Not really sure if is better this variable is here
    public static final String FILE_TEMPERATURE_PROVIDER = "temperature_provider_file";

    private double value;

    public TemperatureDescriptor() {
    }

    public TemperatureDescriptor(double value) {
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
        final StringBuffer sb = new StringBuffer("TemperatureDescriptor{");
        sb.append("value = ").append(value).append("} ");
        return sb.toString();
    }
}