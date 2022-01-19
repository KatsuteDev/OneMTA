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

import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.RailroadDirection;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.util.*;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.railroad.LIRR.*;

@SuppressWarnings("SpellCheckingInspection")
abstract class MTASchema_LIRR extends MTASchema {

    static Route asRoute(final MTA mta, final int route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV csv               = resource.getData("routes.txt");
        final List<String> row      = csv.getRow("route_id", String.valueOf(route_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find LIRR route with id '" + route_id + "'");

        return new Route() {

            private final Integer routeID       = route_id;
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final TransitAgency agency = asAgency("LI", resource);

            private final List<Vehicle> vehicles;

            {
                final String route = String.valueOf(route_id);

                final FeedMessage feed = cast(mta).service.lirr.getLIRR(cast(mta).subwayToken);
                final int len          = feed.getEntityCount();

                TripUpdate tripUpdate = null;
                String tripVehicle    = null;

                final List<Vehicle> vehicles = new ArrayList<>();
                for(int i = 0; i < len; i++){
                    final FeedEntity entity = feed.getEntity(i);

                    // get next trip
                    if(entity.hasTripUpdate()){
                        if( // only include trips with vehicle and on this route
                            entity.getTripUpdate().hasVehicle() &&
                            entity.getTripUpdate().getTrip().getRouteId().equals(route)
                        ){
                            tripUpdate  = entity.getTripUpdate();
                            tripVehicle = tripUpdate.getVehicle().getLabel();
                        }
                    }else if( // get matching vehicle for trip
                        entity.hasVehicle() &&
                        entity.getVehicle().getVehicle().getLabel().equals(tripVehicle)
                    ){
                        vehicles.add(asVehicle(mta, entity.getVehicle(), tripUpdate));
                        tripUpdate  = null;
                        tripVehicle = null;
                    }
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

            private List<LIRR.Alert> alerts = null;

            @Override
            public final LIRR.Alert[] getAlerts(){
                if(alerts == null){
                    final List<LIRR.Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getLIRR(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final LIRR.Alert alert = MTASchema_LIRR.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getRouteIDs()).contains(route_id))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new LIRR.Alert[0]);
            }

            // Java

            @Override
            public final boolean equals(final Object o){
                return this == o ||
                   (o != null &&
                    getClass() == o.getClass() &&
                    route_id == ((Route) o) .getRouteID());
            }

        };
    }

    static Stop asStop(final MTA mta, final String stop_code){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV csv               = resource.getData("stops.txt");
        final List<String> row      = csv.getRow("stop_code", stop_code.toUpperCase());

        // instantiate
        Objects.requireNonNull(row, "Failed to find LIRR stop with stopcode '" + stop_code.toUpperCase() + "'");

        return asStop(mta, Integer.parseInt(row.get(csv.getHeaderIndex("stop_id"))));
    }

    static Stop asStop(final MTA mta, final int stop_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV csv               = resource.getData("stops.txt");
        final List<String> row      = csv.getRow("stop_id", String.valueOf(stop_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find LIRR stop with id '" + stop_id + "'");

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

                final FeedMessage feed = cast(mta).service.lirr.getLIRR(cast(mta).subwayToken);
                final int len          = feed.getEntityCount();

                TripUpdate tripUpdate = null;
                String tripVehicle    = null;

                final List<Vehicle> vehicles = new ArrayList<>();
                OUTER:
                for(int i = 0; i < len; i++){
                    final FeedEntity entity = feed.getEntity(i);

                    // get next trip
                    if(entity.hasTripUpdate()){
                        if( // only include trips at this stop
                            entity.getTripUpdate().hasVehicle() &&
                            entity.getTripUpdate().getStopTimeUpdateCount() > 0
                        ){
                            final TripUpdate tu = entity.getTripUpdate();
                            final int len2 = tu.getStopTimeUpdateCount();
                            // check all stops on train route
                            for(int u = 0; u < len2; u++){
                                final TripUpdate.StopTimeUpdate update = tu.getStopTimeUpdate(u);
                                // check if this stop is en route
                                if(update.getStopId().equalsIgnoreCase(stop)){
                                    tripUpdate  = entity.getTripUpdate();
                                    tripVehicle = tripUpdate.getVehicle().getLabel();
                                    continue OUTER;
                                }
                            }
                        }
                    }else if( // get matching vehicle for trip
                        entity.hasVehicle() &&
                        entity.getVehicle().getVehicle().getLabel().equals(tripVehicle)
                    ){
                        // only include vehicles at this stop
                        if(entity.getVehicle().getStopId().equals(stop)){
                            vehicles.add(asVehicle(mta, entity.getVehicle(), tripUpdate));
                            tripUpdate  = null;
                            tripVehicle = null;
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

            private List<LIRR.Alert> alerts = null;

            @Override
            public final LIRR.Alert[] getAlerts(){
                if(alerts == null){
                    final List<LIRR.Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getLIRR(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final LIRR.Alert alert = MTASchema_LIRR.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getStopIDs()).contains(stop_id))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new LIRR.Alert[0]);
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

            private final Double latitude  = requireNonNull(() -> (double) vehicle.getPosition().getLatitude());
            private final Double longitude = requireNonNull(() -> (double) vehicle.getPosition().getLongitude());
            private final Double bearing   = requireNonNull(() -> (double) vehicle.getPosition().getBearing());

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
            public final Double getBearing(){
                return bearing;
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
                return stop != null ? stop : (stop = mta.getLIRRStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            private Route route = null;

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getLIRRRoute(Objects.requireNonNull(routeID, "Route ID must not be null")));
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
            private final RailroadDirection direction = requireNonNull(() -> RailroadDirection.asDirection(tripUpdate.getTrip().getDirectionId()));

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

            @Override
            public final RailroadDirection getDirection(){
                return direction;
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
        final MNRRProto.MnrStopTimeUpdate mnrStopTimeUpdate = stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate);
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
            public final Date getArrivalTime(){
                return arrival != null ? new Date(arrival) : null;
            }

            @Override
            public final Long getArrivalTimeEpochMillis(){
                return arrival;
            }

            @Override
            public final Date getDepartureTime(){
                return departure != null ? new Date(departure) : null;
            }

            @Override
            public final Long getDepartureTimeEpochMillis(){
                return departure;
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
                return stop != null ? stop : (stop = mta.getLIRRStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

        };
    }

    static LIRR.Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new LIRR.Alert() {

            private final String ID = requireNonNull(feedEntity::getId);

            private final String headerText      = requireNonNull(() -> alert.getHeaderText().getTranslation(0).getText());
            private final String descriptionText = requireNonNull(() -> alert.getDescriptionText().getTranslation(0).getText());

            private final String alertType = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getAlertType());

            private final List<TransitAlertPeriod> alertPeriods;
            private final List<Integer> routeIDs;
            private final List<Integer> stopIDs;

            {
                final List<TransitAlertPeriod> alertPeriods = new ArrayList<>();
                for(final GTFSRealtimeProto.TimeRange range : alert.getActivePeriodList())
                    alertPeriods.add(asTransitAlertTimeframe(mta, range));
                this.alertPeriods = Collections.unmodifiableList(alertPeriods);

                final List<Integer> routeIDs = new ArrayList<>();
                final List<Integer> stopIDs = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        routeIDs.add(Integer.valueOf(entity.getRouteId()));
                    else if(entity.hasStopId())
                        stopIDs.add(Integer.valueOf(entity.getStopId()));
                }
                this.routeIDs = Collections.unmodifiableList(routeIDs);
                this.stopIDs  = Collections.unmodifiableList(stopIDs);
            }

            @Override
            public final String getID(){
                return ID;
            }

            @Override
            public final TransitAlertPeriod[] getActivePeriods(){
                return alertPeriods.toArray(new TransitAlertPeriod[0]);
            }

            @Override
            public final Integer[] getRouteIDs(){
                return routeIDs.toArray(new Integer[0]);
            }

            private List<Route> routes = null;

            @Override
            public final Route[] getRoutes(){
                if(routes == null){
                    final List<Route> routes = new ArrayList<>();
                    for(final Integer id : routeIDs)
                        routes.add(mta.getLIRRRoute(id));
                    this.routes = Collections.unmodifiableList(routes);
                }
                return routes.toArray(new Route[0]);
            }

            @Override
            public final Integer[] getStopIDs(){
                return stopIDs.toArray(new Integer[0]);
            }

            private List<Stop> stops = null;

            @Override
            public final Stop[] getStops(){
                if(stops == null){
                    final List<Stop> stops = new ArrayList<>();
                    for(final Integer id : stopIDs)
                        stops.add(mta.getLIRRStop(id));
                    this.stops = Collections.unmodifiableList(stops);
                }
                return stops.toArray(new Stop[0]);
            }

            @Override
            public final String getHeader(){
                return headerText;
            }

            @Override
            public final String getDescription(){
                return descriptionText;
            }

            @Override
            public final String getAlertType(){
                return alertType;
            }

        };
    }

}
