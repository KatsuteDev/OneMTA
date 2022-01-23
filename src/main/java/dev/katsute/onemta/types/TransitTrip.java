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

package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.RouteReference;

/**
 * Represents a transit trip.
 *
 * @param <V> transit vehicle type
 * @param <R> transit route type
 * @param <S> transit stop type
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class TransitTrip<V extends TransitVehicle<?,?,?,?,?,?>, R extends TransitRoute<?,?,?>, S extends TransitTripStop<?,?,?>> implements RouteReference<R> {

    /**
     * Returns the planned stops for the trip.
     *
     * @return trip stops
     *
     * @since 1.0.0
     */
    public abstract S[] getTripStops();

    /**
     * Returns the vehicle the trip is for.
     *
     * @return vehicle trip
     *
     * @since 1.0.0
     */
    public abstract V getVehicle();

}
