package framework;

import java.util.logging.Logger;

public class MessageLogger {
    static Logger logger = Logger.getLogger(MessageLogger.class.getName());

    public static void info(String log) {
        logger.info(log);
    }
    public static void error(String log) {
        logger.severe(log);
    }

    public static void warning(String log) {
        logger.warning(log);
    }
}
