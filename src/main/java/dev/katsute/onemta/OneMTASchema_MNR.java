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
import dev.katsute.onemta.types.VehicleStatus;

import java.util.*;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.MNRRProto.*;
import static dev.katsute.onemta.railroad.MNR.*;

@SuppressWarnings("SpellCheckingInspection")
abstract class OneMTASchema_MNR extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final int route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("routes.csv");
        final List<String> row      = csv.getRow("route_id", String.valueOf(route_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find MNR route with id '" + route_id + "'");

        return new Route() {

            private final int routeID           = route_id;
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final TransitAgency agency = asAgency(row.get(csv.getHeaderIndex("agency_id")), resource);

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
                return new Vehicle[0];
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

    static Stop asStop(final OneMTA mta, final String stop_code){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("stops.csv");
        final List<String> row      = csv.getRow("stop_code", stop_code.toUpperCase());

        // instantiate
         Objects.requireNonNull(row, "Failed to find MNR stop with stopcode '" + stop_code.toUpperCase() + "'");

         return asStop(mta, Integer.parseInt(row.get(csv.getHeaderIndex("stop_id"))));
    }

    static Stop asStop(final OneMTA mta, final int stop_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.MetroNorthRailroad);
        final CSV csv               = resource.getData("stops.csv");
        final List<String> row      = csv.getRow("stop_id", String.valueOf(stop_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find MNR stop with id '" + stop_id + "'");

        return new Stop(){

            private final Integer stopID  = stop_id;
            private final String stopCode = row.get(csv.getHeaderIndex("stop_code"));
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));

            private final Double stopLat  = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lat")));
            private final Double stopLon  = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lon")));

            private final Boolean wheelchairAccessible = !row.get(csv.getHeaderIndex("wheelchair_boarding")).equals("2");

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
                return new Vehicle[0];
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

    static Vehicle asVehicle(final OneMTA mta, final VehiclePosition vehicle, final TripUpdate tripUpdate){
        return new Vehicle() {

            private final Double latitude  = (double) vehicle.getPosition().getLatitude();
            private final Double longitude = (double) vehicle.getPosition().getLongitude();

            private final VehicleStatus status = VehicleStatus.asStatus(vehicle.getCurrentStatus().getNumber());

            private final int stopID = Integer.parseInt(vehicle.getStopId());

            private final int routeID = Integer.parseInt(tripUpdate.getTrip().getRouteId());

            private final Trip trip = asTrip(mta, tripUpdate, this);

            @Override
            public final Double getLatitude(){
                return latitude;
            }

            @Override
            public final Double getLongitude(){
                return longitude;
            }

            @Override
            public final VehicleStatus getCurrentStatus(){
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
                return stop != null ? stop : (stop = mta.getMNRStop(stopID));
            }

            private Route route = null;

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getMNRRoute(routeID));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

        };
    }

    static Trip asTrip(final OneMTA mta, final TripUpdate tripUpdate, final Vehicle referringVehicle){
        return new Trip() {

            private final Vehicle vehicle = referringVehicle;

            private final String tripID  = tripUpdate.getTrip().getTripId();
            private final String routeID = tripUpdate.getTrip().getRouteId();

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
            public final TripStop[] getStopUpdates(){
                return tripStops.toArray(new TripStop[0]);
            }

        };
    }

    static TripStop asTripStop(final OneMTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        final MnrStopTimeUpdate mnrStopTimeUpdate = stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate);
        return new TripStop() {

            private final Trip trip      = referringTrip;

            private final Integer stopID = Integer.parseInt(stopTimeUpdate.getStopId());
            private final Long arrival   = stopTimeUpdate.getArrival().getTime();
            private final Long departure = stopTimeUpdate.getDeparture().getTime();
            private final Integer delay  = stopTimeUpdate.getDeparture().getDelay();
            private final String track   = mnrStopTimeUpdate.getTrack();
            private final String status  = mnrStopTimeUpdate.getTrainStatus();

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
                return new Date(arrival);
            }

            @Override
            public final Long getDepartureTimeEpochMillis(){
                return departure;
            }

            @Override
            public final Date getDepartureTime(){
                return new Date(departure);
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
                return stop != null ? stop : (stop = mta.getMNRStop(stopID));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

        };
    }

}
