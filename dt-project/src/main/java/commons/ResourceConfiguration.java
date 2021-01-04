package commons;

public class ResourceConfiguration {


    private int telemetryUpdateTimeMs;

    private int totalTelemetryMessageLimit;

    private int startUpDelayMs;

    public ResourceConfiguration() {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ResourceConfiguration{");
        sb.append("telemetryUpdateTimeMs=").append(telemetryUpdateTimeMs);
        sb.append(", totalTelemetryMessageLimit=").append(totalTelemetryMessageLimit);
        sb.append(", startUpDelayMs=").append(startUpDelayMs);
        sb.append('}');
        return sb.toString();
    }
}
