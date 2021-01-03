package mqtt.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mqtt.configurationMqtt.MqttSmartObjectConfiguration;
import mqtt.device.MqttSmartObject;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.sensors.PressureSensorResource;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MqttSmartObjectProcess {

    private static final Logger logger = LoggerFactory.getLogger(MqttSmartObjectProcess.class);

    private static final String TAG = "[MQTT-SMARTOBJECT]";

    private static final String MQTT_SMARTOBJECT_CONFIGURATION_FILE = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\smart_object_mqtt_conf.yaml";

    private static MqttSmartObjectConfiguration mqttSmartObjectConfiguration;


    public MqttSmartObjectProcess()  {

        readConfigurationFile();

        run(new HashMap<>(){
            {
                put("temperature", new TemperatureSensorResource(mqttSmartObjectConfiguration));
                put("pressure", new PressureSensorResource(mqttSmartObjectConfiguration));

            }
        });
    }

    public MqttSmartObjectProcess(Map<String, DTObjectResource<?>> resourceMap){
        readConfigurationFile();
        run(resourceMap);
    }


    public void run(Map<String, DTObjectResource<?>> resourceMap) {

        try{

            MqttClientPersistence persistence = new MemoryPersistence();
            IMqttClient mqttClient = new MqttClient(String.format("tcp://%s:%d",
                mqttSmartObjectConfiguration.getMqttBrokerAddress(),
                mqttSmartObjectConfiguration.getMqttBrokerPort()),
                mqttSmartObjectConfiguration.getDeviceID(),
                persistence);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(mqttSmartObjectConfiguration.getAutomaticReconnect());
        options.setCleanSession(mqttSmartObjectConfiguration.getCleanSession());
        options.setConnectionTimeout(mqttSmartObjectConfiguration.getConnectionTimeout());
        options.setUserName(mqttSmartObjectConfiguration.getMqttUser());
        options.setPassword(mqttSmartObjectConfiguration.getMqttPw().toCharArray());

        mqttClient.connect(options);
        logger.info("MQTT Client Connected! Cliend ID: {}", mqttSmartObjectConfiguration.getDeviceID());

        MqttSmartObject mqttSmartObject = new MqttSmartObject();
        mqttSmartObject.init(mqttSmartObjectConfiguration, mqttClient, resourceMap);
        mqttSmartObject.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void readConfigurationFile(){

        try{

            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //File file = new File(classLoader.getResource(WLDT_CONFIGURATION_FILE).getFile());
            File file = new File(MQTT_SMARTOBJECT_CONFIGURATION_FILE);

            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            mqttSmartObjectConfiguration = om.readValue(file, MqttSmartObjectConfiguration.class);

            if(mqttSmartObjectConfiguration.getDeviceID() == null){
                mqttSmartObjectConfiguration.setDeviceID(UUID.randomUUID().toString());
            }

            logger.info("{} MQTT Configuration Loaded ! Conf: {}", TAG, mqttSmartObjectConfiguration);

        }catch (Exception e){
            e.printStackTrace();
            String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
            logger.error("{} {}", TAG, errorMessage);
        }
    }

    public static void main(String[] args) {

        MqttSmartObjectProcess mqttSmartObjectProcess = new MqttSmartObjectProcess();
    }

}
