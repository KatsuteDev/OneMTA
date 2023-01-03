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

package dev.katsute.onemta.attribute;

import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.types.*;

/**
 * Represents reference interfaces.
 *
 * @see Route
 * @see Stop
 * @see Trip
 * @see Vehicle
 * @since 2.0.0
 * @version 2.0.0
 * @author Katsute
 */
public abstract class Reference {

    /**
     * Indicates that there is a route reference.
     *
     * @param <R> transit route type
     *
     * @since 2.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public interface Route<R extends TransitRoute<?,?,?>> {

        /**
         * Returns the route.
         *
         * @return route
         *
         * @since 1.0.0
         */
        R getRoute();

    }

    /**
     * Indicates that there is a stop reference.
     *
     * @param <S> transit stop type
     *
     * @since 2.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public interface Stop<S extends TransitStop<?,?,?>> {

        /**
         * For all vehicles it will return the current or next stop on the route. <br>
         * For trip stops it will return the trip stop.
         *
         * @return stop
         *
         * @since 1.0.0
         */
        S getStop();

    }

    /**
     * Indicates that there is a trip reference.
     *
     * @param <T> transit trip type
     *
     * @since 2.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public interface Trip<T extends TransitTrip<?,?,?>> {

        /**
         * Returns the trip.
         *
         * @return trip
         *
         * @since 1.0.0
         */
        T getTrip();

    }

    /**
     * Indicates that there is a vehicle reference.
     *
     * @param <V> transit vehicle type
     *
     * @since 2.0.0
     * @version 2.0.0
     * @author Katsute
     */
    public interface Vehicle<V extends TransitVehicle<?,?,?,?,?,?>> {

        /**
         * Returns vehicles. For subway routes this will return both local and express trains; use {@link Subway.Vehicle#isExpress()} to check which trains are express.
         *
         * @return vehicles
         *
         * @since 1.0.0
         */
        V[] getVehicles();

    }

}
