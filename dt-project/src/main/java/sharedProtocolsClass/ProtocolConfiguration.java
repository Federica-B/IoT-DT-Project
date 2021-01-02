package sharedProtocolsClass;

public class ProtocolConfiguration {
    private String deviceNameSpace;

    private String deviceID;

    private int telemetryUpdateTimeMs;

    private int totalTelemetryMessageLimit;

    private int startUpDelayMs;

    public ProtocolConfiguration() {
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

    public int getTelemetryUpdateTimeMs() {
        return telemetryUpdateTimeMs;
    }

    public void setTelemetryUpdateTimeMs(int telemetryUpdateTimeMs) {
        this.telemetryUpdateTimeMs = telemetryUpdateTimeMs;
    }

    public int getTotalTelemetryMessageLimit() {
        return totalTelemetryMessageLimit;
    }

    public void setTotalTelemetryMessageLimit(int totalTelemetryMessageLimit) {
        this.totalTelemetryMessageLimit = totalTelemetryMessageLimit;
    }

    public int getStartUpDelayMs() {
        return startUpDelayMs;
    }

    public void setStartUpDelayMs(int startUpDelayMs) {
        this.startUpDelayMs = startUpDelayMs;
    }
}
