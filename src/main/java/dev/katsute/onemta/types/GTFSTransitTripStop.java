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

import java.util.Date;

/**
 * Represents a GTFS transit trip stop.
 *
 * @param <S> transit stop type
 * @param <T> transit trip type
 * @param <SID> stop ID format
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class GTFSTransitTripStop<S extends TransitStop<?,?,?>, T extends TransitTrip<?,?,?>, SID> extends TransitTripStop<S,T,SID> {

    /**
     * Returns the arrival time.
     *
     * @return arrival time
     *
     * @see Date
     * @see #getArrivalTimeEpochMillis()
     * @since 1.0.0
     */
    public abstract Date getArrivalTime();

    /**
     * Returns the arrival time as milliseconds since epoch.
     *
     * @return arrival time
     *
     * @see #getArrivalTime()
     * @since 1.0.0
     */
    public abstract Long getArrivalTimeEpochMillis();

    /**
     * Returns the departure time.
     *
     * @return departure time
     *
     * @see Date
     * @see #getDepartureTimeEpochMillis()
     * @since 1.0.0
     */
    public abstract Date getDepartureTime();

    /**
     * Returns the departure time as milliseconds since epoch.
     *
     * @return departure time
     *
     * @see #getDepartureTime()
     * @since 1.0.0
     */
    public abstract Long getDepartureTimeEpochMillis();

    /**
     * Returns the track.
     *
     * @return track
     *
     * @since 1.0.0
     */
    public abstract String getTrack();

}
