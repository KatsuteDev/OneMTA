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
import dev.katsute.onemta.types.DataResource;

import java.util.Arrays;
import java.util.List;

import static dev.katsute.onemta.MTAService.*;

final class OneMTAImpl extends OneMTA {

    private final transient String busToken;
    private final transient String subwayToken;
    private final DataResource[] resources;

    private final MTABusService busService       = MTABusService.create();
    private final MTASubwayService subwayService = MTASubwayService.create();

    OneMTAImpl(final String busToken, final String subwayToken){
        this(busToken, subwayToken, new DataResource[0]);
    }

    OneMTAImpl(final String busToken, final String subwayToken, final DataResource... resources){
        this.busToken    = busToken;
        this.subwayToken = subwayToken;
        this.resources   = Arrays.copyOf(resources, resources.length);
    }

    //

    @Override
    public void print(){
        System.out.println(busService.getVehicle(busToken, null, "M1", null).raw());
    }

    @Override
    public BusStop getBusStop(final int stopID){
        return null;
    }

    @Override
    public BusStop getBusStop(final int stopID, final String line){
        return null;
    }

    @Override
    public BusStop getBusStop(final int stopID, final String line, final int direction){
        return null;
    }

    @Override
    public BusVehicle getBus(final int busID){
        return null;
    }

    @Override
    public List<BusVehicle> getBusses(){
        return null;
    }

    @Override
    public List<BusVehicle> getBusses(final String line){
        return null;
    }

    @Override
    public List<BusVehicle> getBusses(final String line, final int direction){
        return null;
    }

}
