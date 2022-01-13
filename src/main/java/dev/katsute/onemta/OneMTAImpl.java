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
import dev.katsute.onemta.exception.MissingResourceException;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.subway.SubwayDirection;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
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

    // bus methods

    public final Object test(){
        return service.subway.get1234567(subwayToken);
    }

    @Override
    public final Bus.Route getBusRoute(final String route_id){
        return getBusRoute(route_id, null);
    }

    @Override
    public final Bus.Route getBusRoute(final String route_id, final DataResourceType type){
        return OneMTASchema_Bus.asRoute(this, route_id, type);
    }

    @Override
    public final Bus.Stop getBusStop(final int stop_id){
        return getBusStop(stop_id, null);
    }

    @Override
    public final Bus.Stop getBusStop(final int stop_id, final DataResourceType type){
        return  OneMTASchema_Bus.asStop(this, stop_id, type);
    }

    @Override
    public final Bus.Vehicle getBus(final int bus_id){
        return null;
    }

    // subway methods

    String resolveSubwayLine(final String route_id){
        switch(route_id.toUpperCase()){
            case "A":
            case "C":
            case "E":
            case "SF":
            case "SR":

            case "B":
            case "D":
            case "F":
            case "M":

            case "G":

            case "J":
            case "Z":

            case "N":
            case "Q":
            case "R":
            case "W":

            case "L":

            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "GS":

            case "SIR":
                return route_id.toUpperCase();

            case "FX":
            case "5X":
            case "6X":
            case "7X":
                return String.valueOf(route_id.toUpperCase().charAt(0));
            case "9":
                return "GS";
            case "S":
            case "SI":
                return "SIR";
            default:
                return null;
        }
    }

    GTFSRealtimeProto.FeedMessage resolveSubwayFeed(final String route_id){
        switch(Objects.requireNonNull(resolveSubwayLine(route_id), "Subway route with ID '" + route_id + "' not found")){
            case "A":
            case "C":
            case "E":
            case "SF":
            case "SR":
                return service.subway.getACE(subwayToken);
            case "B":
            case "D":
            case "F":
            case "M":
                return service.subway.getBDFM(subwayToken);
            case "G":
                return service.subway.getG(subwayToken);
            case "J":
            case "Z":
                return service.subway.getJZ(subwayToken);
            case "N":
            case "Q":
            case "R":
            case "W":
                return service.subway.getNQRW(subwayToken);
            case "L":
                return service.subway.getL(subwayToken);
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "GS":
                return service.subway.get1234567(subwayToken);
            case "SIR":
                return service.subway.getSI(subwayToken);
            default:
                return null;
        }
    }

    @Override
    public final Subway.Route getSubwayRoute(final String route_id){
        return OneMTASchema_Subway.asRoute(this, resolveSubwayLine(route_id));
    }

    @Override
    public final Subway.Stop getSubwayStop(final String stop_id){
        return OneMTASchema_Subway.asStop(this, stop_id);
    }

    @Override
    public final Subway.Stop getSubwayStop(final String stop_id, final SubwayDirection direction){
        return OneMTASchema_Subway.asStop(
            this,
            OneMTASchema_Subway.direction.matcher(stop_id).replaceAll("") + (direction == SubwayDirection.NORTH ? 'N' : 'S')
        );
    }

    @Override
    public final Subway.Vehicle getSubwayTrain(final String train_id){
        return null;
    }

    // lirr methods

    @Override
    public final LIRR.Route getLIRRRoute(final int route_id){
        return OneMTASchema_LIRR.asRoute(this, route_id);
    }

    @Override
    public final LIRR.Stop getLIRRStop(final int stop_id){
        return OneMTASchema_LIRR.asStop(this, stop_id);
    }

    @Override
    public final LIRR.Stop getLIRRStop(final String stop_code){
        return OneMTASchema_LIRR.asStop(this, stop_code);
    }

    @Override
    public final LIRR.Vehicle getLIRRTrain(final String train_id){
        return null;
    }

    // mnrr methods

    @Override
    public final MNR.Route getMNRRoute(final int route_id){
        return OneMTASchema_MNR.asRoute(this, route_id);
    }

    @Override
    public final MNR.Stop getMNRStop(final int stop_id){
        return OneMTASchema_MNR.asStop(this, stop_id);
    }

    @Override
    public final MNR.Stop getMNRStop(final String stop_code){
        return OneMTASchema_MNR.asStop(this, stop_code);
    }

    @Override
    public final MNR.Vehicle getMNRTrain(final String train_id){
        return null;
    }

}
