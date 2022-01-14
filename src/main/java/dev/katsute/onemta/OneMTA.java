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

import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.subway.SubwayDirection;

@SuppressWarnings("SpellCheckingInspection")
public abstract class OneMTA {

    OneMTA(){ }

    public static OneMTA create(final String busToken, final String subwayToken){
        return new OneMTAImpl(busToken, subwayToken);
    }

    public static OneMTA create(final String busToken, final String subwayToken, final DataResource... resources){
        return new OneMTAImpl(busToken, subwayToken, resources);
    }

    // bus methods

    public abstract Bus.Route getBusRoute(final String route_id);

    public abstract Bus.Route getBusRoute(final String route_id, final DataResourceType type);

    public abstract Bus.Stop getBusStop(final int stop_id);

    public abstract Bus.Stop getBusStop(final int stop_id, final DataResourceType type);

    public abstract Bus.Vehicle getBus(final int bus_id);

    // subway methods

    public abstract Subway.Route getSubwayRoute(final String route_id);

    public abstract Subway.Stop getSubwayStop(final String stop_id);

    public abstract Subway.Stop getSubwayStop(final String stop_id, final SubwayDirection direction);

    public abstract Subway.Vehicle getSubwayTrain(final String train_id);

    // lirr methods

    public abstract LIRR.Route getLIRRRoute(final int route_id);

    public abstract LIRR.Stop getLIRRStop(final int stop_id);

    public abstract LIRR.Stop getLIRRStop(final String stop_code);

    public abstract LIRR.Vehicle getLIRRTrain(final String train_id);

    // mnrr methods

    public abstract MNR.Route getMNRRoute(final int route_id);

    public abstract MNR.Stop getMNRStop(final int stop_id);

    public abstract MNR.Stop getMNRStop(final String stop_code);

    public abstract MNR.Vehicle getMNRTrain(final String train_id);

}
