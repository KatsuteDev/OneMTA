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

package dev.katsute.onemta.subway;

import dev.katsute.onemta.attribute.*;
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
 * @version 1.1.0
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
    public abstract static class Route extends TransitRoute<String,Vehicle,Alert> implements RouteShortName, RouteDescription { }

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
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends GTFSVehicle<Route,Stop,Trip,String,String> {

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
     * @see TripStop
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Trip extends GTFSTransitTrip<Vehicle,Route,TripStop> implements Direction<SubwayDirection> { }

    /**
     * Represents a subway vehicle trip stop.
     *
     * @since Trip
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends GTFSTransitTripStop<Stop,Trip,String> {

        /**
         * Returns the actual track that a subway train arrived at.
         *
         * @return actual track
         *
         * @since 1.0.0
         */
        public abstract String getActualTrack();

    }

    /**
     * Represents a subway alert.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Alert extends TransitAlert<String,Route,String,Stop> { }

}
