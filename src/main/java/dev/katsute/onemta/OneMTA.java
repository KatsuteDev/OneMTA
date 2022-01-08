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

import dev.katsute.onemta.bus.BusStop;
import dev.katsute.onemta.bus.BusVehicle;

import java.util.List;

public abstract class OneMTA {

    OneMTA(){ }

    public static OneMTA create(final String busToken, final String subwayToken){
        return new OneMTAImpl(busToken, subwayToken);
    }

    public static OneMTA create(final String busToken, final String subwayToken, final DataResource... resources){
        return new OneMTAImpl(busToken, subwayToken, resources);
    }

    public abstract void print();

    public abstract BusStop getBusStop(final int stopID);

    public abstract BusStop getBusStop(final int stopID, final String line);

    public abstract BusStop getBusStop(final int stopID, final String line, final int direction);

    public abstract BusVehicle getBus(final int busID);

    public abstract List<BusVehicle> getBusses();

    public abstract List<BusVehicle> getBusses(final String line);

    public abstract List<BusVehicle> getBusses(final String line, final int direction);

}
