package io.wispforest.lmft;

import com.google.common.collect.Streams;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.slf4j.IMarkerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Mod(LMFTCommon.MODID)
public class LMFTForge {
    public LMFTForge() {
        try {
            var factory = LoggerFactory.getILoggerFactory();

            if (factory instanceof NOPLoggerFactory) {
                LMFTCommon.LOGGER = new Log4jLogger((ExtendedLogger) LogManager.getLogger(), LMFTCommon.class.getName());
            }
        } catch (Exception e) {
            System.out.println("Unable to init LMFT logger as an exception has occured");
            System.out.println(e.toString());
        }
    }
}

// Based on Apache's log4j slf4j18 interface module
class Log4jLogger implements LocationAwareLogger, Serializable {

    private final Logger logger;
    private final String name;

    public Log4jLogger(final ExtendedLogger logger, final String name) {
        this.logger = logger;
        this.name = name;
    }

    @Override public String getName() { return name; }
    @Override public void trace(final String format) { logger.trace(format); }
    @Override public void trace(final String format, final Object o) { logger.trace(format, o); }
    @Override public void trace(final String format, final Object arg1, final Object arg2) { logger.trace(format, arg1, arg2); }
    @Override public void trace(final String format, final Object... args) { logger.trace(format, args); }
    @Override public void trace(final String format, final Throwable t) { logger.trace(format, t); }
    @Override public boolean isTraceEnabled() { return logger.isTraceEnabled(); }
    @Override public boolean isTraceEnabled(final Marker marker) { return logger.isTraceEnabled(getMarker(marker)); }
    @Override public void trace(final Marker marker, final String s) { logger.trace(getMarker(marker), s); }
    @Override public void trace(final Marker marker, final String s, final Object o) { logger.trace(getMarker(marker), s, o); }
    @Override public void trace(final Marker marker, final String s, final Object o, final Object o1) { logger.trace(getMarker(marker), s, o, o1); }
    @Override public void trace(final Marker marker, final String s, final Object... objects) { logger.trace(getMarker(marker), s, objects); }
    @Override public void trace(final Marker marker, final String s, final Throwable throwable) { logger.trace(getMarker(marker), s, throwable); }
    @Override public void debug(final String format) { logger.debug(format); }
    @Override public void debug(final String format, final Object o) { logger.debug(format, o); }
    @Override public void debug(final String format, final Object arg1, final Object arg2) { logger.debug(format, arg1, arg2); }
    @Override public void debug(final String format, final Object... args) { logger.debug(format, args); }
    @Override public void debug(final String format, final Throwable t) { logger.debug(format, t); }
    @Override public boolean isDebugEnabled() { return logger.isDebugEnabled(); }
    @Override public boolean isDebugEnabled(final Marker marker) { return logger.isDebugEnabled(getMarker(marker)); }
    @Override public void debug(final Marker marker, final String s) { logger.debug(getMarker(marker), s); }
    @Override public void debug(final Marker marker, final String s, final Object o) { logger.debug(getMarker(marker), s, o); }
    @Override public void debug(final Marker marker, final String s, final Object o, final Object o1) { logger.debug(getMarker(marker), s, o, o1); }
    @Override public void debug(final Marker marker, final String s, final Object... objects) { logger.debug(getMarker(marker), s, objects); }
    @Override public void debug(final Marker marker, final String s, final Throwable throwable) { logger.debug(getMarker(marker), s, throwable); }
    @Override public void info(final String format) { logger.info(format); }
    @Override public void info(final String format, final Object o) { logger.info(format, o); }
    @Override public void info(final String format, final Object arg1, final Object arg2) { logger.info(format, arg1, arg2); }
    @Override public void info(final String format, final Object... args) { logger.info(format, args); }
    @Override public void info(final String format, final Throwable t) { logger.info(format, t); }
    @Override public boolean isInfoEnabled() { return logger.isInfoEnabled(); }
    @Override public boolean isInfoEnabled(final Marker marker) { return logger.isInfoEnabled(getMarker(marker)); }
    @Override public void info(final Marker marker, final String s) { logger.info(getMarker(marker), s); }
    @Override public void info(final Marker marker, final String s, final Object o) { logger.info(getMarker(marker), s, o); }
    @Override public void info(final Marker marker, final String s, final Object o, final Object o1) { logger.info(getMarker(marker), s, o, o1); }
    @Override public void info(final Marker marker, final String s, final Object... objects) { logger.info(getMarker(marker), s, objects); }
    @Override public void info(final Marker marker, final String s, final Throwable throwable) { logger.info(getMarker(marker), s, throwable); }
    @Override public void warn(final String format) { logger.warn(format); }
    @Override public void warn(final String format, final Object o) { logger.warn(format, o); }
    @Override public void warn(final String format, final Object arg1, final Object arg2) { logger.warn(format, arg1, arg2); }
    @Override public void warn(final String format, final Object... args) { logger.warn(format, args); }
    @Override public void warn(final String format, final Throwable t) { logger.warn(format, t); }
    @Override public boolean isWarnEnabled() { return logger.isWarnEnabled(); }
    @Override public boolean isWarnEnabled(final Marker marker) { return logger.isWarnEnabled(getMarker(marker)); }
    @Override public void warn(final Marker marker, final String s) { logger.warn(getMarker(marker), s); }
    @Override public void warn(final Marker marker, final String s, final Object o) { logger.warn(getMarker(marker), s, o); }
    @Override public void warn(final Marker marker, final String s, final Object o, final Object o1) { logger.warn(getMarker(marker), s, o, o1); }
    @Override public void warn(final Marker marker, final String s, final Object... objects) { logger.warn(getMarker(marker), s, objects); }
    @Override public void warn(final Marker marker, final String s, final Throwable throwable) { logger.warn(getMarker(marker), s, throwable); }
    @Override public void error(final String format) { logger.error(format); }
    @Override public void error(final String format, final Object o) { logger.error(format, o); }
    @Override public void error(final String format, final Object arg1, final Object arg2) { logger.error(format, arg1, arg2); }
    @Override public void error(final String format, final Object... args) { logger.error(format, args); }
    @Override public void error(final String format, final Throwable t) { logger.error(format, t); }
    @Override public boolean isErrorEnabled() { return logger.isErrorEnabled(); }
    @Override public boolean isErrorEnabled(final Marker marker) { return logger.isErrorEnabled(getMarker(marker)); }
    @Override public void error(final Marker marker, final String s) { logger.error(getMarker(marker), s); }
    @Override public void error(final Marker marker, final String s, final Object o) { logger.error(getMarker(marker), s, o); }
    @Override public void error(final Marker marker, final String s, final Object o, final Object o1) { logger.error(getMarker(marker), s, o, o1); }
    @Override public void error(final Marker marker, final String s, final Object... objects) { logger.error(getMarker(marker), s, objects); }
    @Override public void error(final Marker marker, final String s, final Throwable throwable) { logger.error(getMarker(marker), s, throwable); }

