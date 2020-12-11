package coap.sever;

import coap.resource.PressureResource;
import coap.resource.TemperatureResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class CoapServerProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(CoapServerProcess.class);

    public CoapServerProcess() {
        super();

        //TODO looking the deviceID
        String deviceId = String.format("dt:%s", UUID.randomUUID().toString());

        TemperatureResource temperatureResource = new TemperatureResource(deviceId, "temperature");
        PressureResource pressureResource = new PressureResource(deviceId, "pressure");

        logger.info("Defining and adding resources ...");

        this.add(temperatureResource);
        this.add(pressureResource);
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
