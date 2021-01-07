package mqtt.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelemetryMessage<T>{
    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("device_type")
    private String type;

    @JsonProperty("provider")
    private String file_provider;

    @JsonProperty("value")
    private T dataValue;


    public TelemetryMessage() {
    }

    public TelemetryMessage(String type, T dataValue, String file_provider) {
        this.timestamp = System.currentTimeMillis();
        this.type = type;
        this.dataValue = dataValue;
        this.file_provider = file_provider;
    }

    public TelemetryMessage(long timestamp, String type, T dataValue, String file_provider) {
        this.timestamp = timestamp;
        this.type = type;
        this.dataValue = dataValue;
        this.file_provider = file_provider;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getDataValue() {
        return dataValue;
    }

    public void setDataValue(T dataValue) {
        this.dataValue = dataValue;
    }

    public String getFile_provider() { return file_provider; }

    public void setFile_provider(String file_provider) { this.file_provider = file_provider; }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TelemetryMessage{");
        sb.append("timestamp = ").append(timestamp);
        sb.append(", device_type = ").append(type);
        sb.append(", value = ").append(dataValue);
        sb.append(", provider = ").append(file_provider);
        sb.append('}');
        return sb.toString();
    }

}