    @Override
    public void log(final Marker marker, final String fqcn, final int level, final String message, final Object[] params, Throwable throwable) {
        Level log4jLevel;

        if (level == TRACE_INT) log4jLevel = Level.TRACE;
        else if (level == DEBUG_INT) log4jLevel = Level.DEBUG;
        else if (level == INFO_INT) log4jLevel = Level.INFO;
        else if (level == WARN_INT) log4jLevel = Level.WARN;
        else log4jLevel = Level.ERROR;

        var log4jMarker = getMarker(marker);

        if (!logger.isEnabled(log4jLevel, log4jMarker)) return;

        Message msg;

        if (params == null) {
            msg = new SimpleMessage(message);
        } else {
            msg = new ParameterizedMessage(message, params, throwable);

            if (throwable != null) throwable = msg.getThrowable();
        }

        logger.log(log4jLevel, log4jMarker, msg, throwable);
    }

    private org.apache.logging.log4j.Marker getMarker(Marker marker) {
        if (marker == null) return null;
        if (!(marker instanceof ApacheMarkerToSLFJMarker)) marker = ApacheMarkerToSLFJMarkerFactory.INSTANCE.getMarker(marker);

        return ((ApacheMarkerToSLFJMarker) marker).marker;
    }
}

class ApacheMarkerToSLFJMarker implements Marker {

