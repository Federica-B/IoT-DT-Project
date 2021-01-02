package mqtt.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mqtt.configurationMqtt.MqttSmartObjectConfiguration;
import mqtt.message.TelemetryMessage;
import mqtt.model.TemperatureDescriptor;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.ResourceDataListener;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MqttSmartObject {

    private static final Logger logger = LoggerFactory.getLogger(MqttSmartObject.class);

    //TODO eventually add command, event and control

    private ObjectMapper mapper;

    private IMqttClient mqttClient;

    private Map<String, DTObjectResource<?>> resourceMap;

    private static MqttSmartObjectConfiguration mqttSmartObjectConfiguration;

    public MqttSmartObject() {
        this.mapper = new ObjectMapper();
    }

    public void init(MqttSmartObjectConfiguration mqttSmartObjectConfiguration, IMqttClient mqttClient, Map<String, DTObjectResource<?>> resourceMap){
        this.mqttSmartObjectConfiguration = mqttSmartObjectConfiguration;
        this.mqttClient = mqttClient;
        this.resourceMap = resourceMap;

        logger.info("Smart Object correctly created! Resource Number: {}",resourceMap.keySet().size() );

    }


    public void start(){
        try {
            if(this.mqttClient != null &&
            this.mqttSmartObjectConfiguration.getDeviceID() != null &&
            this.mqttSmartObjectConfiguration.getDeviceID().length() > 0 &&
            this.resourceMap != null &&
            this.resourceMap.keySet().size() > 0){
                logger.info("Starting Smart Object Emulator ...");

                //TODO It can implement also a control channel, in this case there are only telemetry data
                registerToAvailableResource();
            }
        }catch (Exception e){
            logger.error("Error Starting the Smart Object! Message: {}",e.getLocalizedMessage());
        }
        }

    private void registerToAvailableResource() {
        this.resourceMap.entrySet().forEach(resourceEntry ->{

            if(resourceEntry.getKey() != null && resourceEntry.getValue() != null){
                DTObjectResource dtObjectResource = resourceEntry.getValue();

                logger.info("Registering to Resource {} (id: {}) notifications ...",dtObjectResource.getType(), dtObjectResource.getId());

                if(dtObjectResource.getType().equals(TemperatureSensorResource.RESOURCE_TYPE)){
                    TemperatureSensorResource temperatureSensorResource = (TemperatureSensorResource) dtObjectResource;
                    temperatureSensorResource.addDataListener(new ResourceDataListener<TemperatureDescriptor>() {
                        @Override
                        public void onDataChanged(DTObjectResource<TemperatureDescriptor> resource, TemperatureDescriptor updatedValue) {
                            try{
                                //I pass the value of the provider because i now before hand what resurce is.
                                publishTelemetryData(
                                        String.format("%s/%s/%s/%s", mqttSmartObjectConfiguration.getBasicTopic(),
                                                mqttSmartObjectConfiguration.getDeviceID(),
                                                mqttSmartObjectConfiguration.getTelemetryTopic(),
                                                resourceEntry.getKey()),
                                        new TelemetryMessage<>(dtObjectResource.getType(), updatedValue.getValue(), TemperatureDescriptor.FILE_TEMPERATURE_PROVIDER));

                            }catch (MqttException | JsonProcessingException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    private void publishTelemetryData(String topic, TelemetryMessage<?> telemetryMessage) throws MqttException, JsonProcessingException{

        logger.info("Sending to topic: {} -> Data: {}", topic, telemetryMessage);

        if(mqttClient != null &&
                this.mqttClient.isConnected() &&
                telemetryMessage != null &&
                topic != null){

            String messagePayload = mapper.writeValueAsString(telemetryMessage);

            MqttMessage mqttMessage = new MqttMessage(messagePayload.getBytes());
            mqttMessage.setQos(mqttSmartObjectConfiguration.getMqttOutgoingClientQoS());
            mqttClient.publish(topic, mqttMessage);
            logger.info("Data Correctly Publish to topic: {}", topic);

        }else{
            logger.error("Error: Topic or Msg = Null or MQTT CLient is not Connected !");
        }
    }
}

