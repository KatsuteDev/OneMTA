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
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

/**
 * Represents a transit vehicle.
 *
 * @param <R> vehicle route type
 *
 * @see Bus.Vehicle
 * @see Subway.Vehicle
 * @see LIRR.Vehicle
 * @see MNR.Vehicle
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class TransitVehicle<R extends TransitRoute<RID>, T extends TransitTrip<?,?,?>, S extends TransitStop<SID>, SID, RID> implements RouteReference<R>, TripReference<T>, StopReference<S> {

    public abstract VehicleStatus getCurrentStatus();

    public abstract SID getStopID();

    public abstract RID getRouteID();

}