    final org.apache.logging.log4j.Marker marker;

    public ApacheMarkerToSLFJMarker(org.apache.logging.log4j.Marker marker) {
        this.marker = marker;
    }

    @Override
    public void add(final Marker marker) {
        if (marker == null) throw new IllegalArgumentException();

        var m = ApacheMarkerToSLFJMarkerFactory.INSTANCE.getMarker(marker.getName());

        if (!(m instanceof ApacheMarkerToSLFJMarker wrappedMarker)) return;

        this.marker.addParents(wrappedMarker.marker);
    }

    @Override
    public boolean contains(final Marker marker) {
        if (marker == null) throw new IllegalArgumentException();

        return this.marker.isInstanceOf(marker.getName());
    }

    @Override
    public boolean contains(final String s) {
        return s != null && this.marker.isInstanceOf(s);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ApacheMarkerToSLFJMarker other)) return false;

        return Objects.equals(marker, other.marker);
    }

    @Override
    public String getName() {
        return marker.getName();
    }

    @Override
    public boolean hasChildren() {
        return marker.hasParents();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(marker);
    }

    @Override
    public boolean hasReferences() {
        return marker.hasParents();
    }

    @Override
    public Iterator<Marker> iterator() {
        return Arrays.stream(this.marker.getParents())
            .map(m -> ApacheMarkerToSLFJMarkerFactory.INSTANCE.getMarker(m.getName()))
            .toList()
            .iterator();
    }

    @Override
    public boolean remove(final Marker marker) {
        return marker != null && this.marker.remove(MarkerManager.getMarker(marker.getName()));
    }
}

class ApacheMarkerToSLFJMarkerFactory implements IMarkerFactory {

    static final ApacheMarkerToSLFJMarkerFactory INSTANCE = new ApacheMarkerToSLFJMarkerFactory();

    private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<>();

    @Override
    public Marker getMarker(final String name) {
        return (exists(name)) ? markerMap.get(name) : addMarkerIfAbsent(name, MarkerManager.getMarker(name));
    }

    private Marker addMarkerIfAbsent(final String name, final org.apache.logging.log4j.Marker log4jMarker) {
        return markerMap.computeIfAbsent(name, k -> new ApacheMarkerToSLFJMarker(log4jMarker));
    }

    public Marker getMarker(final Marker marker) {
        return markerMap.computeIfAbsent(marker.getName(), s -> addMarkerIfAbsent(marker.getName(), convertMarker(marker, new ArrayList<>())));
    }

    private static org.apache.logging.log4j.Marker convertMarker(Marker original, Collection<Marker> markerStack) {
        var marker = MarkerManager.getMarker(original.getName());

        if (original.hasReferences()) {
            Streams.stream(original.iterator()).forEach(reference -> {
                if (markerStack.contains(reference)) {
                    System.out.println("Found a circular marker reference in Marker [" + reference.getName() + "], breaking loop.");

                    return;
                }

                markerStack.add(reference);
                marker.addParents(convertMarker(reference, markerStack));
            });
        }

        return marker;
    }

    @Override
    public boolean exists(final String name) {
        return markerMap.containsKey(name);
    }

    @Override public boolean detachMarker(final String name) { return false; }
    @Override public Marker getDetachedMarker(final String name) { return getMarker(name); }
}
