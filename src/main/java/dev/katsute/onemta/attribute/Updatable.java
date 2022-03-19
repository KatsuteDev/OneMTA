package dev.katsute.onemta.attribute;

/**
 * Indicates that the object can refresh its' realtime data.
 *
 * @param <T> type
 *
 * @since 1.1.0
 * @version 1.0.0
 * @author Katsute
 */
public interface Updatable {

    /**
     * Refreshes realtime data such as location, vehicles, trips, and alerts.
     *
     * @since 1.1.0
     */
    void refresh();

}
