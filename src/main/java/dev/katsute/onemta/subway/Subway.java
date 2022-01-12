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

package dev.katsute.onemta.subway;

import dev.katsute.onemta.attribute.RouteDescription;
import dev.katsute.onemta.attribute.RouteShortName;
import dev.katsute.onemta.types.*;

/**
 * Contains all subway related methods. For the subway train see {@link Vehicle}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class Subway {

    // todo: get vehicles for route
    public abstract static class Route extends TransitRoute implements RouteShortName, RouteDescription { }

    // todo: get vehicles for stop
    public abstract static class Stop extends TransitStop<String> {

        /**
         * Returns which direction the station serves. Returns null if the direction is not specified.
         *
         * @return direction or null
         *
         * @see SubwayDirection
         * @since 1.0.0
         */
        @SuppressWarnings("GrazieInspection")
        public abstract SubwayDirection getDirection();

        /**
         * Returns if two stops are referring to the same stop on the same line.
         * <br>
         * Ex: 901, 901N, and 901S count as the same stop.
         * <br>
         * Ex: 901 and 723 do not count as the same stop.
         *
         * @param stop other stop
         * @return if the stop is the same
         *
         * @since 1.0.0
         */
        public abstract boolean isSameStop(final Stop stop);

    }

    // todo: add utility methods for isExpress, isLocal
    public abstract static class Vehicle extends TransitVehicle<Route> { }

    public abstract static class Trip extends TransitTrip {

        /**
         * Returns which direction the trip is going.
         *
         * @return direction
         *
         * @see SubwayDirection
         * @since 1.0.0
         */
        public abstract SubwayDirection getDirection();

        public abstract static class Stop extends TransitStopUpdate {

            public abstract int getActualTrack();

        }

    }

}
