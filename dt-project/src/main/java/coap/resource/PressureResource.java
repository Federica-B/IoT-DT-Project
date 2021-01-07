package coap.resource;

import coap.sever.configurationCoap.CoapSmartObjectConfiguration;
import coap.utils.CoreInterfaces;
import coap.utils.SenMLPack;
import coap.utils.SenMLRecord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import mqtt.message.TelemetryMessage;
import mqtt.model.PressureDescpritor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import commons.resources.DTObjectResource;
import commons.resources.ResourceDataListener;
import commons.resources.sensors.PressureSensorResource;

import java.util.*;



public class PressureResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(TemperatureResource.class);

    private static final Number SENSOR_VERSION = 0.1;

    private static final String OBJECT_TITLE = "PressureSensor";

    public final String RESOURCE_LINK_NAME = "pressure";

    //TODO write better RESOURCE_TYPE
    private static final String RESOURCE_TYPE = "dt.sensor.pressure";


    // TODO converting bar values in Pa because SenML does not support bar but Pa.
    private Number pressure;

    // TODO converting bar values in Pa because SenML does not support bar but Pa.
    private static final String PRESSURE_UNIT = "Pa";

    private static final long BAR_TO_PASCAL = 100000;

    private ObjectMapper objectMapper;

    private CoapSmartObjectConfiguration coapSmartObjectConfiguration;



    public PressureResource(CoapSmartObjectConfiguration coapSmartObjectConfiguration, String name, PressureSensorResource pressureSensorResource) {
        super(name);

        this.coapSmartObjectConfiguration = coapSmartObjectConfiguration;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        setObservable(true);

        //notification type CON
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable(); //set obs in the link-format but can also not be obs with this value

        //resource attributes
        //TODO better write RESOURCE_TYPE
        getAttributes().addAttribute("rt", RESOURCE_TYPE);
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());

        seyUpDataListener(pressureSensorResource);
    }

    private void seyUpDataListener(PressureSensorResource pressureSensorResource) {
        pressureSensorResource.addDataListener(new ResourceDataListener<PressureDescpritor>() {
            @Override
            //I don't know if this can be a thing, because there is the temperatureDescpritor that I use in MQTT
            public void onDataChanged(DTObjectResource<PressureDescpritor> resource, PressureDescpritor updatedValue) {
                try{
                    pressure = updatedValue.getValue();
                    //It notified the observable
                    changed();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private Optional<String> getJsonSenmlResponse(){
        try{
            SenMLPack senMLPack = new SenMLPack();
            SenMLRecord senMLRecord = new SenMLRecord();

            senMLRecord.setBaseName(String.format("%s:%s", this.coapSmartObjectConfiguration.getDeviceID(), this.getName()));
            senMLRecord.setVersion(SENSOR_VERSION);
            senMLRecord.setUnit(PRESSURE_UNIT);
            senMLRecord.setValue(pressure);
            senMLRecord.setTime(System.currentTimeMillis());

            //Pack
            senMLPack.add(senMLRecord);

            return Optional.of(this.objectMapper.writeValueAsString(senMLPack));
        }catch (Exception e){
            logger.error("Error Generiting SenML Record : {}", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.setMaxAge(coapSmartObjectConfiguration.getResourceMaxAgeSecond());

        if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){
            try {
                TelemetryMessage<?> telemetryMessage = new TelemetryMessage(
                        RESOURCE_TYPE, pressure, PressureDescpritor.FILE_TEMPERATURE_PROVIDER);
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
        sb.append(pressure.toString()).append("}");
        return Optional.of(sb.toString());
    }
}
