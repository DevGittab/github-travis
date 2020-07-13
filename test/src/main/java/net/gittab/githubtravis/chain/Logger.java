package net.gittab.githubtravis.chain;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Consumer;

/**
 * Logger.
 *
 * @author xiaohua zhou
 **/
@FunctionalInterface
public interface Logger {

    enum LogLevel{
        DEBUG,
        INFO,
        WARING,
        ERROR,
        FUNCTION_MESSAGE,
        FUNCTION_ERROR;

        public static LogLevel[] all(){
            return values();
        }

    }

    void message(String msg, LogLevel logLevel);

    default Logger appendNext(Logger nextLogger){
        return (msg, level) -> {
            message(msg, level);
            nextLogger.message(msg, level);
        };
    }

    static Logger logger(LogLevel[] levels, Consumer<String> writeMessage){
        EnumSet<LogLevel> set = EnumSet.copyOf(Arrays.asList(levels));
        return (msg, level) -> {
            if(set.contains(level)){
                writeMessage.accept(msg);
            }
        };
    }

    static Logger consoleLogger(LogLevel... levels){
        return logger(levels, System.out::println);
    }

    static Logger emailLogger(LogLevel... levels){
        return logger(levels, System.out::println);
    }

    static void main(String[] args) {
        Logger logger = consoleLogger(LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARING, LogLevel.ERROR, LogLevel.FUNCTION_MESSAGE)
                .appendNext(emailLogger(LogLevel.FUNCTION_MESSAGE, LogLevel.FUNCTION_ERROR));
//        logger.message("debug", LogLevel.DEBUG);
//
//        logger.message("error", LogLevel.ERROR);

        logger.message("function message", LogLevel.FUNCTION_MESSAGE);
    }
}
