/*
 * Copyright (C) 2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package dev.katsute.onemta.bus;

import dev.katsute.onemta.attribute.*;
import dev.katsute.onemta.types.*;

import java.util.Date;

/**
 * Represents bus related objects.
 *
 * @see Route
 * @see Stop
 * @see Vehicle
 * @see Trip
 * @see TripStop
 * @see Alert
 * @since 1.0.0
 * @version 1.0.1
 * @author Katsute
 */
public abstract class Bus {

    private Bus(){ }

    /**
     * Represents a bus route.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Route extends TransitRoute<String,Vehicle,Alert> implements RouteShortName, RouteDescription, BusRouteTypes { }

    /**
     * Represents a bus stop.
     *
     * @since 1.0.0
     * @version 1.0.1
     * @author Katsute
     */
    public abstract static class Stop extends TransitStop<Integer,Vehicle,Alert> { }

    /**
     * Represents a bus vehicle.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends TransitVehicle<Route,Stop,Trip,Integer,String,Integer> implements Bearing, Location, Direction<BusDirection>, BusRouteTypes {

        /**
         * Returns the origin stop code.
         *
         * @return origin stop code
         *
         * @see #getOriginStop()
         * @since 1.0.0
         */
        public abstract Integer getOriginStopCode();

        /**
         * Returns the origin stop.
         *
         * @return origin stop
         *
         * @see #getOriginStopCode()
         * @since 1.0.0
         */
        public abstract Stop getOriginStop();

        /**
         * Returns the destination name.
         *
         * @return destination name
         *
         * @since 1.0.0
         */
        public abstract String getDestinationName();

        /**
         * Returns the progress rate.
         *
         * @return progress rate
         *
         * @since 1.0.0
         */
        public abstract String getProgressRate();

        /**
         * Returns the progress status.
         *
         * @return progress status
         *
         * @since 1.0.0
         */
        public abstract String[] getProgressStatus();

        /**
         * Returns the aimed arrival time.
         *
         * @return aimed arrival time
         *
         * @see Date
         * @see #getAimedArrivalTimeEpochMillis()
         * @since 1.0.0
         */
        public abstract Date getAimedArrivalTime();

        /**
         * Returns the aimed arrival time as milliseconds since epoch.
         *
         * @return aimed arrival time
         *
         * @see #getAimedArrivalTime()
         * @since 1.0.0
         */
        public abstract Long getAimedArrivalTimeEpochMillis();

        /**
         * Returns the expected arrival time.
         *
         * @return expected arrival time
         *
         * @see Date
         * @see #getExpectedArrivalTimeEpochMillis()
         * @since 1.0.0
         */
        public abstract Date getExpectedArrivalTime();

        /**
         * Returns the expected arrival time as milliseconds since epoch.
         *
         * @return expected arrival time
         *
         * @see #getExpectedArrivalTime()
         * @since 1.0.0
         */
        public abstract Long getExpectedArrivalTimeEpochMillis();

        /**
         * Returns the expected departure time.
         *
         * @return expected departure time
         *
         * @see Date
         * @see #getExpectedDepartureTimeEpochMillis()
         * @since 1.0.0
         */
        public abstract Date getExpectedDepartureTime();

        /**
         * Returns the expected departure time as milliseconds since epoch.
         *
         * @return expected departure time
         *
         * @see #getExpectedDepartureTime()
         * @since 1.0.0
         */
        public abstract Long getExpectedDepartureTimeEpochMillis();

        /**
         * Returns the arrival proximity text.
         *
         * @return arrival proximity text
         */
        public abstract String getArrivalProximityText();

        /**
         * Returns the distance from the stop in meters.
         *
         * @return distance from stop
         *
         * @since 1.0.0
         */
        public abstract Integer getDistanceFromStop();

        /**
         * Returns number of stops between vehicle stop and requested stop.
         *
         * @return stops away
         *
         * @since 1.0.0
         */
        public abstract Integer getStopsAway();

        /**
         * Returns the stop name
         *
         * @return stop name
         *
         * @since 1.0.0
         */
        public abstract String getStopName();

    }

    /**
     * Represents a bus vehicle trip.
     *
     * @see TripStop
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Trip extends TransitTrip<Vehicle,Route,TripStop> { }

    /**
     * Represents a bus vehicle trip stop.
     *
     * @see Trip
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends TransitTripStop<Stop,Trip,Integer> {

        /**
         * Returns the expected arrival time.
         *
         * @return expected arrival time
         *
         * @see Date
         * @see #getExpectedArrivalTimeEpochMillis()
         * @since 1.0.0
         */
        public abstract Date getExpectedArrivalTime();

        /**
         * Returns the expected arrival time as milliseconds since epoch.
         *
         * @return expected arrival time
         *
         * @see #getExpectedArrivalTime()
         * @since 1.0.0
         */
        public abstract Long getExpectedArrivalTimeEpochMillis();

        /**
         * Returns the arrival proximity text.
         *
         * @return arrival proximity text
         */
        public abstract String getArrivalProximityText();

        /**
         * Returns the distance from the stop in meters.
         *
         * @return distance from stop
         *
         * @since 1.0.0
         */
        public abstract Integer getDistanceFromStop();

        /**
         * Returns number of stops between vehicle stop and requested stop.
         *
         * @return stops away
         *
         * @since 1.0.0
         */
        public abstract Integer getStopsAway();

        /**
         * Returns the stop name
         *
         * @return stop name
         *
         * @since 1.0.0
         */
        public abstract String getStopName();

    }

    /**
     * Represents a bus alert.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Alert extends TransitAlert<String,Route,Integer,Stop> { }

}
