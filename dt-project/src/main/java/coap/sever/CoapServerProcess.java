package coap.sever;

import coap.resource.PressureResource;
import coap.resource.TemperatureResource;
import coap.sever.configurationCoap.CoapSmartObjectConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mqtt.configurationMqtt.MqttSmartObjectConfiguration;
import mqtt.message.TelemetryMessage;
import mqtt.model.TemperatureDescriptor;
import org.eclipse.paho.client.mqttv3.MqttException;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.ResourceDataListener;
import sharedProtocolsClass.resource.sensors.PressureSensorResource;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.UUID;


public class CoapServerProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(CoapServerProcess.class);

    private final static String COAP_SMARTOBJECT_CONFIGURATION_FILE = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\smart_object_coap_conf.yaml";
    private static CoapSmartObjectConfiguration coapSmartObjectConfiguration;
    private static final String TAG = "[COAP-SMARTOBJECT]";

    public CoapServerProcess() {

        super();
        readConfigurationFile();

        String deviceId = String.format("dt:%s",coapSmartObjectConfiguration.getDeviceID());

        TemperatureSensorResource temperatureSensorResource = new TemperatureSensorResource(coapSmartObjectConfiguration);
        PressureSensorResource pressureSensorResource = new PressureSensorResource(coapSmartObjectConfiguration);

        TemperatureResource temperatureResource = new TemperatureResource(coapSmartObjectConfiguration, "temperature",temperatureSensorResource);
        PressureResource pressureResource = new PressureResource(coapSmartObjectConfiguration, "pressure",pressureSensorResource );
        //PressureResource pressureResource = new PressureResource(deviceId, "pressure");

        logger.info("Defining and adding resources ...");

        this.add(temperatureResource);
        this.add(pressureResource);

    }

    public CoapServerProcess (Map<String, DTObjectResource<?>> resourceMap){
        super();

        readConfigurationFile();

        String deviceId = String.format("dt:%s",coapSmartObjectConfiguration.getDeviceID());

        logger.info("Defining and adding resources ...");

        resourceMap.entrySet().forEach(resourceEntry ->{

            if(resourceEntry.getKey() != null && resourceEntry.getValue() != null){
                DTObjectResource dtObjectResource = resourceEntry.getValue();
                if(dtObjectResource.getType().equals(TemperatureSensorResource.RESOURCE_TYPE)){
                    TemperatureResource temperatureResource = new TemperatureResource(coapSmartObjectConfiguration, "temperature",(TemperatureSensorResource) dtObjectResource);
                    this.add(temperatureResource);
                }
                if(dtObjectResource.getType().equals(PressureSensorResource.RESOURCE_TYPE)){
                    PressureResource pressureResource = new PressureResource(coapSmartObjectConfiguration, "pressure", (PressureSensorResource) dtObjectResource);
                    this.add(pressureResource);
                }
        }
    });
    }



    private static void readConfigurationFile(){

        try{

            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //File file = new File(classLoader.getResource(WLDT_CONFIGURATION_FILE).getFile());
            File file = new File(COAP_SMARTOBJECT_CONFIGURATION_FILE);

            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            coapSmartObjectConfiguration = om.readValue(file, CoapSmartObjectConfiguration.class);

            if(coapSmartObjectConfiguration.getDeviceID() == null){
                coapSmartObjectConfiguration.setDeviceID(UUID.randomUUID().toString());
            }

            logger.info("{} CoAP Configuration Loaded ! Conf: {}", TAG,coapSmartObjectConfiguration);

        }catch (Exception e){
            e.printStackTrace();
            String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
            logger.error("{} {}", TAG, errorMessage);
        }
    }


    public static void main(String[] args) {

        CoapServerProcess coapServerProcess = new CoapServerProcess();

        logger.info("Starting Coap Server ...");

        coapServerProcess.start();

        logger.info("Coap Server Started");


        coapServerProcess.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
        });
    }
}
