/*
 * Copyright (C) 2024 Katsute <https://github.com/Katsute>
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

package dev.katsute.onemta.railroad;

import dev.katsute.onemta.attribute.Direction;
import dev.katsute.onemta.attribute.Location;
import dev.katsute.onemta.types.*;

/**
 * Represents Long Island Railroad (LIRR) related objects.
 *
 * @see Route
 * @see Stop
 * @see Vehicle
 * @see Trip
 * @see TripStop
 * @see Alert
 * @since 1.0.0
 * @version 2.1.0
 * @author Katsute
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class LIRR {

    private LIRR(){ }

    /**
     * Represents a Long Island Railroad (LIRR) route.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Route extends TransitRoute<Integer,Vehicle,Alert> { }

    /**
     * Represents a Long Island Railroad (LIRR) stop.
     *
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Stop extends TransitStop<Integer,Vehicle,Alert> {

        /**
         * Returns the stop code for the station.
         *
         * @return stop code
         *
         * @since 1.0.0
         */
        public abstract String getStopCode();

        /**
         * Returns if the stop is wheelchair accessible.
         *
         * @return wheelchair accessible
         *
         * @since 1.0.0
         */
        public abstract Boolean hasWheelchairBoarding();

    }

    /**
     * Represents a Long Island Railroad (LIRR) vehicle.
     *
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends TransitVehicle<Integer,Route,Integer,Stop,String,Trip> implements Location {

        /**
         * Returns the vehicle bearing, 0 being North.
         *
         * @return bearing
         *
         * @since 1.0.0
         */
        public abstract Double getBearing();

        /**
         * Returns the current vehicle status.
         *
         * @return vehicle status
         *
         * @since 1.0.0
         */
        public abstract String getStatus();

    }

    /**
     * Represents a Long Island Railroad (LIRR) vehicle trip.
     *
     * @see TransitTrip
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Trip extends TransitTrip<Integer,Route,Vehicle,TripStop> implements Direction<RailroadDirection> {

        /**
         * Returns the schedule relationship.
         *
         * @return schedule relationship
         *
         * @since 1.2.0
         */
        public abstract String getScheduleRelationship();

    }

    /**
     * Represents a Long Island Railroad (LIRR) vehicle trip stop.
     *
     * @see Trip
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends TransitTripStop<Integer,Stop,Trip> {

        /**
         * Returns the stop sequence.
         *
         * @return stop sequence
         *
         * @since 2.0.0
         */
        public abstract Integer getStopSequence();

        /**
         * Returns the delay in seconds.
         *
         * @return delay
         *
         * @since 1.0.0
         */
        public abstract Integer getDelay();

        /**
         * Returns the schedule relationship.
         *
         * @return schedule relationship
         *
         * @since 2.0.0
         */
        public abstract String getScheduleRelationship();

        /**
         * Returns the track.
         *
         * @return track
         *
         * @since 1.0.0
         */
        public abstract String getTrack();

        /**
         * Returns the train status.
         *
         * @return train status
         *
         * @since 1.0.0
         */
        public abstract String getStatus();

    }

    /**
     * Represents a Long Island Railroad (LIRR) alert.
     *
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Alert extends TransitAlert<Integer,Route,Integer,Stop> { }

}