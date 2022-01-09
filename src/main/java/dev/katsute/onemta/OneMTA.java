/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
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

import dev.katsute.onemta.bus.*;
import dev.katsute.onemta.railroad.*;
import dev.katsute.onemta.subway.*;
import dev.katsute.onemta.types.BusDirection;
import dev.katsute.onemta.types.SubwayDirection;

public abstract class OneMTA {

    OneMTA(){ }

    public static OneMTA create(final String busToken, final String subwayToken){
        return new OneMTAImpl(busToken, subwayToken);
    }

    public static OneMTA create(final String busToken, final String subwayToken, final DataResource... resources){
        return new OneMTAImpl(busToken, subwayToken, resources);
    }

    // bus methods

    public abstract BusRoute getBusRoute(final String route);

    public abstract BusStop getBusStop(final int stopID); // todo: make bus stop class fulfill requests instead, accept line & direction parameters in BusStop#getVehicles(...)

    public abstract BusVehicle getBus(final int busID);

    public abstract BusVehicle[] getBusses();

    public abstract BusVehicle[] getBusses(final String route);

    public abstract BusVehicle[] getBusses(final String route, final BusDirection direction);

    public abstract BusVehicle[] getBusses(final BusRoute route);

    public abstract BusVehicle[] getBusses(final BusRoute route, final BusDirection direction);

    // subway methods

    public abstract SubwayRoute getSubwayRoute(final String route);

    public abstract SubwayStop getSubwayStop(final String stopID); // todo: same as bus stop, use SubwayStop#getVehicles(...)

    public abstract SubwayStop getSubwayStop(final String stopID, final SubwayDirection direction);

    public abstract SubwayVehicle getSubwayTrain(final String trainID);

    // train methods

    public abstract RailroadRoute getLIRRRoute(final String route);

    public abstract RailroadStop getLIRRStop(final String stopId);

    public abstract RailroadVehicle getLIRRTrain(final String trainID);

    public abstract RailroadRoute getMNRRoute(final String route);

    public abstract RailroadStop getMNRStop(final String stopId);

    public abstract RailroadVehicle getMNRTrain(final String trainID);

}
