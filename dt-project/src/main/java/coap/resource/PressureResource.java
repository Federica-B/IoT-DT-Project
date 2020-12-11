package coap.resource;

import coap.utils.CoreInterfaces;
import coap.utils.SenMLPack;
import coap.utils.SenMLRecord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


//TODO better if the configurations value are read from a configuration file!
public class PressureResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(TemperatureResource.class);

    private static final Number SENSOR_VERSION = 0.1;

    private static final String OBJECT_TITLE = "PressureSensor";

    //TODO write better RESOURCE_TYPE
    private static final String RESOURCE_TYPE = "dt.sensor.pressure";

    //TODO better configurationd of this values
    private static final long SENSOR_UPDATE_TIME_MS = 1000; //1Hz for update

    private static final int RESOURCE_MAX_AGE_SECONDS = 1;

    // TODO converting bar values in Pa because SenML does not support bar but Pa.
    private Number pressure;

    private String deviceId;

    // TODO converting bar values in Pa because SenML does not support bar but Pa.
    private static final String PRESSURE_UNIT = "Pa";

    private static final long BAR_TO_PASCAL = 100000;

    private ObjectMapper objectMapper;

    private List<Double> pressureList = new ArrayList<Double>();

    private ListIterator<Double> pressureListIterator;

    private static final String DIRECTORY_DATA = "data";

    private static final String[] TEMPERATURE_FILES_NAME = {"PS1.txt", "PS2.txt"};


    public PressureResource(String deviceId, String name) {
        super(name);

        init();

        this.deviceId = deviceId;

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

        //periodic update
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(pressureListIterator.hasNext()) {
                    pressure = pressureListIterator.next();
                    //notification that the value has changed
                    changed();
                }else{
                    logger.info("End of the document");
                }
            }
        }, 0, SENSOR_UPDATE_TIME_MS);

    }

    private void init() {
        try{

            for(String s : TEMPERATURE_FILES_NAME){
                BufferedReader cvsReader = new BufferedReader(new FileReader(DIRECTORY_DATA+ "/"+ s));
                String row;
                while ((row = cvsReader.readLine()) != null){
                    String [] line = row.split("\t");
                    for (String st : line) {
                        double convertedValue = Double.parseDouble(st) * BAR_TO_PASCAL;
                        pressureList.add(convertedValue);
                    }
                }
                logger.info("Temperature File correctly loaded! Size: {}", this.pressureList.size());

                this.pressureListIterator = this.pressureList.listIterator();
            }

        }catch(Exception e){
            logger.error("Error init Resource Object! Msg: {}", e.getLocalizedMessage());
        }

    }

    private Optional<String> getJsonSenmlResponse(){
        try{
            SenMLPack senMLPack = new SenMLPack();
            SenMLRecord senMLRecord = new SenMLRecord();

            senMLRecord.setBaseName(String.format("%s:%s", this.deviceId, this.getName()));
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
        exchange.setMaxAge(RESOURCE_MAX_AGE_SECONDS);

        //two type of response: JSON + SenML and plain
        //code senml-json  110 - json 50 - plain 0
        if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){
            Optional<String> senmlPayload = getJsonSenmlResponse();

            if(senmlPayload.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
            else
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }else{
            if(pressure != null)
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(pressure),MediaTypeRegistry.TEXT_PLAIN);
            else
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);

        }
    }
}
