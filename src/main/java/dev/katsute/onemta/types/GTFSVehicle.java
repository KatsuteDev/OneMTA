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
 * Represents a GTFS vehicle.
 *
 * @param <R> transit route type
 * @param <S> transit stop type
 * @param <T> transit trip type
 * @param <SID> stop ID format
 * @param <RID> route ID format
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class GTFSVehicle<R extends TransitRoute<?,?,?>, S extends TransitStop<?,?,?>, T extends TransitTrip<?,?,?>, SID, RID> extends TransitVehicle<R,S,T,SID,RID,String> {

    /**
     * Returns the current vehicle status.
     *
     * @return vehicle status
     *
     * @since 1.0.0
     */
    public abstract String getCurrentStatus();

}
