package coap.sever.configurationCoap;

import sharedProtocolsClass.ProtocolConfiguration;

public class CoapSmartObjectConfiguration extends ProtocolConfiguration {

    private int resourceMaxAgeSecond;

    public CoapSmartObjectConfiguration() {
        super();
    }


    public int getResourceMaxAgeSecond() {
        return resourceMaxAgeSecond;
    }

    public void setResourceMaxAgeSecond(int resourceMaxAgeSecond) {
        this.resourceMaxAgeSecond = resourceMaxAgeSecond;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MqttSmartObjectConfiguration{");
        sb.append(", deviceNameSpace='").append(super.getDeviceNameSpace());
        sb.append(", deviceID= ").append(super.getDeviceID());
        sb.append(", telemetryUpdateTimeMs=").append(super.getTelemetryUpdateTimeMs());
        sb.append(", totalTelemetryMessageLimit=").append(super.getTotalTelemetryMessageLimit());
        sb.append(", startUpDelayMs=").append(super.getStartUpDelayMs());
        sb.append(", resourceMaxAgeSecond=").append(resourceMaxAgeSecond);
        sb.append('}');
        return sb.toString();
    }
}
