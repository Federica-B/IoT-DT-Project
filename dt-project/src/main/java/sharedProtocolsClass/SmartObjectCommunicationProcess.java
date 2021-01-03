package sharedProtocolsClass;

import coap.sever.CoapServerProcess;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mqtt.process.MqttSmartObjectProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.sensors.PressureSensorResource;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;

import java.io.File;
import java.util.HashMap;


public class SmartObjectCommunicationProcess {

    private static final String RESOURCE_CONF_FILE = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\resource_conf.yaml";

    private static Logger logger = LoggerFactory.getLogger(SmartObjectCommunicationProcess.class);
    public static void main(String[] args) {

        ProtocolConfiguration protocolConfiguration = readResourceFile();
        HashMap<String, DTObjectResource<?>> mapResource = new HashMap<>(){
            {
                put("temperature", new  TemperatureSensorResource(protocolConfiguration));
                put("pressure", new PressureSensorResource(protocolConfiguration));

            }
        };

        CoapServerProcess coapServerProcess = new CoapServerProcess(mapResource);
        coapServerProcess.start();

        coapServerProcess.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
        });

        MqttSmartObjectProcess mqttSmartObjectProcess = new MqttSmartObjectProcess(mapResource);


    }

    private static ProtocolConfiguration readResourceFile() {

        try{
        File file = new File(RESOURCE_CONF_FILE);

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        ProtocolConfiguration protocolConfiguration = om.readValue(file, ProtocolConfiguration.class);

        logger.info("Resource Configuration Loaded ! Conf: {}",protocolConfiguration);

        return protocolConfiguration;

    }catch (Exception e){
        e.printStackTrace();
        String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
        logger.error(" {}", errorMessage);
        return null;
    }
    }
}
