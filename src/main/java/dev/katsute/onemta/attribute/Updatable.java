package dev.katsute.onemta.attribute;

/**
 * Indicates that the object can refresh its' realtime data.
 *
 * @since 1.1.0
 * @version 1.1.0
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
