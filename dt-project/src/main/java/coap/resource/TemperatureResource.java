package coap.resource;

import coap.sever.configurationCoap.CoapSmartObjectConfiguration;
import coap.utils.CoreInterfaces;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mqtt.message.TelemetryMessage;
import mqtt.model.TemperatureDescriptor;
import sharedProtocolsClass.resource.DTObjectResource;
import sharedProtocolsClass.resource.ResourceDataListener;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class TemperatureResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(TemperatureResource.class);

    //Cel	degrees Celsius     float	RFC-AAAA -- Number in JASON / double in XML
    private Number temperature;

    private final String RESOURCE_TYPE = "dt.sensor.temperature";

    private static final Number SENSOR_VERSION = 0.1;

    private static final String OBJECT_TITLE = "TemperatureSensor";

    private String TEMPERATURE_UNIT = "Cel";

    private ObjectMapper objectMapper;

   private CoapSmartObjectConfiguration coapSmartObjectConfiguration;


    public TemperatureResource(CoapSmartObjectConfiguration coapSmartObjectConfiguration, String name, TemperatureSensorResource temperatureSensorResource) {
        super(name);

        this.coapSmartObjectConfiguration = coapSmartObjectConfiguration;

        //Ignore Null Field
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //set Observable
        setObservable(true);
        //notification type CON
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable(); //set obs in the link-format but can also not be obs with this value

        //resource attributes
        //TODO better write RESOURCE_TYPE
        getAttributes().addAttribute("rt", RESOURCE_TYPE);
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());

        seyUpDataListener(temperatureSensorResource);
    }

    private void seyUpDataListener(TemperatureSensorResource temperatureSensorResource) {
        temperatureSensorResource.addDataListener(new ResourceDataListener<TemperatureDescriptor>() {
            @Override
            //I don't know if this can be a thing, because there is the temperatureDescpritor that I use in MQTT
            public void onDataChanged(DTObjectResource<TemperatureDescriptor> resource, TemperatureDescriptor updatedValue) {
                try{
                    temperature = updatedValue.getValue();
                    //It notified the observable
                    changed();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /*private Optional<String> getJsonSenmlResponse(){
        try{
            SenMLPack senMLPack = new SenMLPack();
            SenMLRecord senMLRecord = new SenMLRecord();

            senMLRecord.setBaseName(String.format("%s:%s", this.deviceId, this.getName()));
            senMLRecord.setVersion(SENSOR_VERSION);
            senMLRecord.setUnit(TEMPERATURE_UNIT);
            senMLRecord.setValue(temperature);
            senMLRecord.setTime(System.currentTimeMillis());

            //Pack
            senMLPack.add(senMLRecord);

            return Optional.of(this.objectMapper.writeValueAsString(senMLPack));
        }catch (Exception e){
            logger.error("Error Generiting SenML Record : {}", e.getLocalizedMessage());
            return Optional.empty();
        }
    }*/


    //Dont have to use JSON SenML but just a proprietary fromat

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.setMaxAge(coapSmartObjectConfiguration.getResourceMaxAgeSecond());

        //two type of response: JSON + SenML and plain
        //code senml-json  110 - json 50 - plain 0
        if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){
            try {
                TelemetryMessage<?> telemetryMessage = new TelemetryMessage(
                        RESOURCE_TYPE, temperature, TemperatureDescriptor.FILE_TEMPERATURE_PROVIDER);
                if (telemetryMessage != null) {

                    Optional<String> jsonResponse = Optional.ofNullable(objectMapper.writeValueAsString(telemetryMessage));

                    if (jsonResponse.isPresent())
                        exchange.respond(CoAP.ResponseCode.CONTENT, jsonResponse.get(), exchange.getRequestOptions().getAccept());
                }
                else{ exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);}
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{

            Optional<String> textPlainResponse = getTextPlainResponse();

            if(textPlainResponse.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, textPlainResponse.get(), exchange.getRequestOptions().getAccept());
            else
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);

            }

    }


    private Optional<String> getTextPlainResponse() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append(System.currentTimeMillis()).append(", ");
        sb.append(temperature.toString()).append("}");
        return Optional.of(sb.toString());
    }

}
