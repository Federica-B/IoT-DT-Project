package mqtt.process;

import mqtt.device.MqttSmartObject;
import mqtt.resource.sensors.TemperatureSensorResource;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class SmartObjectProcess {

    private static final Logger logger = LoggerFactory.getLogger(SmartObjectProcess.class);

    //TODO replace the variable with a reader from file yaml
    private static String MQTT_BROKER_IP = "10.0.0.170";

    private static int MQTT_BROKER_PORT = 1883;

    public static void main(String[] args) {

        try{
        String smartObjectId = UUID.randomUUID().toString();

        MqttClientPersistence persistence = new MemoryPersistence();
        IMqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d",
                MQTT_BROKER_IP,
                MQTT_BROKER_PORT),
                smartObjectId,
                persistence);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        mqttClient.connect(options);
        logger.info("MQTT Client Connected! Cliend ID: {}", smartObjectId);

        MqttSmartObject mqttSmartObject = new MqttSmartObject();
        mqttSmartObject.init(smartObjectId, mqttClient, new HashMap<>(){
            {
                put("temperature", new TemperatureSensorResource());
            }
        });
        mqttSmartObject.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
