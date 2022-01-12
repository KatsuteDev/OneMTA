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
import dev.katsute.onemta.attribute.VehicleReference;

/**
 * Represents the trip or schedule.
 *
 * @param <V> {@link TransitVehicle}
 * @param <R> {@link TransitRoute}
 * @param <S> {@link TransitStopUpdate}
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class TransitTrip<V extends TransitVehicle<R,?,?,?,?>, R extends TransitRoute<?>, S extends TransitStopUpdate<?,?,?>> implements RouteReference<R>, VehicleReference<V> {

    public abstract String getTripId();

    /**
     * Returns the route ID for the trip.
     *
     * @return route ID
     *
     * @since 1.0.0
     */
    public abstract String getRouteID();

    /**
     * Returns the stop updates for the trip.
     *
     * @return stop updates
     *
     * @since 1.0.0
     */
    public abstract S[] getStopUpdates();

}
