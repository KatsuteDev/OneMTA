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

import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.subway.SubwayDirection;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.util.*;
import java.util.regex.Pattern;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.subway.Subway.*;

@SuppressWarnings({"Java9CollectionFactory"})
abstract class MTASchema_Subway extends MTASchema {

    private static final Pattern direction = Pattern.compile("[NS]$", Pattern.CASE_INSENSITIVE);

    static String stripDirection(final String stop){
        return direction.matcher(stop).replaceAll("");
    }

    private static SubwayDirection getDirection(final String stop){
        final String stopOnly = stripDirection(stop);
        return stop.length() == stopOnly.length() || (!stop.toUpperCase().endsWith("N") && !stop.toUpperCase().endsWith("S"))
               ? null
               : stop.toUpperCase().endsWith("N")
                    ? SubwayDirection.NORTH
                    : SubwayDirection.SOUTH;
    }

    private static final Pattern express = Pattern.compile("X$", Pattern.CASE_INSENSITIVE);

    static String stripExpress(final String route){
        return express.matcher(route).replaceAll("");
    }

    static String resolveSubwayLine(final String route_id){
        if(route_id == null) return null;

        final String route = route_id.toUpperCase();

        switch(route){
            case "A":
            case "C":
            case "E":

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

            case "FX":
            case "5X":
            case "6X":
            case "7X":
                return route;
            case "FS":
            case "SF":
            case "SR":
                return "FS";
            case "9":
                return "GS";
            case "S":
            case "SI":
            case "SIR":
                return "SI";
            default:
                return null;
        }
    }

    //

    static Route asRoute(final MTA mta, final String route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.Subway);
        final CSV csv = resource.getData("routes.txt");
        final List<String> row = csv.getRow("route_id", resolveSubwayLine(route_id));

        // instantiate
        Objects.requireNonNull(row, "Failed to find subway route with id '" + route_id + "'");

