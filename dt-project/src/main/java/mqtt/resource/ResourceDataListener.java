package mqtt.resource;

public interface ResourceDataListener <T> {
    public void onDataChanged(DTObjectResource<T> resource, T updatedValue);
}
