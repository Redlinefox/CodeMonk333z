package Ref;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggingManager {
    public static void initialiseLogging() {
        String LOG_PROPERTIES_FILE = "Resources/log4j.properties";
        Logger log = Logger.getLogger(LoggingManager.class.getName());
        PropertyConfigurator.configure(LOG_PROPERTIES_FILE);
        log.info("Logging initialised");
    }
}
