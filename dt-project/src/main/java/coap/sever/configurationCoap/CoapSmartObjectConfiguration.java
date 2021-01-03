package coap.sever.configurationCoap;

import sharedProtocolsClass.ResourceConfiguration;

public class CoapSmartObjectConfiguration extends ResourceConfiguration {

    private String deviceNameSpace;

    private String deviceID;

    private int resourceMaxAgeSecond;


    public CoapSmartObjectConfiguration() {
        super();
    }

    public String getDeviceNameSpace() {
        return deviceNameSpace;
    }

    public void setDeviceNameSpace(String deviceNameSpace) {
        this.deviceNameSpace = deviceNameSpace;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getResourceMaxAgeSecond() {
        return resourceMaxAgeSecond;
    }

    public void setResourceMaxAgeSecond(int resourceMaxAgeSecond) {
        this.resourceMaxAgeSecond = resourceMaxAgeSecond;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CoapSmartObjectConfiguration{");
        sb.append("deviceNameSpace='").append(deviceNameSpace);
        sb.append(", deviceID= ").append(deviceID);
        sb.append(", telemetryUpdateTimeMs=").append(super.getTelemetryUpdateTimeMs());
        sb.append(", totalTelemetryMessageLimit=").append(super.getTotalTelemetryMessageLimit());
        sb.append(", startUpDelayMs=").append(super.getStartUpDelayMs());
        sb.append(", resourceMaxAgeSecond=").append(resourceMaxAgeSecond);
        sb.append('}');
        return sb.toString();
    }
}
