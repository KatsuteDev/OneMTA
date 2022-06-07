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

/**
 * Represents a GTFS transit trip.
 *
 * @param <V> transit vehicle type
 * @param <R> transit route type
 * @param <S> transit stop type
 *
 * @since 1.0.0
 * @version 1.2.0
 * @author Katsute
 */
public abstract class GTFSTransitTrip<V extends TransitVehicle<?,?,?,?,?,?>, R extends TransitRoute<?,?,?>, S extends TransitTripStop<?,?,?>> extends TransitTrip<V,R,S> {

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
    public abstract String getRouteID();

    /**
     * Returns the schedule relationship.
     *
     * @return schedule relationship
     *
     * @since 1.2.0
     */
    public abstract String getScheduleRelationship();

}
