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

    public abstract Bus.Route getBus(final String route);

    public abstract Bus.Stop getBusStop(final int stopID); // todo: make bus stop class fulfill requests instead, accept line & direction parameters in BusStop#getVehicles(...)

    public abstract Bus.Vehicle getBus(final int busID);

    public abstract Bus.Vehicle[] getBusses();

    public abstract Bus.Vehicle[] getBusses(final String route);

    public abstract Bus.Vehicle[] getBusses(final String route, final BusDirection direction);

    public abstract Bus.Vehicle[] getBusses(final Bus.Route route);

    public abstract Bus.Vehicle[] getBusses(final Bus.Route route, final BusDirection direction);

    // subway methods

    public abstract Subway.Route getSubwayRoute(final String route);

    public abstract Subway.Stop getSubwayStop(final String stopID); // todo: same as bus stop, use Subway.Stop#getVehicles(...)

    public abstract Subway.Stop getSubway(final String stopID, final SubwayDirection direction);

    public abstract Subway.Vehicle getSubwayTrain(final String trainID);

    // train methods

    public abstract LIRR.Route getLIRRRoute(final String route);

    public abstract LIRR.Stop getLIRRStop(final String stopId);

    public abstract LIRR.Vehicle getLIRRTrain(final String trainID);

    public abstract MNR.Route getMNRRoute(final String route);

    public abstract MNR.Stop getMNRStop(final String stopId);

    public abstract MNR.Vehicle getMNRTrain(final String trainID);

}
