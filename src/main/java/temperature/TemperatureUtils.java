package temperature;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(TemperatureUtils.class);


    private static final String datePattern = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}";
    private static List<Temperature> temperatures = null;

    public static boolean validateDate(String date) {
        return date.matches(datePattern);
    }

    //get temperature from csv
    public static List<Temperature> getTemperature(String date) {
        if (temperatures == null)
            initTempList();

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
            DateTime dateTime = formatter.parseDateTime(date);

            //return all the values for the given date
            return Lists.newArrayList(Collections2.filter(temperatures,
                    input -> input.getDateTime().isAfter(dateTime)
                            && input.getDateTime().isBefore(dateTime.plusDays(1))
                            || input.getDateTime().isEqual(dateTime)));
        } catch (Exception e) {
            //if can't parse our date just swallow the error and return an empty list because we dont' have data for it
            return new ArrayList<>();
        }
    }

    private static void initTempList() {
        try {
            temperatures = new ArrayList<>();

            FileReader filereader = new FileReader(new File(TemperatureUtils.class.getClassLoader().getResource("temp.csv").getFile()));
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .withSkipLines(1)
                    .build();

            List<String[]> allData = csvReader.readAll();

            //parse data
            for (String[] row : allData) {
                Temperature t = new Temperature(row[0], row[1], row[2]);
                temperatures.add(t);
            }
        } catch (Exception ex) {
            temperatures = null;
            LOG.error("cannot read csv file", ex);
        }
    }

}
