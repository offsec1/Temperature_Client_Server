package utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AbstractUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractUtils.class);

    public static Map<String, String> SETTINGS = new HashMap<>();

    static {
        try {
            Properties prop = new Properties();
            // Load property file
            prop.load(AbstractUtils.class.getClassLoader().getResourceAsStream("applicationSettings.properties"));
            SETTINGS = Maps.fromProperties(prop);
            prop.clear();
            LOG.debug("Property loaded ");
        } catch (IOException ex) {
            LOG.error("Property file could not be loaded", ex);
        }
    }
}
