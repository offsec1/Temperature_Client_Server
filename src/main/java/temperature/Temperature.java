package temperature;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Temperature {

    protected static final Logger LOG = LoggerFactory.getLogger(Temperature.class);


    private float temperature;
    private DateTime dateTime;

    public Temperature(String date, String time, String temperature) {
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
            this.dateTime = formatter.parseDateTime(date + " " + time);
            this.temperature = Float.parseFloat(temperature);
        } catch (Exception ex) {
            LOG.error("creating temperature failed", ex);
        }
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return dateTime.hourOfDay().getAsString() + " o'clock : " + temperature;
    }
}
