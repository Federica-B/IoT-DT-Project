package sharedProtocolsClass;

import coap.sever.CoapServerProcess;
import mqtt.process.SmartObjectProcess;
import sharedProtocolsClass.resource.sensors.TemperatureSensorResource;

import java.util.UUID;

public class SmartObjectCommunicationProcess {

    /*public static void main(String[] args) {

        String smartObjectId = UUID.randomUUID().toString();
        TemperatureSensorResource temperatureSensorResource = new TemperatureSensorResource();

        CoapServerProcess coapServerProcess = new CoapServerProcess(smartObjectId, temperatureSensorResource);
        coapServerProcess.start();

        SmartObjectProcess smartObjectProcess = new SmartObjectProcess();
        smartObjectProcess.run(temperatureSensorResource);

    }*/
}
