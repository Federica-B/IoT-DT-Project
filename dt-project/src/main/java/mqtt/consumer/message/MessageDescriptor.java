package mqtt.consumer.message;

public class MessageDescriptor {

    private long timestamp;

    private String type;

    private String data;

    private String provider;

    public MessageDescriptor() {
    }

    public MessageDescriptor(long timestamo, String type, String data, String provider) {
        this.timestamp = timestamp;
        this.type = type;
        this.data = data;
        this.provider = provider;
    }

    public long getTimestmp() {
        return timestamp;
    }

    public void setTimestmp(long timestmp) {
        this.timestamp = timestmp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageDescriptor{");
        sb.append("timestmp = ").append(timestamp);
        sb.append(", type = ").append(type);
        sb.append(", data = ").append(data);
        sb.append(", provider = ").append(provider);
        return sb.toString();
    }
}
