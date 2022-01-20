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

import dev.katsute.onemta.types.*;

/**
 * Represents a railroad stop.
 *
 * @param <V> transit vehicle type
 * @param <A> transit alert type
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class RailroadStop<V extends TransitVehicle<?,?,?,?,?,?>, A extends TransitAlert<?,?,?,?>> extends TransitStop<Integer,V,A> {

    /**
     * Returns the stop code for the station.
     *
     * @return stop code
     *
     * @since 1.0.0
     */
    public abstract String getStopCode();

    /**
     * Returns the stop description.
     *
     * @return route description
     *
     * @since 1.0.0
     */
    public abstract String getStopDescription();

    /**
     * Returns if the stop is wheelchair accessible.
     *
     * @return wheelchair accessible
     *
     * @since 1.0.0
     */
    public abstract Boolean hasWheelchairBoarding();

}