        return new Route() {

            private final String routeID = resolveSubwayLine(route_id);
            private final String routeShortName = row.get(csv.getHeaderIndex("route_short_name"));
            private final String routeLongName = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeDesc = row.get(csv.getHeaderIndex("route_desc"));
            private final String routeColor = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final TransitAgency agency = asAgency(row.get(csv.getHeaderIndex("agency_id")), resource);

            private List<Vehicle> vehicles = null;
            private List<Subway.Alert> alerts = null;

            @Override
            public final String getRouteID(){
                return routeID;
            }

            @Override
            public final String getRouteShortName(){
                return routeShortName;
            }

            @Override
            public final String getRouteName(){
                return routeLongName;
            }

            @Override
            public final String getRouteDescription(){
                return routeDesc;
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

            @SuppressWarnings("DataFlowIssue")
            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final FeedMessage feed = cast(mta).resolveSubwayFeed(routeID);
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        feed,
                        ent ->
                            ent.hasTripUpdate() &&
                            isExactRoute(ent.getTripUpdate().getTrip().getRouteId()), // check if trip is this route
                        ent -> {
                            // find matching vehicle entity
                            final String id = ent.getTripUpdate().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId();
                            final FeedEntity veh = cast(mta).getFeedEntity(
                                feed,
                                vent ->
                                    vent.hasVehicle() &&
                                    Objects.equals(vent.getVehicle().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId(), id)
                            );

                            return MTASchema_Subway.asVehicle(mta, veh.getVehicle(), ent.getTripUpdate(), this);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            @Override
            public final Subway.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Subway.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getSubway(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameRoute(alert.getInformedEntity(i).getRouteId())) // check if alert includes this route
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Subway.Alert[0]
                    )));
                }
                return alerts.toArray(new Subway.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            @Override
            public final boolean isExactRoute(final Object object){
                if(object instanceof Route)
                    return getRouteID() != null && getRouteID().equalsIgnoreCase(((Route) object).getRouteID());
                else if(object instanceof String)
                    return getRouteID() != null && getRouteID().equalsIgnoreCase(((String) object));
                else if(object instanceof Number)
                    return getRouteID() != null && getRouteID().equalsIgnoreCase(object.toString());
                else
                    return false;
            }

            @Override
            public final boolean isSameRoute(final Object object){
                if(object instanceof Route)
                    return resolveSubwayLine(getRouteID()) != null &&
                           resolveSubwayLine(((Route) object).getRouteID()) != null &&
                           stripExpress(resolveSubwayLine(getRouteID())).equalsIgnoreCase(stripExpress(resolveSubwayLine(((Route) object).getRouteID())));
                else if(object instanceof String)
                    return resolveSubwayLine(getRouteID()) != null &&
                           resolveSubwayLine((String) object) != null &&
                           stripExpress(resolveSubwayLine(getRouteID())).equalsIgnoreCase(stripExpress(resolveSubwayLine((String) object)));
                else if(object instanceof Number)
                    return resolveSubwayLine(getRouteID()) != null &&
                           resolveSubwayLine(object.toString()) != null &&
                           stripExpress(resolveSubwayLine(getRouteID())).equalsIgnoreCase(stripExpress(resolveSubwayLine(object.toString())));
                else
                    return false;
            }

            //

            @Override
            public final String toString(){
                return "Subway.Route{" +
                       "routeID='" + routeID + '\'' +
                       ", routeShortName='" + routeShortName + '\'' +
                       ", routeLongName='" + routeLongName + '\'' +
                       ", routeDesc='" + routeDesc + '\'' +
                       ", routeColor='" + routeColor + '\'' +
                       ", routeTextColor='" + routeTextColor + '\'' +
                       ", agency=" + agency +
                       '}';
            }

        };
    }

    static Stop asStop(final MTA mta, final String stop_id){
        final String stop = stop_id.toUpperCase();
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.Subway);
        final CSV csv = resource.getData("stops.txt");
        final List<String> row = csv.getRow("stop_id", stop);

        // instantiate
        Objects.requireNonNull(row, "Failed to find subway stop with id '" + stop + "'");

        return new Stop() {

            private final String stopID = stop;
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));

            private final Double stopLat = Double.valueOf(row.get(csv.getHeaderIndex("stop_lat")));
            private final Double stopLon = Double.valueOf(row.get(csv.getHeaderIndex("stop_lon")));

            private final SubwayDirection stopDirection = MTASchema_Subway.getDirection(stopID);

            private List<Vehicle> vehicles = null;
            private List<Subway.Alert> alerts = null;

            @Override
            public final String getStopID(){
                return stopID;
            }

            @Override
            public final String getStopName(){
                return stopName;
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
            public final SubwayDirection getDirection(){
                return stopDirection;
            }

            private List<Stop> transfers = null;

            @Override
            public final Stop[] getTransfers(){
                if(transfers == null){
                    final List<Stop> transfers = new ArrayList<>();
                    final String raw = stripDirection(stopID);
                    for(final String value : resource.getData("transfers.txt").getValues("from_stop_id", raw, "to_stop_id"))
                        if(!value.equalsIgnoreCase(raw))
                            transfers.add(mta.getSubwayStop(value));
                    this.transfers = transfers;
                }
                return transfers.toArray(new Stop[0]);
            }

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            @SuppressWarnings("DataFlowIssue")
            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final FeedMessage feed = cast(mta).resolveSubwayFeed(stop_id.substring(0, 1));
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        feed,
                        ent -> {
                            final int len = ent.getTripUpdate().getStopTimeUpdateCount();
                            for(int i = 0; i < len; i++)
                                if(isSameStop(ent.getTripUpdate().getStopTimeUpdate(i).getStopId())) // check if trip includes this stop
                                    return true;
                            return false;
                        },
                        ent -> {
                            // find matching vehicle entity
                            final String id = ent.getTripUpdate().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId();
                            final FeedEntity veh = cast(mta).getFeedEntity(
                                feed,
                                vent ->
                                    vent.hasVehicle() &&
                                    Objects.equals(vent.getVehicle().getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId(), id)
                            );

                            return MTASchema_Subway.asVehicle(mta, veh.getVehicle(), ent.getTripUpdate(), null);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            @Override
            public final Subway.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Subway.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getSubway(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameStop(alert.getInformedEntity(i).getStopId())) // check if alert includes this stop
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Subway.Alert[0]
                    )));
                }
                return alerts.toArray(new Subway.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            @Override
            public final boolean isExactStop(final Object object){
                if(object instanceof Stop)
                    return getStopID().equalsIgnoreCase(((Stop) object).getStopID());
                else if(object instanceof String)
                    return getStopID().equalsIgnoreCase(((String) object));
                else if(object instanceof Number)
                    return getStopID().equalsIgnoreCase(object.toString());
                else
                    return false;
            }

            @Override
            public final boolean isSameStop(final Object object){
                if(object instanceof Stop)
                    return stripDirection(getStopID()).equalsIgnoreCase(stripDirection(((Stop) object).getStopID()));
                else if(object instanceof String)
                    return stripDirection(getStopID()).equalsIgnoreCase(stripDirection(((String) object)));
                else if(object instanceof Number)
                    return stripDirection(getStopID()).equalsIgnoreCase(object.toString());
                else
                    return false;
            }

            //

            @Override
            public final String toString(){
                return "Subway.Stop{" +
                       "stopID='" + stopID + '\'' +
                       ", stopName='" + stopName + '\'' +
                       ", stopLat=" + stopLat +
                       ", stopLon=" + stopLon +
                       ", stopDirection=" + stopDirection +
                       '}';
            }

        };
    }

    static Vehicle asVehicle(final MTA mta, final VehiclePosition vehicle, final TripUpdate tripUpdate, final Route optionalRoute){
        return new Vehicle(){

            private final String vehicleID = requireNonNull(() -> vehicle.getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getTrainId());

            private Boolean assigned = requireNonNull(() -> vehicle.getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getIsAssigned());

            @SuppressWarnings("DataFlowIssue")
            private Boolean rerouted = requireNonNull(() -> vehicleID.charAt(0) == '=');

            @SuppressWarnings("DataFlowIssue")

            private Boolean skipStop = requireNonNull(() -> vehicleID.charAt(0) == '/');

            @SuppressWarnings("DataFlowIssue")

            private Boolean turnTrain = requireNonNull(() -> vehicleID.charAt(0) == '$');

            private String status = requireNonNull(() -> vehicle.getCurrentStatus().name());

            private Integer sequence = requireNonNull(vehicle::getCurrentStopSequence);

            private String stopID = requireNonNull(vehicle::getStopId);
            private Stop stop = null;

            private String routeID = requireNonNull(() -> vehicle.getTrip().getRouteId());
            private Route route = optionalRoute;

            private Boolean express = requireNonNull(() -> routeID.toUpperCase().endsWith("X"));

            private Trip trip = asTrip(mta, tripUpdate, this);

            @Override
            public final String getVehicleID(){
                return vehicleID;
            }

            @Override
            public final Boolean isAssigned(){
                return assigned;
            }

            @Override
            public final Boolean isRerouted(){
                return rerouted;
            }

            @Override
            public final Boolean isSkipStop(){
                return skipStop;
            }

            @Override
            public final Boolean isTurnTrain(){
                return turnTrain;
            }

            @Override
            public final String getStatus(){
                return status;
            }

            @Override
            public final Integer getStopSequence(){
                return sequence;
            }

            @Override
            public final Boolean isExpress(){
                return express;
            }

            @Override
            public final String getRouteID(){
                return routeID;
            }

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getSubwayRoute(routeID));
            }

            @Override
            public final String getStopID(){
                return stopID;
            }

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getSubwayStop(stopID));
            }

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            private Trip getTrip(final boolean update){
                return !update ? trip : (trip = mta.getSubwayTrain(vehicleID).getTrip());
            }

            @Override
            public final void refresh(){
                getTrip(true);

                final Vehicle vehicle = mta.getSubwayTrain(vehicleID);

                assigned = vehicle.isAssigned();
                rerouted = vehicle.isRerouted();
                skipStop = vehicle.isSkipStop();
                turnTrain = vehicle.isTurnTrain();
                status = vehicle.getStatus();
                sequence = vehicle.getStopSequence();
                express = vehicle.isExpress();
                routeID = vehicle.getRouteID();
                route = null;
                stopID = vehicle.getStopID();
                stop = null;
            }

            //

            @Override
            public final String toString(){
                return "Subway.Vehicle{" +
                       "vehicleID='" + vehicleID + '\'' +
                       ", assigned=" + assigned +
                       ", rerouted=" + rerouted +
                       ", skipStop=" + skipStop +
                       ", turnTrain=" + turnTrain +
                       ", status='" + status + '\'' +
                       ", sequence=" + sequence +
                       ", stopID='" + stopID + '\'' +
                       ", routeID='" + routeID + '\'' +
                       ", express=" + express +
                       ", trip=" + trip +
                       '}';
            }

        };
    }

    static Trip asTrip(final MTA mta, final TripUpdate tripUpdate, final Vehicle referringVehicle){
        return new Trip(){

            private final String tripID = requireNonNull(() -> tripUpdate.getTrip().getTripId());
            private final String route = requireNonNull(() -> tripUpdate.getTrip().getRouteId());

            private final List<TripStop> stops;

            private final SubwayDirection direction = requireNonNull(() -> SubwayDirection.asDirection(tripUpdate.getTrip().getExtension(NYCTSubwayProto.nyctTripDescriptor).getDirection().getNumber()));

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
            public final String getRouteID(){
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
            public final SubwayDirection getDirection(){
                return direction;
            }

            @Override
            public final Vehicle getVehicle(){
                return referringVehicle;
            }

            //

            @Override
            public final String toString(){
                return "Subway.Trip{" +
                       "tripID='" + tripID + '\'' +
                       ", route='" + route + '\'' +
                       ", stops=" + stops +
                       ", direction=" + direction +
                       '}';
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        return new TripStop(){

            private final String stopID = requireNonNull(stopTimeUpdate::getStopId);

            private Stop stop = null;

            private final Long arrival = requireNonNull(() -> stopTimeUpdate.getArrival().getTime() * 1000);
            private final Long departure = requireNonNull(() -> stopTimeUpdate.getDeparture().getTime() * 1000);

            private final String scheduledTrack = requireNonNull(() -> stopTimeUpdate.getExtension(NYCTSubwayProto.nyctStopTimeUpdate).getScheduledTrack());
            private final String actualTrack = requireNonNull(() -> stopTimeUpdate.getExtension(NYCTSubwayProto.nyctStopTimeUpdate).getActualTrack());

            private final String status = requireNonNull(() -> stopTimeUpdate.getExtension(MNRRProto.mnrStopTimeUpdate).getTrainStatus());

            private final Trip trip = referringTrip;

            @Override
            public final String getStopID(){
                return stopID;
            }

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getSubwayStop(stopID));
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
            public final String getActualTrack(){
                return actualTrack;
            }

            @Override
            public final String getTrack(){
                return scheduledTrack;
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

            //

            @Override
            public final String toString(){
                return "Subway.TripStop{" +
                       "stopID='" + stopID + '\'' +
                       ", arrival=" + arrival +
                       ", departure=" + departure +
                       ", scheduledTrack='" + scheduledTrack + '\'' +
                       ", actualTrack='" + actualTrack + '\'' +
                       ", status='" + status + '\'' +
                       '}';
            }

        };
    }

    static Subway.Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new Subway.Alert(){

            private final String alertID = requireNonNull(feedEntity::getId);

            private final List<TransitAlertPeriod> periods;

            private final List<String> routeIDs;
            private List<Route> routes = null;

            private final List<String> stopIDs;
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

                final List<String> routes = new ArrayList<>();
                final List<String> stops = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        routes.add(entity.getRouteId());
                    else if(entity.hasStopId())
                        stops.add(entity.getStopId());
                }
                this.routeIDs = Collections.unmodifiableList(routes);
                this.stopIDs = Collections.unmodifiableList(stops);
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
            public final String[] getRouteIDs(){
                return routeIDs.toArray(new String[0]);
            }

            @Override
            public final Route[] getRoutes(){
                if(routes == null){
                    final List<Route> routes = new ArrayList<>();
                    for(final String id : routeIDs)
                        routes.add(mta.getSubwayRoute(id));
                    this.routes = Collections.unmodifiableList(routes);
                }
                return routes.toArray(new Route[0]);
            }

            @Override
            public final String[] getStopIDs(){
                return stopIDs.toArray(new String[0]);
            }

            @Override
            public final Stop[] getStops(){
                if(stops == null){
                    final List<Stop> stops = new ArrayList<>();
                    for(final String id : stopIDs)
                        stops.add(mta.getSubwayStop(id));
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
                return "Subway.Alert{" +
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