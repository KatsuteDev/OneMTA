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

import dev.katsute.onemta.exception.MissingResourceException;
import dev.katsute.onemta.types.BusDirection;
import dev.katsute.onemta.types.SubwayDirection;

import java.util.Arrays;

final class OneMTAImpl extends OneMTA {

    final transient String busToken;
    final transient String subwayToken;

    final MTAService service = new MTAService();

    private final DataResource[] resources;

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
    public Bus.Route getBusRoute(final String route_id){
        return null;
    }

    @Override
    public Bus.Stop getBusStop(final int stop_id){
        return null;
    }

    @Override
    public Bus.Vehicle getBus(final int bus_id){
        return null;
    }

    @Override
    public Bus.Vehicle[] getBusses(){
        return new Bus.Vehicle[0];
    }

    @Override
    public Subway.Route getSubwayRoute(final String route_id){
        return null;
    }

    @Override
    public Subway.Stop getSubwayStop(final String stop_id){
        return null;
    }

    @Override
    public Subway.Stop getSubwayStop(final String stop_id, final SubwayDirection direction){
        return null;
    }

    @Override
    public Subway.Vehicle getSubwayTrain(final String train_id){
        return null;
    }

    @Override
    public LIRR.Route getLIRRRoute(final String route_id){
        return null;
    }

    @Override
    public LIRR.Stop getLIRRStop(final String stop_id){
        return null;
    }

    @Override
    public LIRR.Vehicle getLIRRTrain(final String train_id){
        return null;
    }

    @Override
    public MNR.Route getMNRRoute(final String route_id){
        return null;
    }

    @Override
    public MNR.Stop getMNRStop(final String stop_id){
        return null;
    }

    @Override
    public MNR.Vehicle getMNRTrain(final String train_id){
        return null;
    }

}
