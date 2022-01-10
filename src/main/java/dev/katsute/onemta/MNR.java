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

package dev.katsute.onemta;

import dev.katsute.onemta.types.*;

/**
 * Contains all Metro North Railroad (MNR) related methods.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class MNR {

    // todo: get vehicles for route
    public abstract static class Route extends TransitRoute { }

    // todo: get vehicles for stop
    public abstract static class Stop extends TransitStop<Integer> {

        /**
         * Returns the stop code for the stop.
         *
         * @return stop code
         *
         * @since 1.0.0
         */
        public abstract String getStopCode();

        /**
         * Returns the stop description.
         *
         * @return stop description
         *
         * @since 1.0.0
         */
        public abstract String getStopDescription();

        /**
         * Returns if the stop is wheelchair accesible.
         *
         * @return wheelchair accesible
         *
         * @since 1.0.0
         */
        public abstract boolean hasWheelchairBoarding();

    }

    public abstract static class Vehicle extends TransitVehicle<Route> { }

}
