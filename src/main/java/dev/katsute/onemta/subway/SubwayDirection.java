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

/**
 * The direction for a subway.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public enum SubwayDirection {

    /**
     * Uptown, Bronx, and Queens bound trains; also Grand Central bound Shuttle trains.
     */
    NORTH(1),

    /**
     * Downtown, Brooklyn, and Manhattan bound trains; also Times Square bound Shuttle trains.
     */
    SOUTH(3);

    private final int direction;

    SubwayDirection(final int direction){
        this.direction = direction;
    }

    /**
     * Returns the GTFS direction ID.
     *
     * @return gtfs direction
     *
     * @since 1.0.0
     */
    public final int getDirection(){
        return direction;
    }

    /**
     * Converts a GTFS direction ID to an enum.
     *
     * @param direction gtfs direction
     * @return direction
     *
     * @since 1.0.0
     */
    public static SubwayDirection asDirection(final int direction){
        switch(direction){
            case 1:
                return NORTH;
            case 3:
                return SOUTH;
            default:
                return null;
        }
    }

}
