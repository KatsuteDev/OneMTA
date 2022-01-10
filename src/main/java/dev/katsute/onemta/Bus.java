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

import dev.katsute.onemta.attribute.*;
import dev.katsute.onemta.types.*;

/**
 * Contains all bus related methods. For the bus vehicle see {@link Vehicle}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class Bus {

    // todo: add utility methods for isSBS, isExpress, isShuttle, isLimited
    // todo: get vehicles for route
    public abstract static class Route extends TransitRoute implements RouteShortName, RouteDescription { }

    // todo: get vehicles for stop
    public abstract static class Stop extends TransitStop<Integer> implements RouteDescription { }

    // todo: add utility methods for isSBS, isExpress, isShuttle, isLimited
    public abstract static class Vehicle extends TransitVehicle<Route> implements LocationReference {

        /**
         * Returns the vehicle number that is visible on the side and front of the bus.
         *
         * @return vehicle number
         *
         * @since 1.0.0
         */
        public abstract int getID();

        /**
         * Returns the current bearing of the vehicle in degrees, 0 meaning north.
         *
         * @return vehicle bearing
         *
         * @since 1.0.0
         */
        public abstract double getBearing();

    }


}
