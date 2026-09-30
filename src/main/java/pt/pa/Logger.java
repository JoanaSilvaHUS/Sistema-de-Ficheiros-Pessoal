package pt.pa;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Logger class provides a simple logging mechanism for recording messages to a log file.
 * It supports different log levels, allowing selective logging based on the specified level.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class Logger {

    // Paths for different log files based on log levels
    private static final String LOG_FILE_ALL = "logs_all.txt";
    private static final String LOG_FILE_FILES = "logs_files.txt";
    private static final String LOG_FILE_DEL = "logs_del.txt";

    // Current log file path
    private static String logFile;

    // Current log level
    private LogLevel logLevel;

    /**
     * Enumeration representing different log levels.
     */
    public enum LogLevel {
        /** Log all operations. */
        ALL,

        /** Log operations related to file manipulation. */
        FILES,

        /** Log deletion operations. */
        DEL
    }

    /**
     * Gets the current log file path.
     *
     * @return The current log file path.
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * Sets the log file path based on the log level.
     *
     * @param level The log level to set.
     */
    private static void setLogFile(LogLevel level) {
        switch (level) {
            case ALL:
                logFile = LOG_FILE_ALL;
                break;
            case FILES:
                logFile = LOG_FILE_FILES;
                break;
            case DEL:
                logFile = LOG_FILE_DEL;
                break;
        }
    }

    /**
     * Gets the current log level.
     *
     * @return The current log level.
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the log level for the logger.
     *
     * @param level The log level to set.
     */
    public void setLogLevel(LogLevel level) {
        this.logLevel = level;
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param message The message to log.
     * @param level   The log level for the message.
     */
    public static void log(String message, LogLevel level) {
        setLogFile(level);

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(timestamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
