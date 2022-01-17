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

import dev.katsute.onemta.types.TransitAgency;

import java.util.*;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.MNRRProto.*;
import static dev.katsute.onemta.railroad.MNR.*;

@SuppressWarnings("SpellCheckingInspection")
abstract class MTASchema_MNR extends MTASchema {

    static Route asRoute(final MTA mta, final int route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("routes.txt");
        final List<String> row      = csv.getRow("route_id", String.valueOf(route_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find MNR route with id '" + route_id + "'");

        return new Route() {

            private final int routeID           = route_id;
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final TransitAgency agency = asAgency(row.get(csv.getHeaderIndex("agency_id")), resource);

            private final List<Vehicle> vehicles;

            {
                final FeedMessage feed = cast(mta).service.mnrr.getMNRR(cast(mta).subwayToken);
                final int len          = feed.getEntityCount();

                final List<Vehicle> vehicles = new ArrayList<>();
                for(int i = 0; i < len; i++){
                    final FeedEntity entity = feed.getEntity(0);
                    // only include trips with vehicles
                    if(entity.hasVehicle() && entity.hasTripUpdate())
                        // only include vehicles on this route
                        if(Integer.parseInt(entity.getTripUpdate().getTrip().getRouteId()) == routeID)
                            vehicles.add(asVehicle(mta, entity.getVehicle(), entity.getTripUpdate()));
                }

                this.vehicles = Collections.unmodifiableList(vehicles);
            }

            // static data

            @Override
            public final Integer getRouteID(){
                return routeID;
            }

            @Override
            public final String getRouteName(){
                return routeLongName;
            }

            @Override
            public final String getRouteColor(){
                return routeColor;
            }

            @Override
            public final String getRouteTextColor(){
                return routeTextColor;
            }

            @Override
            public final TransitAgency getAgency(){
                return agency;
            }

            // live feed

            @Override
            public final Vehicle[] getVehicles(){
                return vehicles.toArray(new Vehicle[0]);
            }

            // Java

            @Override
            public final boolean equals(final Object o){
                return this == o ||
                   (o != null &&
                    getClass() == o.getClass() &&
                    route_id == ((Route) o).getRouteID());
            }

        };
    }

    static Stop asStop(final MTA mta, final String stop_code){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("stops.txt");
        final List<String> row      = csv.getRow("stop_code", stop_code.toUpperCase());

        // instantiate
        Objects.requireNonNull(row, "Failed to find MNR stop with stopcode '" + stop_code.toUpperCase() + "'");

        return asStop(mta, Integer.parseInt(row.get(csv.getHeaderIndex("stop_id"))));
    }

    static Stop asStop(final MTA mta, final int stop_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("stops.txt");
        final List<String> row      = csv.getRow("stop_id", String.valueOf(stop_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find MNR stop with id '" + stop_id + "'");

        return new Stop(){

            private final Integer stopID  = stop_id;
            private final String stopCode = row.get(csv.getHeaderIndex("stop_code"));
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));

            private final Double stopLat  = Double.valueOf(row.get(csv.getHeaderIndex("stop_lat")));
            private final Double stopLon  = Double.valueOf(row.get(csv.getHeaderIndex("stop_lon")));

            private final Boolean wheelchairAccessible = !row.get(csv.getHeaderIndex("wheelchair_boarding")).equals("2");

            private final List<Vehicle> vehicles;

            {
                final String stop = String.valueOf(stop_id);

                final FeedMessage feed = cast(mta).service.mnrr.getMNRR(cast(mta).subwayToken);
                final int len          = feed.getEntityCount();

                final List<Vehicle> vehicles = new ArrayList<>();
                OUTER:
                for(int i = 0; i < len; i++){
                    final FeedEntity entity = feed.getEntity(0);
                    // only include trips with vehicles
                    if(
                        entity.hasVehicle() &&
                        entity.hasTripUpdate()
                    ){
                        final TripUpdate tu = entity.getTripUpdate();
                        final int len2 = tu.getStopTimeUpdateCount();
                        // check all stops on train route
                        for(int u = 0; u < len2; u++){
                            final TripUpdate.StopTimeUpdate update = tu.getStopTimeUpdate(u);
                            // check if this stop is en route
                            if(update.getStopId().equalsIgnoreCase(stop)){
                                vehicles.add(asVehicle(mta, entity.getVehicle(), entity.getTripUpdate()));
                                continue OUTER;
                            }
                        }
                    }
                }

                this.vehicles = Collections.unmodifiableList(vehicles);
            }

            // static data

            @Override
            public final Integer getStopID(){
                return stop_id;
            }

            @Override
            public final String getStopCode(){
                return stopCode;
            }

            @Override
            public final String getStopName(){
                return stopName;
            }

            @Override
            public final String getStopDescription(){
                return stopDesc;
            }

            @Override
            public final Double getLatitude(){
                return stopLat;
            }

            @Override
            public final Double getLongitude(){
                return stopLon;
            }

            @Override
            public final Boolean hasWheelchairBoarding(){
                return wheelchairAccessible;
            }

            // live feed

            @Override
            public final Vehicle[] getVehicles(){
                return vehicles.toArray(new Vehicle[0]);
            }

            // Java

            @Override
            public final boolean equals(final Object o){
                return this == o ||
                   (o != null &&
                    getClass() == o.getClass() &&
                    stopID.equals(((Stop) o).getStopID()));
            }

        };
    }

    static Vehicle asVehicle(final MTA mta, final VehiclePosition vehicle, final TripUpdate tripUpdate){
        return new Vehicle() {

            private final String vehicleID = requireNonNull(() -> vehicle.getVehicle().getLabel());

            private final Double latitude  = requireNonNull( () -> (double) vehicle.getPosition().getLatitude());
            private final Double longitude = requireNonNull( () -> (double) vehicle.getPosition().getLongitude());

            private final String status   = requireNonNull(() -> vehicle.getCurrentStatus().name());

            private final Integer stopID  = requireNonNull(() -> Integer.valueOf(vehicle.getStopId()));
            private final Integer routeID = requireNonNull(() -> Integer.valueOf(tripUpdate.getTrip().getRouteId()));

            private final Trip trip = asTrip(mta, tripUpdate, this);

            @Override
            public final String getVehicleID(){
                return vehicleID;
            }

            @Override
            public final Double getLatitude(){
                return latitude;
            }

            @Override
            public final Double getLongitude(){
                return longitude;
            }

            @Override
            public final String getCurrentStatus(){
                return status;
            }

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final Integer getRouteID(){
                return routeID;
            }

            // onemta methods

            private Stop stop = null;

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getMNRStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            private Route route = null;

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getMNRRoute(Objects.requireNonNull(routeID, "Route ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

        };
    }

    static Trip asTrip(final MTA mta, final TripUpdate tripUpdate, final Vehicle referringVehicle){
        return new Trip() {

            private final Vehicle vehicle = referringVehicle;

            private final String tripID  = requireNonNull(() -> tripUpdate.getTrip().getTripId());
            private final String routeID = requireNonNull(() -> tripUpdate.getTrip().getRouteId());

            private final List<TripStop> tripStops;

            {
                final List<TripStop> stops = new ArrayList<>();
                for(final TripUpdate.StopTimeUpdate update : tripUpdate.getStopTimeUpdateList())
                    stops.add(asTripStop(mta, update, this));
                tripStops = Collections.unmodifiableList(stops);
            }

            @Override
            public final String getTripID(){
                return tripID;
            }

            @Override
            public final String getRouteID(){
                return routeID;
            }

            // onemta methods

            @Override
            public final Route getRoute(){
                return vehicle.getRoute();
            }

            @Override
            public final Vehicle getVehicle(){
                return vehicle;
            }

            @Override
            public final TripStop[] getTripStops(){
                return tripStops.toArray(new TripStop[0]);
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        final MnrStopTimeUpdate mnrStopTimeUpdate = stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate);
        return new TripStop() {

            private final Trip trip      = referringTrip;

            private final Integer stopID = requireNonNull(() -> Integer.valueOf(stopTimeUpdate.getStopId()));

            private final Long arrival   = requireNonNull(() -> stopTimeUpdate.getArrival().getTime());
            private final Long departure = requireNonNull(() -> stopTimeUpdate.getDeparture().getTime());
            private final Integer delay  = requireNonNull(() -> stopTimeUpdate.getDeparture().getDelay());

            private final String track   = requireNonNull(mnrStopTimeUpdate::getTrack);
            private final String status  = requireNonNull(mnrStopTimeUpdate::getTrainStatus);

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final Long getArrivalTimeEpochMillis(){
                return arrival;
            }

            @Override
            public final Date getArrivalTime(){
                return arrival != null ? new Date(arrival) : null;
            }

            @Override
            public final Long getDepartureTimeEpochMillis(){
                return departure;
            }

            @Override
            public final Date getDepartureTime(){
                return departure != null ? new Date(departure) : null;
            }

            @Override
            public final Integer getDelay(){
                return delay;
            }

            @Override
            public final String getTrack(){
                return track;
            }

            @Override
            public final String getTrainStatus(){
                return status;
            }

            // onemta methods

            private Stop stop = null;

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getMNRStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

        };
    }

}