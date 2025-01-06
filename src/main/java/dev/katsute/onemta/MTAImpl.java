/*
 * Copyright (C) 2025 Katsute <https://github.com/Katsute>
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

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static dev.katsute.onemta.GTFSRealtimeProto.*;

@SuppressWarnings({"SpellCheckingInspection", "DataFlowIssue"})
final class MTAImpl extends MTA {

    final transient String busToken;

    final MTAService service;

    private final DataResource[] resources;

    MTAImpl(final String busToken, final int cacheSeconds, final DataResource... resources){
        this.service = new MTAService(busToken, cacheSeconds);

        this.busToken = busToken;
        this.resources = resources == null ? new DataResource[0] : Arrays.copyOf(resources, resources.length);
    }

    // resources

    final DataResource getDataResource(final DataResourceType type){
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
        return MTASchema_Bus.asStop(this, stop_id, type);
    }

    @Override
    public final Bus.Vehicle getBus(final int bus_id){
        final String id = String.valueOf(bus_id);
        return transformFirstEntity(
            service.bus.getVehiclePositions(),
            ent -> Objects.equals(ent.getId().split("_")[1], id), // don't match 'MTA NYCT_' or 'MTABC_' ; numbers are unique to each bus
            ent -> {
                final String busId = ent.getId();

                // find matching trip entity
                final FeedEntity trip = getFeedEntity(
                    service.bus.getTripUpdates(),
                    tent -> Objects.equals(tent.getTripUpdate().getVehicle().getId(), busId)
                );

                return MTASchema_Bus.asVehicle(this, ent.getVehicle(), trip.getTripUpdate(), null);
            }
        );
    }

    @Override
    public final Bus.Alert[] getBusAlerts(){
        return transformFeedEntities(
            service.alerts.getBus(),
            ent -> MTASchema_Bus.asTransitAlert(this, ent),
            new Bus.Alert[0]
        );
    }

    // subway methods

    final FeedMessage resolveSubwayFeed(final String route_id){
        final String route = MTASchema_Subway.stripExpress(Objects.requireNonNull(MTASchema_Subway.resolveSubwayLine(Objects.requireNonNull(route_id, "Route ID must not be null")), "Subway route with ID '" + route_id + "' not found"));
        switch(route){
            case "A":
            case "C":
            case "E":
            case "FS":
            case "SF":
            case "SR":
                return service.subway.getACE();
            case "B":
            case "D":
            case "F":
            case "M":
                return service.subway.getBDFM();
            case "G":
                return service.subway.getG();
            case "J":
            case "Z":
                return service.subway.getJZ();
            case "N":
            case "Q":
            case "R":
            case "W":
                return service.subway.getNQRW();
            case "L":
                return service.subway.getL();
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "GS":
                return service.subway.get1234567();
            case "SI":
                return service.subway.getSI();
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
        return MTASchema_Subway.asRoute(this, Objects.requireNonNull(MTASchema_Subway.resolveSubwayLine(Objects.requireNonNull(route_id, "Route ID must not be null")), "Failed to find subway route with id '" + route_id + "'"));
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
            MTASchema_Subway.stripDirection(Objects.requireNonNull(stop_id, "Stop ID must not be null")) +
                (Objects.requireNonNull(direction, "Direction must not be null") == SubwayDirection.NORTH ? 'N' : 'S')
        );
    }

    @Override
    public final Subway.Vehicle getSubwayTrain(final String train_id){ // do not use number ID, trains on different feeds may use same number
        Objects.requireNonNull(train_id, "Train ID must not be null");
        if(!train_id.contains(" ") || train_id.endsWith("_")) return null;
        final FeedMessage feed = Objects.requireNonNull(resolveSubwayFeed(train_id.substring(1, train_id.indexOf(' '))), "Failed to find feed for '" + train_id + '\''); // isolate route code
        final String id = train_id.substring(1); // don't match first symbol, may change for same train; see NYCT proto [train_id]
        return transformFirstEntity(
            feed,
            ent ->
                ent.hasTripUpdate() &&
                Objects.equals(ent.getTripUpdate().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId().substring(1), id),
            ent -> {
                // find matching vehicle entity
                final FeedEntity veh = getFeedEntity(
                    feed,
                    vent ->
                        vent.hasVehicle() &&
                        Objects.equals(vent.getVehicle().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId().substring(1), id)
                );

                return MTASchema_Subway.asVehicle(this, veh.getVehicle(), ent.getTripUpdate(), null);
            }
        );
    }

    @Override
    public final Subway.Alert[] getSubwayAlerts(){
        return transformFeedEntities(
            service.alerts.getSubway(),
            ent -> MTASchema_Subway.asTransitAlert(this, ent),
            new Subway.Alert[0]
        );
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
    public final LIRR.Vehicle getLIRRTrain(final String train_id){ // don't convert to number, train may have string ID
        Objects.requireNonNull(train_id, "Train ID must not be null");
        return transformFirstEntity(
            service.lirr.getLIRR(),
            ent ->
                ent.hasTripUpdate() &&
                Objects.equals(ent.getTripUpdate().getVehicle().getLabel(), train_id),
            ent -> {
                // find matching vehicle entity
                final FeedEntity veh = getFeedEntity(
                    service.lirr.getLIRR(),
                    vent ->
                        vent.hasVehicle() &&
                        Objects.equals(vent.getVehicle().getVehicle().getLabel(), train_id)
                );

                return MTASchema_LIRR.asVehicle(this, veh.getVehicle(), ent.getTripUpdate(), null);
            }
        );
    }

    @Override
    public final LIRR.Alert[] getLIRRAlerts(){
        return transformFeedEntities(
            service.alerts.getLIRR(),
            ent -> MTASchema_LIRR.asTransitAlert(this, ent),
            new LIRR.Alert[0]
        );
    }

    // mnr methods

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
    public final MNR.Vehicle getMNRTrain(final String train_id){ // don't convert to number, train may have string ID
        Objects.requireNonNull(train_id, "Train ID must not be null");
        return transformFirstEntity(
            service.mnr.getMNR(),
            ent -> Objects.equals(ent.getId(), train_id),
            ent -> MTASchema_MNR.asVehicle(
                this,
                ent.getVehicle(),
                ent.getTripUpdate(),
                null
            )
        );
    }

    @Override
    public final MNR.Alert[] getMNRAlerts(){
        return transformFeedEntities(
            service.alerts.getMNR(),
            ent -> MTASchema_MNR.asTransitAlert(this, ent),
            new MNR.Alert[0]
        );
    }

    // shared methods

    final FeedEntity getFeedEntity(final FeedMessage feed, final Predicate<FeedEntity> predicate){
        final int len = feed.getEntityCount();
        for(int i = 0; i < len; i++){
            final FeedEntity ent = feed.getEntity(i);
            try{
                if(predicate.test(ent))
                    return ent;
            }catch(final Throwable ignored){ }
        }
        return null;
    }

    final <T> T transformFirstEntity(final FeedMessage feed, final Predicate<FeedEntity> predicate, final Function<FeedEntity,T> transform){
        final FeedEntity ent = getFeedEntity(feed, predicate);
        return ent != null ? transform.apply(ent) : null;
    }

    final <T> T[] transformFeedEntities(final FeedMessage feed, final Function<FeedEntity,T> transform, final T[] arr){
        return transformFeedEntities(feed, null, transform, arr);
    }

    final <T> T[] transformFeedEntities(final FeedMessage feed, final Predicate<FeedEntity> filter, final Function<FeedEntity,T> transform, final T[] arr){
        final List<T> alerts = new ArrayList<>();
        final int len = feed.getEntityCount();
        for(int i = 0; i < len; i++){
            final FeedEntity ent = feed.getEntity(i);
            try{
                if(filter == null || filter.test(ent))
                    alerts.add(transform.apply(ent));
            }catch(final Throwable ignored){ }
        }
        alerts.removeIf(Objects::isNull);
        return alerts.toArray(arr);
    }

}