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

import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

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

            private List<Vehicle> vehicles = null;

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final FeedMessage feed = cast(mta).service.mnr.getMNR(cast(mta).subwayToken);
                    final int len          = feed.getEntityCount();

                    final List<Vehicle> vehicles = new ArrayList<>();
                    for(int i = 0; i < len; i++){
                        final FeedEntity entity = feed.getEntity(i);
                        // only include trips with vehicles
                        if(entity.hasVehicle() && entity.hasTripUpdate())
                            // only include vehicles on this route
                            if(Integer.parseInt(entity.getTripUpdate().getTrip().getRouteId()) == routeID)
                                vehicles.add(asVehicle(mta, entity.getVehicle(), entity.getTripUpdate(), this));
                    }

                    this.vehicles = Collections.unmodifiableList(vehicles);
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            private List<MNR.Alert> alerts = null;

            @Override
            public final MNR.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private MNR.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    final List<MNR.Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getMNR(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final MNR.Alert alert = MTASchema_MNR.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getRouteIDs()).contains(route_id))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new MNR.Alert[0]);
            }

            // onemta methods

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

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.Route{" +
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

            // static data

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

            // live feed

            private List<Vehicle> vehicles = null;

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final String stop = String.valueOf(stopID);

                    final FeedMessage feed = cast(mta).service.mnr.getMNR(cast(mta).subwayToken);
                    final int len          = feed.getEntityCount();

                    final List<Vehicle> vehicles = new ArrayList<>();
                    OUTER:
                    for(int i = 0; i < len; i++){
                        final FeedEntity entity = feed.getEntity(i);
                        // only include trips with vehicles
                        if(
                            entity.hasVehicle() &&
                            entity.hasTripUpdate()
                        ){
                            if(entity.getTripUpdate().getStopTimeUpdateCount() > 0){
                                final TripUpdate tu = entity.getTripUpdate();
                                final int len2 = tu.getStopTimeUpdateCount();
                                // check all stops on train route
                                for(int u = 0; u < len2; u++){
                                    final TripUpdate.StopTimeUpdate stu = tu.getStopTimeUpdate(u);
                                    // check if this stop is en route
                                    if(stu.getStopId().equalsIgnoreCase(stop)){
                                        vehicles.add(asVehicle(mta, entity.getVehicle(), entity.getTripUpdate(), null));
                                        continue OUTER;
                                    }
                                }
                            }
                        }
                    }

                    this.vehicles = Collections.unmodifiableList(vehicles);
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            private List<MNR.Alert> alerts = null;

            @Override
            public final MNR.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private MNR.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    final List<MNR.Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getMNR(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final MNR.Alert alert = MTASchema_MNR.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getStopIDs()).contains(stopID))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new MNR.Alert[0]);
            }

            // onemta methods

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

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.Stop{" +
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
        return new Vehicle() {

            private final String vehicleID = requireNonNull(() -> vehicle.getVehicle().getLabel());

            private Double latitude  = requireNonNull( () -> Double.valueOf(vehicle.getPosition().getLatitude()));
            private Double longitude = requireNonNull( () -> Double.valueOf(vehicle.getPosition().getLongitude()));

            private String status   = requireNonNull(() -> vehicle.getCurrentStatus().name());

            private Integer stopID  = requireNonNull(() -> Integer.valueOf(vehicle.hasStopId() ? vehicle.getStopId() : tripUpdate.getStopTimeUpdate(0).getStopId())); // fallback to next trip stop if stop id is not working
            private Integer routeID = requireNonNull(() -> Integer.valueOf(tripUpdate.getTrip().getRouteId()));

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

            private Route route = optionalRoute;

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getMNRRoute(Objects.requireNonNull(routeID, "Route ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            private Trip getTrip(final boolean update){
                return !update ? trip : (trip = mta.getMNRTrain(vehicleID).getTrip());
            }

            @Override
            public final void refresh(){
                getTrip(true);

                final Vehicle vehicle = mta.getMNRTrain(vehicleID);
                latitude  = vehicle.getLatitude();
                longitude = vehicle.getLongitude();
                status    = vehicle.getCurrentStatus();
                stopID    = vehicle.getStopID();
                routeID   = vehicle.getRouteID();
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.Vehicle{" +
                       "vehicleID='" + vehicleID + '\'' +
                       ", latitude=" + latitude +
                       ", longitude=" + longitude +
                       ", status='" + status + '\'' +
                       ", stopID=" + stopID +
                       ", routeID=" + routeID +
                       '}';
            }

        };
    }

    static Trip asTrip(final MTA mta, final TripUpdate tripUpdate, final Vehicle referringVehicle){
        return new Trip() {

            private final Vehicle vehicle = referringVehicle;

            private final String tripID  = requireNonNull(() -> tripUpdate.getTrip().getTripId());
            private final String routeID = requireNonNull(() -> tripUpdate.getTrip().getRouteId());

            private final String scheRel = requireNonNull(() -> tripUpdate.getTrip().getScheduleRelationship().name());

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
            public final String getScheduleRelationship(){
                return scheRel;
            }

            @Override
            public final TripStop[] getTripStops(){
                return tripStops.toArray(new TripStop[0]);
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.Trip{" +
                       "tripID='" + tripID + '\'' +
                       ", routeID='" + routeID + '\'' +
                       ", scheduleRelationship='" + scheRel + '\'' +
                       '}';
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        final MnrStopTimeUpdate mnrStopTimeUpdate = stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate);
        return new TripStop() {

            private final Trip trip      = referringTrip;

            private final Integer stopID = requireNonNull(() -> Integer.valueOf(stopTimeUpdate.getStopId()));

            private final Long arrival   = requireNonNull(() -> stopTimeUpdate.getArrival().getTime() * 1000);
            private final Long departure = requireNonNull(() -> stopTimeUpdate.getDeparture().getTime() * 1000);
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
                return stop != null ? stop : (stop = mta.getMNRStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.TripStop{" +
                       "stopID=" + stopID +
                       ", arrival=" + arrival +
                       ", departure=" + departure +
                       ", delay=" + delay +
                       ", track='" + track + '\'' +
                       ", status='" + status + '\'' +
                       '}';
            }

        };
    }

    static MNR.Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new MNR.Alert() {

            private final String alertID = requireNonNull(feedEntity::getId);

            private final String headerText      = requireNonNull(() -> alert.getHeaderText().getTranslation(0).getText());
            private final String descriptionText = requireNonNull(() -> alert.getDescriptionText().getTranslation(0).getText());

            private final String alertType = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getAlertType());

            private final String effect = requireNonNull(() -> alert.getEffect().name());

            private final List<TransitAlertPeriod> alertPeriods;
            private final List<Integer> routeIDs;
            private final List<Integer> stopIDs;

            {
                final List<TransitAlertPeriod> alertPeriods = new ArrayList<>();
                for(final GTFSRealtimeProto.TimeRange range : alert.getActivePeriodList())
                    alertPeriods.add(asTransitAlertTimeframe(range));
                this.alertPeriods = Collections.unmodifiableList(alertPeriods);

                final List<Integer> routeIDs = new ArrayList<>();
                final List<Integer> stopIDs = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        try{
                            routeIDs.add(Integer.valueOf(entity.getRouteId()));
                        }catch(final NumberFormatException ignored){ }
                    else if(entity.hasStopId())
                        try{
                            stopIDs.add(Integer.valueOf(entity.getStopId()));
                        }catch(final NumberFormatException ignored){ }
                }
                this.routeIDs = Collections.unmodifiableList(routeIDs);
                this.stopIDs  = Collections.unmodifiableList(stopIDs);
            }

            @Override
            public final String getAlertID(){
                return alertID;
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
                        routes.add(mta.getMNRRoute(id));
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
                        stops.add(mta.getMNRStop(id));
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

            @Override
            public final String getEffect(){
                return effect;
            }

            // Java

            @Override
            public final String toString(){
                return "MNR.Alert{" +
                       "alertID='" + alertID + '\'' +
                       ", headerText='" + headerText + '\'' +
                       ", descriptionText='" + descriptionText + '\'' +
                       ", alertType='" + alertType + '\'' +
                       ", effect='" + effect + '\'' +
                       ", alertPeriods=" + alertPeriods +
                       ", routeIDs=" + routeIDs +
                       ", stopIDs=" + stopIDs +
                       '}';
            }

        };
    }

}
