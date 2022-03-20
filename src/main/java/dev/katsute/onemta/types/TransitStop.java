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
 * Represents a transit stop.
 *
 * @param <SID> stop ID format
 * @param <V> transit vehicle type
 * @param <A> transit alert type
 *
 * @since 1.0.0
 * @version 1.1.0
 * @author Katsute
 */
public abstract class TransitStop<SID, V extends TransitVehicle<?,?,?,?,?,?>, A extends TransitAlert<?,?,?,?>> implements Alerts<A>, Location, VehiclesReference<V>, Updatable {

    /**
     * Returns the stop ID.
     *
     * @return stop ID
     *
     * @since 1.0.0
     */
    public abstract SID getStopID();

    /**
     * Returns the stop name.
     *
     * @return stop name
     *
     * @since 1.0.0
     */
    public abstract String getStopName();

    /**
     * Returns if the stop has the exact same stop ID. Includes stop direction.
     *
     * @param object stop, stop ID, or stop code
     * @return if stop ID matches
     *
     * @since 1.1.0
     * @see #isSameStop(Object)
     */
    public abstract boolean isExactStop(final Object object);

    /**
     * Returns if the stop is the same stop. Ignores stop direction.
     *
     * @param object stop, stop ID, or stop code
     * @return if stop is referring to the same stop
     *
     * @since 1.1.0
     * @see #isExactStop(Object)
     */
    public abstract boolean isSameStop(final Object object);

}
