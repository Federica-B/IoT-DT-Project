package mqtt.configurationMqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import sharedProtocolsClass.ProtocolConfiguration;

import java.io.File;
import java.util.UUID;

public class MqttSmartObjectConfiguration extends ProtocolConfiguration {
    private String mqttBrokerAddress;

    private int mqttBrokerPort;

    private String mqttUser;

    private String mqttPw;

    private String basicTopic;

    private String telemetryTopic;

    private int mqttOutgoingClientQoS;

    private boolean automaticReconnect;

    private boolean cleanSession;

    private int connectionTimeout;

    public MqttSmartObjectConfiguration() {
        super();
    }

    public String getMqttBrokerAddress() {
        return mqttBrokerAddress;
    }

    public void setMqttBrokerAddress(String mqttBrokerAddress) {
        this.mqttBrokerAddress = mqttBrokerAddress;
    }

    public int getMqttBrokerPort() {
        return mqttBrokerPort;
    }

    public void setMqttBrokerPort(int mqttBrokerPort) {
        this.mqttBrokerPort = mqttBrokerPort;
    }

    public String getMqttUser() {
        return mqttUser;
    }

    public void setMqttUser(String mqttUser) {
        this.mqttUser = mqttUser;
    }

    public String getMqttPw() {
        return mqttPw;
    }

    public void setMqttPw(String mqttPw) {
        this.mqttPw = mqttPw;
    }

    public String getBasicTopic() {
        return basicTopic;
    }

    public void setBasicTopic(String basicTopic) {
        this.basicTopic = basicTopic;
    }

    public String getTelemetryTopic() {
        return telemetryTopic;
    }

    public void setTelemetryTopic(String telemetryTopic) {
        this.telemetryTopic = telemetryTopic;
    }

    public int getMqttOutgoingClientQoS() {
        return mqttOutgoingClientQoS;
    }

    public void setMqttOutgoingClientQoS(int mqttOutgoingClientQoS) {
        this.mqttOutgoingClientQoS = mqttOutgoingClientQoS;
    }

    public boolean getAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public boolean getCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MqttSmartObjectConfiguration{");
        sb.append("mqttBrokerAddress='").append(mqttBrokerAddress);
        sb.append(", mqttBrokerPort=").append(mqttBrokerPort);
        sb.append(", mqttUser=").append(mqttUser);
        sb.append(", mqttPw").append(mqttPw);
        sb.append(", deviceNameSpace='").append(super.getDeviceNameSpace());
        sb.append(", deviceID= ").append(super.getDeviceID());
        sb.append(", basicTopic= ").append(basicTopic);
        sb.append(", telemetryTopic= ").append(telemetryTopic);
        sb.append(", telemetryUpdateTimeMs=").append(super.getTelemetryUpdateTimeMs());
        sb.append(", totalTelemetryMessageLimit=").append(super.getTotalTelemetryMessageLimit());
        sb.append(", startUpDelayMs=").append(super.getStartUpDelayMs());
        sb.append(", mqttOutgoingClientQoS=").append(mqttOutgoingClientQoS);
        sb.append(", automaticReconnect=").append(automaticReconnect);
        sb.append(", cleanSession=").append(cleanSession);
        sb.append(", connectionTimeout=").append(connectionTimeout);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        try{

            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //File file = new File(classLoader.getResource(WLDT_CONFIGURATION_FILE).getFile());
            String FILEX ="smart_object_mqtt_conf.yaml";
            File file = new File("C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\smart_object_mqtt_conf.yaml");

            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            MqttSmartObjectConfiguration mqttSmartObjectConfiguration = new MqttSmartObjectConfiguration();

            mqttSmartObjectConfiguration = om.readValue(file, MqttSmartObjectConfiguration.class);

            if(mqttSmartObjectConfiguration.getDeviceID() == null){
                mqttSmartObjectConfiguration.setDeviceID(UUID.randomUUID().toString());
            }

            System.out.println(mqttSmartObjectConfiguration.toString());


        }catch (Exception e){
            e.printStackTrace();
            String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
        }
    }

}

