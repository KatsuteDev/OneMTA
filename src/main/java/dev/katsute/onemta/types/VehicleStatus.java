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
 * The current vehicle status.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public enum VehicleStatus {

    INCOMING_AT(0),
    STOPPED_AT(1),
    IN_TRANSIT_TO(2);

    private final int status;

    VehicleStatus(final int direction){
        this.status = direction;
    }

    /**
     * Returns the status ID.
     *
     * @return status
     *
     * @since 1.0.0
     */
    public final int getStatus(){
        return status;
    }

    /**
     * Converts a status ID to an enum.
     *
     * @param status status ID
     * @return status
     *
     * @since 1.0.0
     */
    public static VehicleStatus asStatus(final int status){
        switch(status){
            case 0:
                return INCOMING_AT;
            case 1:
                return STOPPED_AT;
            case 2:
                return IN_TRANSIT_TO;
            default:
                return null;
        }
    }

}
