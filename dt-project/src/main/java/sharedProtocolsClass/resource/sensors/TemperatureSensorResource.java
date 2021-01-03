package sharedProtocolsClass.resource.sensors;

import mqtt.model.TemperatureDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sharedProtocolsClass.ResourceConfiguration;
import sharedProtocolsClass.resource.DTObjectResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class TemperatureSensorResource extends DTObjectResource<TemperatureDescriptor> {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureSensorResource.class);

    public static final String RESOURCE_TYPE = "iot:sensors:temperature";

    //C:\Users\User\Desktop\Uni\Terzo anno\IoT\Git-DT-project\IoT-DT-Project\dt-project\data
    //TODO Insert a for to have all the documents and assigned.
    public static final String TEMPERATURE_FILE_NAME = "C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Git-DT-project\\IoT-DT-Project\\dt-project\\data\\TS1.txt";

    private List<Double> dataList = new ArrayList<Double>();

    private ListIterator<Double> dataListIterator;

    private Timer updateTimer = null;

    private TemperatureDescriptor updateTemperatureDescriptor = null;

    private ResourceConfiguration resourceConfiguration;

    public TemperatureSensorResource(ResourceConfiguration resourceConfiguration) {
        super(UUID.randomUUID().toString(), TemperatureSensorResource.RESOURCE_TYPE);
        this.resourceConfiguration = resourceConfiguration;
        init();
    }

    public TemperatureSensorResource(String id, String type, ResourceConfiguration resourceConfiguration) {
        super(id, type);
        this.resourceConfiguration = resourceConfiguration;
        init();
    }

    private void init() {
        try{
            this.updateTemperatureDescriptor = new TemperatureDescriptor();

            BufferedReader cvsReader = new BufferedReader(new FileReader(TEMPERATURE_FILE_NAME));
            String row;
            while ((row = cvsReader.readLine()) != null){
                String [] line = row.split("\t");
                for (String s : line) {
                    dataList.add(Double.parseDouble(s));
                }
            }
            logger.info("Temperature File correctly loaded! Size: {}", this.dataList.size());

            this.dataListIterator = this.dataList.listIterator();

            startPeriodicEventValueUpdateTask();

        }catch(Exception e){
            logger.error("Error init Resource Object! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask() {
        try{
            logger.info("Starting periodic Update Task with Period {} ms", resourceConfiguration.getTelemetryUpdateTimeMs());

            this.updateTimer = new Timer();
            this.updateTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(dataListIterator.hasNext()) {
                        Double currentTemperature = dataListIterator.next();
                        updateTemperatureDescriptor = new TemperatureDescriptor(
                                currentTemperature);
                    notifyUpdate(updateTemperatureDescriptor);
                    }else{
                        logger.info("End of the document");
                    }
                }
                }, resourceConfiguration.getStartUpDelayMs(), resourceConfiguration.getTelemetryUpdateTimeMs());
        }catch (Exception e){
            logger.error("Error executing periodic resource value {}", e.getLocalizedMessage());
        }
    }

    @Override
    public TemperatureDescriptor loadUpdateValue() {
        return this.updateTemperatureDescriptor;
    }

    /*public static void main(String[] args) {
        TemperatureSensorResource temperatureSensorResource = new TemperatureSensorResource();
        temperatureSensorResource.addDataListener(new ResourceDataListener<TemperatureDescriptor>() {
            @Override
            public void onDataChanged(DTObjectResource<TemperatureDescriptor> resource, TemperatureDescriptor updatedValue) {
                if(resource != null && updatedValue != null)
                    logger.info("Device: {} --> New Value Recived: {}", resource.getId(), updatedValue);
                else
                    logger.error("onDataChangedCallback --> Null Resource or Update Value");

            }
        });
    }*/
}
