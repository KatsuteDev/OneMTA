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

package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.Reference;

/**
 * Represents a transit trip.
 *
 * @param <RID> route ID format
 * @param <R> route type
 * @param <V> vehicle type
 * @param <S> stop type
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class TransitTrip<RID,R extends TransitRoute<?,?,?>,V extends TransitVehicle<?,?,?,?,?,?>,S extends TransitTripStop<?,?,?>> implements Reference.Route<R> {

    /**
     * Returns the trip ID.
     *
     * @return trip ID
     *
     * @see #getTripStops()
     * @since 1.0.0
     */
    public abstract String getTripID();

    /**
     * Returns the route ID.
     *
     * @return route ID
     *
     * @see #getRoute()
     * @since 1.0.0
     */
    public abstract RID getRouteID();

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
