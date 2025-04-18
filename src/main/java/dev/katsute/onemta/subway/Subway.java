/*
 * Copyright (C) 2025 Katsute <https://github.com/Katsute>
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

package dev.katsute.onemta.subway;

import dev.katsute.onemta.attribute.Direction;
import dev.katsute.onemta.types.*;

/**
 * Represents subway related objects.
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
public abstract class Subway {

    private Subway(){ }

    /**
     * Represents a subway route.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Route extends TransitRoute<String,Vehicle,Alert> {

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
     * Represents a subway stop.
     *
     * @since 1.0.0
     * @version 1.1.0
     * @author Katsute
     */
    public abstract static class Stop extends TransitStop<String,Vehicle,Alert> implements Direction<SubwayDirection> {

        /**
         * Returns a list of transfers.
         *
         * @return transfers
         *
         * @since 1.1.0
         */
        public abstract Stop[] getTransfers();

    }

    /**
     * Represents a subway vehicle.
     *
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends TransitVehicle<String,Route,String,Stop,String,Trip> {

        /**
         * Returns if the trip is assigned to an actual train. <br>
         * If true, the vehicle is on a trip or is about to depart. <br>
         * If false, the vehicle has not yet been assigned a trip or is being taken out of service.
         *
         * @return assigned
         *
         * @since 2.0.0
         */
        public abstract Boolean isAssigned();

        /**
         * Returns if the train is rerouted.
         *
         * @return rerouted train
         *
         * @since 2.0.0
         */
        public abstract Boolean isRerouted();

        /**
         * Returns if the train is following a skip stop pattern.
         *
         * @return skip stop train
         *
         * @since 2.0.0
         */
        public abstract Boolean isSkipStop();

        /**
         * Returns if the train is a turn train (shortly lined service)
         *
         * @return turn train
         *
         * @since 2.0.0
         */
        public abstract Boolean isTurnTrain();

        /**
         * Returns the current vehicle status.
         *
         * @return vehicle status
         *
         * @since 1.0.0
         */
        public abstract String getStatus();

        /**
         * Returns the stop sequence.
         *
         * @return stop sequence
         *
         * @since 2.0.0
         */
        public abstract Integer getStopSequence();

        /**
         * Returns if the train is running express.
         *
         * @return express train
         *
         * @since 1.0.3
         */
        public abstract Boolean isExpress();

    }

    /**
     * Represents a subway vehicle trip.
     *
     * @see TransitTrip
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Trip extends TransitTrip<String,Route,Vehicle,TripStop> implements Direction<SubwayDirection> { }

    /**
     * Represents a subway vehicle trip stop.
     *
     * @see TransitTripStop
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends TransitTripStop<String,Stop,Trip> {

        /**
         * Returns the actual track.
         *
         * @return actual track
         *
         * @since 1.0.0
         */
        public abstract String getActualTrack();

        /**
         * Returns the scheduled track.
         *
         * @return track
         *
         * @since 1.0.0
         */
        public abstract String getTrack();

    }

    /**
     * Represents a subway alert.
     *
     * @since 1.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public abstract static class Alert extends TransitAlert<String,Route,String,Stop> { }

}