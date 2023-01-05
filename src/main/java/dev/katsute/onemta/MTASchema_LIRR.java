/*
 * Copyright (C) 2023 Katsute <https://github.com/Katsute>
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

@SuppressWarnings({"SpellCheckingInspection", "Java9CollectionFactory"})
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

            private List<Vehicle> vehicles = null;
            private List<LIRR.Alert> alerts = null;

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

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.lirr.getLIRR(),
                        ent ->
                            ent.hasTripUpdate() &&
                            isSameRoute(ent.getTripUpdate().getTrip().getRouteId()), // check if trip is this route
                        ent -> {
                            // find matching vehicle entity
                            final String id = ent.getTripUpdate().getVehicle().getLabel();
                            final FeedEntity veh = cast(mta).getFeedEntity(
                                cast(mta).service.lirr.getLIRR(),
                                vent ->
                                    vent.hasVehicle() &&
                                    Objects.equals(vent.getVehicle().getVehicle().getLabel(), id)
                            );

                            return MTASchema_LIRR.asVehicle(mta, veh.getVehicle(), ent.getTripUpdate(), null);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            @Override
            public final LIRR.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private LIRR.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getLIRR(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameRoute(alert.getInformedEntity(i).getRouteId())) // check if alert includes this route
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new LIRR.Alert[0]
                    )));
                }
                return alerts.toArray(new LIRR.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            @Override
            public final boolean isExactRoute(final Object object){
                if(object instanceof Route)
                    return getRouteID().toString().equalsIgnoreCase(((Route) object).getRouteID().toString());
                else if(object instanceof String)
                    return getRouteID().toString().equalsIgnoreCase(((String) object));
                else if(object instanceof Number)
                    return getRouteID().equals(object);
                else
                    return false;
            }

            @Override
            public final boolean isSameRoute(final Object object){
                return isExactRoute(object);
            }

            //

            @Override
            public final String toString(){
                return "LIRR.Route{" +
                       "routeID=" + routeID +
                       ", routeLongName='" + routeLongName + '\'' +
                       ", routeColor='" + routeColor + '\'' +
                       ", routeTextColor='" + routeTextColor + '\'' +
                       ", agency=" + agency +
                       '}';
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

            private List<Vehicle> vehicles = null;
            private List<LIRR.Alert> alerts = null;

            @Override
            public final Integer getStopID(){
                return stopID;
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

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.lirr.getLIRR(),
                        ent -> {
                            final int len = ent.getTripUpdate().getStopTimeUpdateCount();
                            for(int i = 0; i < len; i++)
                                if(isExactStop(ent.getTripUpdate().getStopTimeUpdate(i).getStopId())) // check if trip includes this stop
                                    return true;
                            return false;
                        },
                        ent -> {
                            // find matching vehicle entity
                            final String id = ent.getTripUpdate().getVehicle().getLabel();
                            final FeedEntity veh = cast(mta).getFeedEntity(
                                cast(mta).service.lirr.getLIRR(),
                                vent ->
                                    vent.hasVehicle() &&
                                    Objects.equals(vent.getVehicle().getVehicle().getLabel(), id)
                            );

                            return MTASchema_LIRR.asVehicle(mta, veh.getVehicle(), ent.getTripUpdate(), null);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }


            @Override
            public final LIRR.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private LIRR.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getLIRR(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameStop(alert.getInformedEntity(i).getStopId())) // check if alert includes this stop
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new LIRR.Alert[0]
                    )));
                }
                return alerts.toArray(new LIRR.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            @Override
            public final boolean isExactStop(final Object object){
                if(object instanceof Stop)
                    return getStopID().toString().equalsIgnoreCase(((Stop) object).getStopID().toString());
                else if(object instanceof String)
                    return getStopID().toString().equalsIgnoreCase(((String) object)) || getStopCode().equalsIgnoreCase(((String) object));
                else if(object instanceof Number)
                    return getStopID().equals(object) || getStopCode().equalsIgnoreCase(object.toString());
                else
                    return false;
            }

            @Override
            public final boolean isSameStop(final Object object){
                return isExactStop(object);
            }

            //

            @Override
            public final String toString(){
                return "LIRR.Stop{" +
                       "stopID=" + stopID +
                       ", stopCode='" + stopCode + '\'' +
                       ", stopName='" + stopName + '\'' +
                       ", stopDesc='" + stopDesc + '\'' +
                       ", stopLat=" + stopLat +
                       ", stopLon=" + stopLon +
                       ", wheelchairAccessible=" + wheelchairAccessible +
                       '}';
            }

        };
    }

    static Vehicle asVehicle(final MTA mta, final VehiclePosition vehicle, final TripUpdate tripUpdate, final Route optionalRoute){
        return new Vehicle(){

            private final String vehicleID = requireNonNull(() -> vehicle.getVehicle().getLabel());

            private Double latitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLatitude()));
            private Double longitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLongitude()));
            private Double bearing = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getBearing()));

            private String status = requireNonNull(() -> vehicle.getCurrentStatus().name());

            private Integer stopID = requireNonNull(() -> Integer.valueOf(vehicle.getStopId()));
            private Stop stop = null;

            private Integer routeID = requireNonNull(() -> Integer.valueOf(vehicle.getTrip().getRouteId()));
            private Route route = optionalRoute;

            private Trip trip = asTrip(mta, tripUpdate, this);

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
            public final String getStatus(){
                return status;
            }

            @Override
            public final Integer getRouteID(){
                return routeID;
            }

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getLIRRRoute(routeID));
            }

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getLIRRStop(stopID));
            }

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            private Trip getTrip(final boolean update){
                return !update ? trip : (trip = mta.getLIRRTrain(vehicleID).getTrip());
            }

            @Override
            public final void refresh(){
                getTrip(true);

                final Vehicle vehicle = mta.getLIRRTrain(vehicleID);

                latitude = vehicle.getLatitude();
                longitude = vehicle.getLongitude();
                bearing = vehicle.getBearing();
                status = vehicle.getStatus();
                routeID = vehicle.getRouteID();
                route = null;
                stopID = vehicle.getStopID();
                stop = null;
            }

            //

            @Override
            public final String toString(){
                return "LIRR.Vehicle{" +
                       "vehicleID='" + vehicleID + '\'' +
                       ", latitude=" + latitude +
                       ", longitude=" + longitude +
                       ", bearing=" + bearing +
                       ", status='" + status + '\'' +
                       ", stopID=" + stopID +
                       ", routeID='" + routeID + '\'' +
                       ", trip=" + trip +
                       '}';
            }

        };
    }

    static Trip asTrip(final MTA mta, final TripUpdate tripUpdate, final Vehicle referringVehicle){
        return new Trip(){

            private final String tripID = requireNonNull(() -> tripUpdate.getTrip().getTripId());
            private final Integer route = requireNonNull(() -> Integer.valueOf(tripUpdate.getTrip().getRouteId()));

            private final List<TripStop> stops;

            private final RailroadDirection direction = requireNonNull(() -> RailroadDirection.asDirection(tripUpdate.getTrip().getDirectionId()));

            private final String scheduleRelationship = requireNonNull(() -> tripUpdate.getTrip().getScheduleRelationship().name());

            private final Vehicle vehicle = referringVehicle;

            {
                final List<TripStop> stops = new ArrayList<>();
                for(final TripUpdate.StopTimeUpdate update : tripUpdate.getStopTimeUpdateList())
                    stops.add(asTripStop(mta, update, this));
                this.stops = Collections.unmodifiableList(stops);
            }

            @Override
            public final String getTripID(){
                return tripID;
            }

            @Override
            public final Integer getRouteID(){
                return route;
            }

            @Override
            public final Route getRoute(){
                return vehicle.getRoute();
            }

            @Override
            public final TripStop[] getTripStops(){
                return stops.toArray(new TripStop[0]);
            }

            @Override
            public final RailroadDirection getDirection(){
                return direction;
            }

            @Override
            public final String getScheduleRelationship(){
                return scheduleRelationship;
            }

            @Override
            public final Vehicle getVehicle(){
                return referringVehicle;
            }

            //

            @Override
            public final String toString(){
                return "LIRR.Trip{" +
                       "tripID='" + tripID + '\'' +
                       ", route=" + route +
                       ", stops=" + stops +
                       ", direction=" + direction +
                       ", scheduleRelationship='" + scheduleRelationship + '\'' +
                       '}';
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        return new TripStop(){

            private final Integer stopID = requireNonNull(() -> Integer.valueOf(stopTimeUpdate.getStopId()));

            private Stop stop = null;

            private final Long arrival = requireNonNull(() -> stopTimeUpdate.getArrival().getTime() * 1000);
            private final Long departure = requireNonNull(() -> stopTimeUpdate.getDeparture().getTime() * 1000);

            private final Integer sequence = requireNonNull(stopTimeUpdate::getStopSequence);

            private final Integer delay = requireNonNull(() -> stopTimeUpdate.getDeparture().getDelay());

            private final String scheduleRelationship = requireNonNull(() -> stopTimeUpdate.getScheduleRelationship().name());

            private final String track = requireNonNull(() -> stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate).getTrack());

            private final String status = requireNonNull(() -> stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate).getTrainStatus());

            private final Trip trip = referringTrip;

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getLIRRStop(stopID));
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
            public final Integer getStopSequence(){
                return sequence;
            }

            @Override
            public final Integer getDelay(){
                return delay;
            }

            @Override
            public final String getScheduleRelationship(){
                return scheduleRelationship;
            }

            @Override
            public final String getTrack(){
                return track;
            }

            @Override
            public final String getStatus(){
                return status;
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

            //

            @Override
            public final String toString(){
                return "LIRR.TripStop{" +
                       "stopID=" + stopID +
                       ", arrival=" + arrival +
                       ", departure=" + departure +
                       ", sequence=" + sequence +
                       ", delay=" + delay +
                       ", scheduleRelationship='" + scheduleRelationship + '\'' +
                       ", track='" + track + '\'' +
                       ", status='" + status + '\'' +
                       '}';
            }

        };
    }

    static LIRR.Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new LIRR.Alert(){

            private final String alertID = requireNonNull(feedEntity::getId);

            private final List<TransitAlertPeriod> periods;

            private final List<Integer> routeIDs;
            private List<Route> routes = null;

            private final List<Integer> stopIDs;
            private List<Stop> stops = null;

            private final String header = requireNonNull(() -> alert.getHeaderText().getTranslation(0).getText());
            private final String description = requireNonNull(() -> alert.getDescriptionText().getTranslation(0).getText());

            private final Long createdAt = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getCreatedAt() * 1000);
            private final Long updatedAt = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getUpdatedAt() * 1000);

            private final String type = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getAlertType());

            {
                final List<TransitAlertPeriod> periods = new ArrayList<>();
                for(final GTFSRealtimeProto.TimeRange range : alert.getActivePeriodList())
                    periods.add(asTransitAlertTimeframe(range));
                this.periods = Collections.unmodifiableList(periods);

                final List<Integer> routes = new ArrayList<>();
                final List<Integer> stops = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        routes.add(Integer.valueOf(entity.getRouteId()));
                    else if(entity.hasStopId())
                        stops.add(Integer.valueOf(entity.getStopId()));
                }
                this.routeIDs = Collections.unmodifiableList(routes);
                this.stopIDs  = Collections.unmodifiableList(stops);
            }

            @Override
            public final String getAlertID(){
                return alertID;
            }

            @Override
            public final TransitAlertPeriod[] getActivePeriods(){
                return periods.toArray(new TransitAlertPeriod[0]);
            }

            @Override
            public final Integer[] getRouteIDs(){
                return routeIDs.toArray(new Integer[0]);
            }

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
                return header;
            }

            @Override
            public final String getDescription(){
                return description;
            }

            @Override
            public final Date getCreatedAt(){
                return createdAt != null ? new Date(createdAt) : null;
            }

            @Override
            public final Long getCreatedAtEpochMillis(){
                return createdAt;
            }

            @Override
            public final Date getUpdatedAt(){
                return updatedAt != null ? new Date(updatedAt) : null;
            }

            @Override
            public final Long getUpdatedAtEpochMillis(){
                return updatedAt;
            }

            @Override
            public final String getAlertType(){
                return type;
            }

            //

            @Override
            public final String toString(){
                return "LIRR.Alert{" +
                       "alertID='" + alertID + '\'' +
                       ", periods=" + periods +
                       ", routeIDs=" + routeIDs +
                       ", stopIDs=" + stopIDs +
                       ", header='" + header + '\'' +
                       ", description='" + description + '\'' +
                       ", createdAt=" + createdAt +
                       ", updatedAt=" + updatedAt +
                       ", type='" + type + '\'' +
                       '}';
            }

        };
    }

}
