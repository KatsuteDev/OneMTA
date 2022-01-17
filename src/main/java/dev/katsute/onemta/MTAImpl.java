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

import dev.katsute.onemta.Json.JsonObject;
import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.exception.MissingResourceException;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.subway.SubwayDirection;

import java.util.Arrays;
import java.util.Objects;

import static dev.katsute.onemta.GTFSRealtimeProto.*;

@SuppressWarnings("SpellCheckingInspection")
final class MTAImpl extends MTA {

    final transient String busToken;
    final transient String subwayToken;

    final MTAService service = new MTAService();

    private final DataResource[] resources;

    MTAImpl(final String busToken, final String subwayToken){
        this(busToken, subwayToken, (DataResource[]) null);
    }

    MTAImpl(final String busToken, final String subwayToken, final DataResource... resources){
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

    @Override
    public final Bus.Route getBusRoute(final String route_id){
        return getBusRoute(route_id, null);
    }

    @Override
    public final Bus.Route getBusRoute(final String route_id, final DataResourceType type){
        return MTASchema_Bus.asRoute(this, Objects.requireNonNull(route_id, "Route ID must not be null"), type);
    }

    @Override
    public final Bus.Stop getBusStop(final int stop_id){
        return getBusStop(stop_id, null);
    }

    @Override
    public final Bus.Stop getBusStop(final int stop_id, final DataResourceType type){
        return  MTASchema_Bus.asStop(this, stop_id, type);
    }

    @Override
    public final Bus.Vehicle getBus(final int bus_id){
        final JsonObject json = service.bus.getVehicle(busToken, bus_id, null, null);

        final JsonObject[] vehicleMonitoringDelivery = json
            .getJsonObject("Siri")
            .getJsonObject("ServiceDelivery")
            .getJsonArray("VehicleMonitoringDelivery");

        if(!vehicleMonitoringDelivery[0].containsKey("MonitoredVehicleJourney")) return null;

        final JsonObject monitoredVehicleJourney = vehicleMonitoringDelivery[0]
            .getJsonArray("VehicleActivity")[0]
            .getJsonObject("MonitoredVehicleJourney");

        return MTASchema_Bus.asVehicle(this, monitoredVehicleJourney, null, null);
    }

    // subway methods

    String resolveSubwayLine(final String route_id){
        final String route = (route_id.startsWith("0") ? route_id.substring(1) : route_id).toUpperCase();

        switch(route){
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

    FeedMessage resolveSubwayFeed(final String route_id){
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
    public final Subway.Route getSubwayRoute(final int route_id){
        return getSubwayRoute(String.valueOf(route_id));
    }

    @Override
    public final Subway.Route getSubwayRoute(final String route_id){
        return MTASchema_Subway.asRoute(this, Objects.requireNonNull(resolveSubwayLine(route_id), "Route ID must not be null"));
    }

    @Override
    public final Subway.Stop getSubwayStop(final int stop_id){
        return getSubwayStop(String.valueOf(stop_id));
    }

    @Override
    public final Subway.Stop getSubwayStop(final String stop_id){
        return MTASchema_Subway.asStop(this, Objects.requireNonNull(stop_id, "Stop ID must not be null"));
    }

    @Override
    public final Subway.Stop getSubwayStop(final int stop_id, final SubwayDirection direction){
        return getSubwayStop(String.valueOf(stop_id), direction);
    }

    @Override
    public final Subway.Stop getSubwayStop(final String stop_id, final SubwayDirection direction){
        return MTASchema_Subway.asStop(
                this,
                MTASchema_Subway.direction
                .matcher(Objects.requireNonNull(stop_id, "Stop ID must not be null"))
                .replaceAll("") +
                (Objects.requireNonNull(direction, "Direction must not be null") == SubwayDirection.NORTH ? 'N' : 'S')
        );
    }

    @Override
    public final Subway.Vehicle getSubwayTrain(final String train_id){
        Objects.requireNonNull(train_id, "Train ID must not be null");

        final FeedMessage feed = resolveSubwayFeed(train_id.substring(1, 3));
        @SuppressWarnings("ConstantConditions") // resolve will handle NPE
        final int len          = feed.getEntityCount();

        TripUpdate tripUpdate = null;
        String tripVehicle    = null;

        for(int i = 0; i < len; i++){
            final FeedEntity entity = feed.getEntity(0);

            // get next trip
            if(entity.hasTripUpdate()){
                if( // find trip for train
                    entity.getTripUpdate().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId().equals(train_id)
                ){
                    tripUpdate  = entity.getTripUpdate();
                    tripVehicle = tripUpdate.getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId();
                }
            }else if( // get matching vehicle for trip
                entity.hasVehicle() &&
                entity.getVehicle().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId().equals(tripVehicle)
            )
                return MTASchema_Subway.asVehicle(this, entity.getVehicle(), tripUpdate);
        }

        return null;
    }

    // lirr methods

    @Override
    public final LIRR.Route getLIRRRoute(final int route_id){
        return MTASchema_LIRR.asRoute(this, route_id);
    }

    @Override
    public final LIRR.Stop getLIRRStop(final int stop_id){
        return MTASchema_LIRR.asStop(this, stop_id);
    }

    @Override
    public final LIRR.Stop getLIRRStop(final String stop_code){
        return MTASchema_LIRR.asStop(this, Objects.requireNonNull(stop_code, "Stop code must not be null"));
    }

    @Override
    public final LIRR.Vehicle getLIRRTrain(final String train_id){
        Objects.requireNonNull(train_id, "Train ID must not be null");

        final FeedMessage feed = service.lirr.getLIRR(subwayToken);
        final int len          = feed.getEntityCount();

        TripUpdate tripUpdate = null;
        String tripVehicle    = null;

        for(int i = 0; i < len; i++){
            final FeedEntity entity = feed.getEntity(0);

            // get next trip
            if(entity.hasTripUpdate()){
                if( // only include trips with vehicle and with matching ID
                    entity.getTripUpdate().hasVehicle() &&
                    entity.getTripUpdate().getVehicle().getLabel().equals(train_id)
                ){
                    tripUpdate  = entity.getTripUpdate();
                    tripVehicle = tripUpdate.getVehicle().getLabel();
                }
            }else if( // get matching vehicle for trip
                entity.hasVehicle() &&
                entity.getVehicle().getVehicle().getLabel().equals(tripVehicle)
            )
                return MTASchema_LIRR.asVehicle(this, entity.getVehicle(), tripUpdate);
        }

        return null;
    }

    // mnrr methods

    @Override
    public final MNR.Route getMNRRoute(final int route_id){
        return MTASchema_MNR.asRoute(this, route_id);
    }

    @Override
    public final MNR.Stop getMNRStop(final int stop_id){
        return MTASchema_MNR.asStop(this, stop_id);
    }

    @Override
    public final MNR.Stop getMNRStop(final String stop_code){
        return MTASchema_MNR.asStop(this, Objects.requireNonNull(stop_code, "Stop code must not be null"));
    }

    @Override
    public final MNR.Vehicle getMNRTrain(final String train_id){
        Objects.requireNonNull(train_id, "Train ID must not be null");
        final FeedMessage feed = service.mnrr.getMNRR(subwayToken);
        final int len = feed.getEntityCount();

        for(int i = 0; i < len; i++){
            final FeedEntity entity = feed.getEntity(0);
            if(
                entity.hasVehicle() &&
                entity.getVehicle().hasVehicle() &&
                train_id.equals(entity.getVehicle().getVehicle().getLabel())
            )
                return MTASchema_MNR.asVehicle(this, entity.getVehicle(), entity.getTripUpdate());
        }

        return null;
    }

}