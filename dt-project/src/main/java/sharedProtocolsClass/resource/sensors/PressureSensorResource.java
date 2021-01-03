package sharedProtocolsClass.resource.sensors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mqtt.configurationMqtt.MqttSmartObjectConfiguration;
import mqtt.model.PressureDescpritor;
import mqtt.model.TemperatureDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sharedProtocolsClass.ProtocolConfiguration;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.ResourceDataListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class PressureSensorResource extends DTObjectResource<PressureDescpritor> {

    private static final Logger logger = LoggerFactory.getLogger(PressureSensorResource.class);

    public static final String RESOURCE_TYPE = "iot:sensors:pressure";

    //C:\Users\User\Desktop\Uni\Terzo anno\IoT\Git-DT-project\IoT-DT-Project\dt-project\data
    //TODO Insert a for to have all the documents and assigned.
    public static final String PRESSURE_FILE_NAME = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\data\\PS1.txt";

    private List<Double> dataList = new ArrayList<Double>();

    private ListIterator<Double> dataListIterator;

    private Timer updateTimer = null;

    private PressureDescpritor updatePressureDescriptor = null;

    private ProtocolConfiguration protocolConfiguration;

    public PressureSensorResource(ProtocolConfiguration protocolConfiguration) {
        super(UUID.randomUUID().toString(), PressureSensorResource.RESOURCE_TYPE);
        this.protocolConfiguration = protocolConfiguration;
        init();
    }

    public PressureSensorResource(String id, String type, ProtocolConfiguration protocolConfiguration) {
        super(id, type);
        this.protocolConfiguration = protocolConfiguration;
        init();
    }

    private void init() {
        try{
            this.updatePressureDescriptor = new PressureDescpritor();

            BufferedReader cvsReader = new BufferedReader(new FileReader(PRESSURE_FILE_NAME));
            String row;
            while ((row = cvsReader.readLine()) != null){
                String [] line = row.split("\t");
                for (String s : line) {
                    dataList.add(Double.parseDouble(s));
                }
            }
            logger.info("Pressure File correctly loaded! Size: {}", this.dataList.size());

            this.dataListIterator = this.dataList.listIterator();

            startPeriodicEventValueUpdateTask();

        }catch(Exception e){
            logger.error("Error init Resource Object! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask() {
        try{
            logger.info("Starting periodic Update Task with Period {} ms", protocolConfiguration.getTelemetryUpdateTimeMs());

            this.updateTimer = new Timer();
            this.updateTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(dataListIterator.hasNext()) {
                        Double currentPressure = dataListIterator.next();
                        updatePressureDescriptor = new PressureDescpritor(
                                currentPressure);
                        notifyUpdate(updatePressureDescriptor);
                    }else{
                        logger.info("End of the document");
                    }
                }
            }, protocolConfiguration.getStartUpDelayMs(), protocolConfiguration.getTelemetryUpdateTimeMs());
        }catch (Exception e){
            logger.error("Error executing periodic resource value {}", e.getLocalizedMessage());
        }
    }
    @Override
    public PressureDescpritor loadUpdateValue() {
        return this.updatePressureDescriptor;
    }


    /*private static final String MQTT_SMARTOBJECT_CONFIGURATION_FILE = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\smart_object_mqtt_conf.yaml";

    private static  ProtocolConfiguration readConfigurationFile() {

        try{

            MqttSmartObjectConfiguration mqttSmartObjectConfiguration = new MqttSmartObjectConfiguration();

            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //File file = new File(classLoader.getResource(WLDT_CONFIGURATION_FILE).getFile());
            File file = new File(MQTT_SMARTOBJECT_CONFIGURATION_FILE);

            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            mqttSmartObjectConfiguration = om.readValue(file, MqttSmartObjectConfiguration.class);

            if(mqttSmartObjectConfiguration.getDeviceID() == null){
                mqttSmartObjectConfiguration.setDeviceID(UUID.randomUUID().toString());
            }

            logger.info("MQTT Configuration Loaded ! Conf: {}", mqttSmartObjectConfiguration);

            return (ProtocolConfiguration) mqttSmartObjectConfiguration;

        }catch (Exception e){
            e.printStackTrace();
            String errorMessage = String.format("ERROR LOADING CONFIGURATION FILE ! Error: %s", e.getLocalizedMessage());
            logger.error("{} {}", errorMessage);
            return null;
        }
    }

     public static void main(String[] args) {
        PressureSensorResource pressureSensorResource = new PressureSensorResource(readConfigurationFile());
        pressureSensorResource.addDataListener(new ResourceDataListener<PressureDescpritor>() {
            @Override
            public void onDataChanged(DTObjectResource<PressureDescpritor> resource, PressureDescpritor updatedValue) {
                if(resource != null && updatedValue != null)
                    logger.info("Device: {} --> New Value Recived: {}", resource.getId(), updatedValue);
                else
                    logger.error("onDataChangedCallback --> Null Resource or Update Value");
            }

        });
    }*/
}
