/*
 * Copyright (C) 2023 Katsute <https://github.com/Katsute>
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

import dev.katsute.onemta.attribute.Direction;
import dev.katsute.onemta.attribute.Location;
import dev.katsute.onemta.types.*;

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
 * @version 2.0.0
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
    public abstract static class Route extends TransitRoute<String,Vehicle,Alert> implements BusRouteTypes {

        /**
         * Returns the short route name.
         *
         * @return route short name
         *
         * @since 1.0.0
         */
        public abstract String getRouteShortName();

        /**
         * Returns the route description.
         *
         * @return route description
         *
         * @since 1.0.0
         */
        public abstract String getRouteDescription();

    }

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
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends TransitVehicle<Route,Stop,Trip,Integer,String,Integer> implements Location, Direction<BusDirection>, BusRouteTypes {

        /**
         * Returns the vehicle bearing, 0 being North.
         *
         * @return bearing
         *
         * @since 1.0.0
         */
        public abstract Double getBearing();

        /**
         * Returns the estimated passenger count.
         *
         * @return passenger count
         *
         * @since 2.0.0
         */
        public abstract Integer getPassengerCount();

    }

    /**
     * Represents a bus vehicle trip.
     *
     * @see TransitTrip
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Trip extends TransitTrip<Vehicle,Route,TripStop> {

        /**
         * Returns the delay from the schedule. Negative numbers implies ahead of schedule.
         *
         * @return delay in seconds
         *
         * @since 2.0.0
         */
        public abstract Integer getDelay();

    }

    /**
     * Represents a bus vehicle trip stop.
     *
     * @see TransitTripStop
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends TransitTripStop<Stop,Trip,Integer> {

        /**
         * Returns the stop sequence.
         *
         * @return stop sequence
         *
         * @since 2.0.0
         */
        public abstract Integer getStopSequence();

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
