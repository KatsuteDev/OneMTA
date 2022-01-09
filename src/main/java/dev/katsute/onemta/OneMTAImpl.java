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
import dev.katsute.onemta.exception.MissingResourceException;
import dev.katsute.onemta.railroad.*;
import dev.katsute.onemta.subway.*;
import dev.katsute.onemta.types.BusDirection;
import dev.katsute.onemta.types.SubwayDirection;

import java.util.Arrays;

final class OneMTAImpl extends OneMTA {

    private final transient String busToken;
    private final transient String subwayToken;
    private final DataResource[] resources;

    private final MTAService service = new MTAService();

    OneMTAImpl(final String busToken, final String subwayToken){
        this(busToken, subwayToken, (DataResource[]) null);
    }

    OneMTAImpl(final String busToken, final String subwayToken, final DataResource... resources){
        this.busToken    = busToken;
        this.subwayToken = subwayToken;
        this.resources   = resources == null ? new DataResource[0] : Arrays.copyOf(resources, resources.length);
    }

    // resources

    DataResource getDataResource(final DataResourceType type){
        for(final DataResource resource : resources)
            if(resource.getType() == type)
                return resource;
        throw new MissingResourceException(type);
    }

    // methods


    @Override
    public BusRoute getBusRoute(final String route){
        return null;
    }

    @Override
    public BusStop getBusStop(final int stopID){
        return null;
    }

    @Override
    public BusVehicle getBus(final int busID){
        return null;
    }

    @Override
    public BusVehicle[] getBusses(){
        return new BusVehicle[0];
    }

    @Override
    public BusVehicle[] getBusses(final String route){
        return new BusVehicle[0];
    }

    @Override
    public BusVehicle[] getBusses(final String route, final BusDirection direction){
        return new BusVehicle[0];
    }

    @Override
    public BusVehicle[] getBusses(final BusRoute route){
        return new BusVehicle[0];
    }

    @Override
    public BusVehicle[] getBusses(final BusRoute route, final BusDirection direction){
        return new BusVehicle[0];
    }

    @Override
    public SubwayRoute getSubwayRoute(final String route){
        return null;
    }

    @Override
    public SubwayStop getSubwayStop(final String stopID){
        return null;
    }

    @Override
    public SubwayStop getSubwayStop(final String stopID, final SubwayDirection direction){
        return null;
    }

    @Override
    public SubwayVehicle getSubwayTrain(final String trainID){
        System.out.println(service.subway.get1234567(subwayToken));
        return null;
    }

    @Override
    public RailroadRoute getLIRRRoute(final String route){
        return null;
    }

    @Override
    public RailroadStop getLIRRStop(final String stopId){
        return null;
    }

    @Override
    public RailroadVehicle getLIRRTrain(final String trainID){
        System.out.println(service.lirr.getLIRR(subwayToken));
        return null;
    }

    @Override
    public RailroadRoute getMNRRoute(final String route){
        return null;
    }

    @Override
    public RailroadStop getMNRStop(final String stopId){
        return null;
    }

    @Override
    public RailroadVehicle getMNRTrain(final String trainID){
        System.out.println(service.mnrr.getMNRR(subwayToken));
        return null;
    }

}
