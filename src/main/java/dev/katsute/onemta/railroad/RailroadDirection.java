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

/**
 * The direction for a railroad.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public enum RailroadDirection {

    /**
     * Manhattan bound trains.
     */
    MANHATTAN(0),

    /**
     * Long Island and North bound trains.
     */
    ORIGIN(1);

    private final int direction;

    RailroadDirection(final int direction){
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
    public static RailroadDirection asDirection(final int direction){
        switch(direction){
            case 0:
                return MANHATTAN;
            case 1:
                return ORIGIN;
            default:
                return null;
        }
    }

}
