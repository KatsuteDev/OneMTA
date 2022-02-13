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

package dev.katsute.onemta.railroad;

import dev.katsute.onemta.attribute.Location;
import dev.katsute.onemta.types.*;

/**
 * Represents Metro North Railroad (MNR) related objects.
 *
 * @see Route
 * @see Stop
 * @see Vehicle
 * @see Trip
 * @see TripStop
 * @see Alert
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class MNR {

    private MNR(){ }

    /**
     * Represents a Metro North Railroad (MNR) route.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Route extends TransitRoute<Integer,Vehicle,Alert> { }

    /**
     * Represents a Metro North Railroad (MNR) stop.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Stop extends RailroadStop<Vehicle,Alert> { }

    /**
     * Represents a Metro North Railroad (MNR) vehicle.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Vehicle extends GTFSVehicle<Route,Stop,Trip,Integer,Integer> implements Location { }

    /**
     * Represents a Metro North Railroad (MNR) vehicle trip.
     *
     * @see TripStop
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Trip extends GTFSTransitTrip<Vehicle,Route,TripStop> { }

    /**
     * Represents a Metro North Railroad (MNR) vehicle trip stop.
     *
     * @see Trip
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class TripStop extends RailroadTripStop<Stop,Trip> { }

    /**
     * Represents a Metro North Railroad (MNR) alert.
     *
     * @since 1.0.0
     * @version 1.0.0
     * @author Katsute
     */
    public abstract static class Alert extends TransitAlert<Integer,Route,Integer,Stop> { }

}
