package commons;

import coap.sever.CoapServerProcess;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mqtt.process.MqttSmartObjectProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import commons.resources.DTObjectResource;
import commons.resources.sensors.PressureSensorResource;
import commons.resources.sensors.TemperatureSensorResource;

import java.io.File;
import java.util.HashMap;


public class SmartObjectCommunicationProcess {

    private static final String RESOURCE_CONF_FILE = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\resource_conf.yaml";

    private static Logger logger = LoggerFactory.getLogger(SmartObjectCommunicationProcess.class);
    public static void main(String[] args) {

        ResourceConfiguration resourceConfiguration = readResourceFile();
        HashMap<String, DTObjectResource<?>> mapResource = new HashMap<>(){
            {
                put("temperature", new  TemperatureSensorResource(resourceConfiguration));
                put("pressure", new PressureSensorResource(resourceConfiguration));

            }
        };

        CoapServerProcess coapServerProcess = new CoapServerProcess(mapResource);
        coapServerProcess.start();

        coapServerProcess.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

        MqttSmartObjectProcess mqttSmartObjectProcess = new MqttSmartObjectProcess(mapResource);


    }

    private static ResourceConfiguration readResourceFile() {

        try{
        File file = new File(RESOURCE_CONF_FILE);

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        ResourceConfiguration resourceConfiguration = om.readValue(file, ResourceConfiguration.class);

        logger.info("Resource Configuration Loaded ! Conf: {}", resourceConfiguration);

        return resourceConfiguration;

    }catch (Exception e){
        e.printStackTrace();
        String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
        logger.error(" {}", errorMessage);
        return null;
    }
    }
}
