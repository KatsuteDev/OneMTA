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

import dev.katsute.onemta.attribute.*;

/**
 * Represents a transit vehicle.
 *
 * @param <R> transit route type
 * @param <S> transit stop type
 * @param <T> transit trip type
 * @param <SID> stop ID format
 * @param <RID> route ID format
 * @param <VID> vehicle ID format
 */
public abstract class TransitVehicle<R extends TransitRoute<?,?,?>, S extends TransitStop<?,?,?>, T extends TransitTrip<?,?,?>, SID, RID, VID> implements RouteReference<R>, StopReference<S>, TripReference<T>, Updatable {

    /**
     * Returns the vehicle ID.
     *
     * @return vehicle ID
     *
     * @since 1.0.0
     */
    public abstract VID getVehicleID();

    /**
     * Returns the stop ID.
     *
     * @return stop ID
     *
     * @since 1.0.0
     */
    public abstract SID getStopID();

    /**
     * Returns the route ID.
     *
     * @return route ID
     *
     * @since 1.0.0
     */
    public abstract RID getRouteID();

}
