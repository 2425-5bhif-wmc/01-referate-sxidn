package at.htl.leoenergy.mqtt;

import at.htl.leoenergy.entity.Co2Value;
import at.htl.leoenergy.entity.SensorValue;
import at.htl.leoenergy.influxdb.InfluxDbRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MqttReceiver {
   @Inject
    InfluxDbRepository influxDbRepository;
    @Inject
    Logger log;
   public void insertMeasurement(SensorValue sensorValue){
       influxDbRepository.insertMeasurementFromJSON(sensorValue);
   }

    @Incoming("leoenergy")
    public void receive(byte[] byteArray) {
       log.infof("Received measurement from leoenergy topic mqtt: %s", byteArray.length);

        String msg = new String(byteArray);
        try {
            SensorValue sensorValue = SensorValue.fromJson(msg);
            insertMeasurement(sensorValue);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }

}
