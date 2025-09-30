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
import dev.katsute.onemta.bus.BusDirection;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.util.*;
import java.util.regex.Pattern;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.bus.Bus.*;

@SuppressWarnings({"Convert2Diamond", "Java9CollectionFactory"})
abstract class MTASchema_Bus extends MTASchema {

    private static final Pattern type = Pattern.compile("^X|[XC+]$", Pattern.CASE_INSENSITIVE);

    static String stripType(final String route){
        return type.matcher(route).replaceAll("");
    }

    static Route asRoute(final MTA mta, final String route_id){
        return asRoute(mta, route_id, null);
    }

    static Route asRoute(final MTA mta, final String route_id, final DataResourceType type){
        final String id = route_id.toUpperCase();
        // populate bus resources
        final List<DataResource> resources = new ArrayList<>();

        final List<DataResource> defaults = new ArrayList<DataResource>(){{
            /* 0 */ add(getDataResource(mta, DataResourceType.Bus_Brooklyn));
            /* 1 */ add(getDataResource(mta, DataResourceType.Bus_Bronx));
            /* 2 */ add(getDataResource(mta, DataResourceType.Bus_Manhattan));
            /* 3 */ add(getDataResource(mta, DataResourceType.Bus_Queens));
            /* 4 */ add(getDataResource(mta, DataResourceType.Bus_StatenIsland));
            /* 5 */ add(getDataResource(mta, DataResourceType.Bus_Company));
        }};

        // guess which resource route is from and prioritize that one first
        {
            int index = -1;
            if(id.startsWith("BX"))
                index = 1;
            else if(id.startsWith("B"))
                index = 0;
            else if(id.startsWith("M"))
                index = 2;
            else if(id.startsWith("Q"))
                index = 3;
            else if(id.startsWith("S"))
                index = 4;

            if(index > -1){
                final DataResource probableResource = defaults.get(index);
                defaults.remove(index);
                defaults.add(0, probableResource);
            }
        }

        // populate resources
        if(type != null){
            resources.add(getDataResource(mta, type));
            for(final DataResource r : defaults)
                if(r.getType() != type)
                    resources.add(r);
        }else
            resources.addAll(defaults);

        // find row
        DataResource d = null;
        CSV c = null;
        List<String> r = null;

        for(final DataResource dr : resources)
            if(
                (r =
                    (c =
                        (d = dr).getData("routes.txt"))
                    .getRow("route_id", id)
                ) != null
            )
                break;

        // instantiate
        Objects.requireNonNull(r, "Failed to find bus route with id '" + id + "'");

        final DataResource resource = d;
        final CSV csv = c;
        final List<String> row = r;

        return new Route() {

            private final String routeID = id;

            private final String routeShortName = row.get(csv.getHeaderIndex("route_short_name"));
            private final String routeLongName = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeDesc = row.get(csv.getHeaderIndex("route_desc"));

            private final String routeColor = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final Boolean SBS = route_id.endsWith("+") ||
                                        routeShortName.endsWith("-SBS") ||
                                        routeLongName.contains("Select Bus Service");
            private final Boolean express = route_id.startsWith("X") ||
                                            route_id.endsWith("X") ||
                                            route_id.endsWith("C") ||
                                            routeShortName.startsWith("X") ||
                                            routeShortName.endsWith("X") ||
                                            routeLongName.contains("Express");
            private final Boolean shuttle = routeLongName.contains("Shuttle");
            private final Boolean limited = routeDesc.contains("Limited");

            private final TransitAgency agency = asAgency(row.get(csv.getHeaderIndex("agency_id")), resource);

            private List<Vehicle> vehicles = null;
            private List<Bus.Alert> alerts = null;

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
            public final Boolean isSelectBusService(){
                return SBS;
            }

            @Override
            public final Boolean isExpress(){
                return express;
            }

            @Override
            public final Boolean isShuttle(){
                return shuttle;
            }

            @Override
            public final Boolean isLimited(){
                return limited;
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
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.bus.getTripUpdates(),
                        ent -> isExactRoute(ent.getTripUpdate().getTrip().getRouteId()), // match route
                        ent -> {
                            final String busId = ent.getTripUpdate().getVehicle().getId();

                            // find matching position entity
                            final FeedEntity pos = cast(mta).getFeedEntity(
                                cast(mta).service.bus.getVehiclePositions(),
                                pent -> Objects.equals(pent.getId(), busId)
                            );

                            return MTASchema_Bus.asVehicle(mta, pos.getVehicle(), ent.getTripUpdate(), this);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            @Override
            public final Bus.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Bus.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getBus(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameRoute(alert.getInformedEntity(i).getRouteId())) // check if alert includes this route
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Bus.Alert[0]
                    )));
                }
                return alerts.toArray(new Bus.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            @Override
            public final boolean isExactRoute(final Object object){
                if(object instanceof Route)
                    return getRouteID().equalsIgnoreCase(((Route) object).getRouteID());
                else if(object instanceof String)
                    return getRouteID().equalsIgnoreCase(object.toString());
                else
                    return false;
            }

            @Override
            public final boolean isSameRoute(final Object object){
                if(object instanceof Route)
                    return stripType(getRouteID()).equalsIgnoreCase(stripType(((Route) object).getRouteID()));
                else if(object instanceof String)
                    return stripType(getRouteID()).equalsIgnoreCase(stripType(object.toString()));
                else
                    return false;
            }

            //

            @Override
            public final String toString(){
                return "Bus.Route{" +
                       "routeID='" + routeID + '\'' +
                       ", routeShortName='" + routeShortName + '\'' +
                       ", routeLongName='" + routeLongName + '\'' +
                       ", routeDesc='" + routeDesc + '\'' +
                       ", routeColor='" + routeColor + '\'' +
                       ", routeTextColor='" + routeTextColor + '\'' +
                       ", SBS=" + SBS +
                       ", express=" + express +
                       ", shuttle=" + shuttle +
                       ", limited=" + limited +
                       ", agency=" + agency +
                       '}';
            }

        };
    }

    static Stop asStop(final MTA mta, final int stop_id){
        return asStop(mta, stop_id, null);
    }

    @SuppressWarnings("unused")
    static Stop asStop(final MTA mta, final int stop_id, final DataResourceType type){
        // populate bus resources
        final List<DataResource> resources = new ArrayList<>();

        final List<DataResource> defaults = Arrays.asList(
            getDataResource(mta, DataResourceType.Bus_Brooklyn),
            getDataResource(mta, DataResourceType.Bus_Bronx),
            getDataResource(mta, DataResourceType.Bus_Manhattan),
            getDataResource(mta, DataResourceType.Bus_Queens),
            getDataResource(mta, DataResourceType.Bus_StatenIsland),
            getDataResource(mta, DataResourceType.Bus_Company)
        );

        // not possible to guess resource type, all boroughs use the full range of codes

        // populate resources
        if(type != null){
            resources.add(getDataResource(mta, type));
            for(final DataResource r : defaults)
                if(r.getType() != type)
                    resources.add(r);
        }else
            resources.addAll(defaults);

        // find row
        DataResource d = null;
        CSV c = null;
        List<String> r = null;

        for(final DataResource dr : resources)
            if(
                (r =
                    (c =
                        (d = dr).getData("stops.txt"))
                    .getRow("stop_id", String.valueOf(stop_id))
                ) != null
            )
                break;

        // instantiate
        Objects.requireNonNull(r, "Failed to find bus stop with id '" + stop_id + "'");

        final DataResource resource = d;
        final CSV csv = c;
        final List<String> row = r;

        return new Stop() {

            private final Integer stopID = stop_id;
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final Double stopLat = Double.valueOf(row.get(csv.getHeaderIndex("stop_lat")));
            private final Double stopLon = Double.valueOf(row.get(csv.getHeaderIndex("stop_lon")));

            private List<Vehicle> vehicles = null;
            private List<Bus.Alert> alerts = null;

            @Override
            public final Integer getStopID(){
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
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            @SuppressWarnings("DataFlowIssue")
            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    this.vehicles = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.bus.getTripUpdates(),
                        ent -> {
                            final TripUpdate trip = ent.getTripUpdate();
                            final int len = trip.getStopTimeUpdateCount();
                            for(int i = 0; i < len; i++)
                                if(isSameStop(trip.getStopTimeUpdate(i).getStopId())) // check if route includes this stop
                                    return true;
                            return false;
                        },
                        ent -> {
                            final String busId = ent.getTripUpdate().getVehicle().getId();

                            // find matching position entity
                            final FeedEntity pos = cast(mta).getFeedEntity(
                                cast(mta).service.bus.getVehiclePositions(),
                                pent -> Objects.equals(pent.getId(), busId)
                            );

                            return MTASchema_Bus.asVehicle(mta, pos.getVehicle(), ent.getTripUpdate(), null);
                        },
                        new Vehicle[0]
                    )));
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            @Override
            public final Bus.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Bus.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Collections.unmodifiableList(Arrays.asList(cast(mta).transformFeedEntities(
                        cast(mta).service.alerts.getBus(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert = ent.getAlert();
                            final int len = alert.getInformedEntityCount();
                            for(int i = 0; i < len; i++)
                                if(isSameStop(alert.getInformedEntity(i).getStopId())) // check if alert includes this stop
                                    return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Bus.Alert[0]
                    )));
                }
                return alerts.toArray(new Bus.Alert[0]);
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
                    return getStopID().toString().equalsIgnoreCase(((String) object));
                else if(object instanceof Number)
                    return getStopID().equals(object);
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
                return "Bus.Stop{" +
                       "stopID=" + stopID +
                       ", stopName='" + stopName + '\'' +
                       ", stopLat=" + stopLat +
                       ", stopLon=" + stopLon +
                       '}';
            }

        };
    }

    static Vehicle asVehicle(final MTA mta, final VehiclePosition vehicle, final TripUpdate tripUpdate, final Route optionalRoute){
        return new Vehicle(){

            private final Integer vehicleID = requireNonNull(() -> Integer.valueOf(vehicle.getVehicle().getId().split("_")[1]));

            private Double latitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLatitude()));
            private Double longitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLongitude()));
            private Double bearing = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getBearing()));

            private BusDirection direction = requireNonNull(() -> BusDirection.asDirection(vehicle.getTrip().getDirectionId()));

            private Integer passengers = requireNonNull(() -> vehicle.getExtension(CrowdingProto.crowdingDescriptor).getEstimatedCount());

            private Integer stopID = requireNonNull(() -> Integer.valueOf(vehicle.getStopId()));
            private Stop stop = null;

            private String routeID = requireNonNull(() -> vehicle.getTrip().getRouteId());
            private Route route = optionalRoute;

            private Trip trip = asTrip(mta, tripUpdate, this);

            @Override
            public final Integer getVehicleID(){
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
            public final BusDirection getDirection(){
                return direction;
            }

            @Override
            public final Integer getPassengers(){
                return passengers;
            }

            @Override
            public final Boolean isSelectBusService(){
                return getRoute().isSelectBusService();
            }

            @Override
            public final Boolean isExpress(){
                return getRoute().isExpress();
            }

            @Override
            public final Boolean isShuttle(){
                return getRoute().isShuttle();
            }

            @Override
            public final Boolean isLimited(){
                return getRoute().isLimited();
            }

            @Override
            public final String getRouteID(){
                return routeID;
            }

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getBusRoute(routeID));
            }

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getBusStop(stopID));
            }

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            @SuppressWarnings("DataFlowIssue")
            private Trip getTrip(final boolean update){
                return !update ? trip : (trip = mta.getBus(vehicleID).getTrip());
            }

            @SuppressWarnings("DataFlowIssue")
            @Override
            public final void refresh(){
                getTrip(true);

                final Vehicle vehicle = mta.getBus(vehicleID);

                latitude = vehicle.getLatitude();
                longitude = vehicle.getLongitude();
                bearing = vehicle.getBearing();
                direction = vehicle.getDirection();
                passengers = vehicle.getPassengers();
                routeID = vehicle.getRouteID();
                route = null;
                stopID = vehicle.getStopID();
                stop = null;
            }

            //

            @Override
            public final String toString(){
                return "Bus.Vehicle{" +
                       "vehicleID=" + vehicleID +
                       ", latitude=" + latitude +
                       ", longitude=" + longitude +
                       ", bearing=" + bearing +
                       ", direction=" + direction +
                       ", passengers=" + passengers +
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
            private final String route = requireNonNull(() -> tripUpdate.getTrip().getRouteId());

            private final List<TripStop> stops;

            private final Integer delay = requireNonNull(tripUpdate::getDelay);

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
            public final Integer getDelay(){
                return delay;
            }

            @Override
            public final Vehicle getVehicle(){
                return referringVehicle;
            }

            //

            @Override
            public final String toString(){
                return "Bus.Trip{" +
                       "tripID='" + tripID + '\'' +
                       ", route='" + route + '\'' +
                       ", stops=" + stops +
                       ", delay=" + delay +
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

            private final Trip trip = referringTrip;

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @SuppressWarnings("DataFlowIssue")
            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getBusStop(stopID));
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
            public final Trip getTrip(){
                return trip;
            }

            //

            @Override
            public final String toString(){
                return "Bus.TripStop{" +
                       "stopID=" + stopID +
                       ", arrival=" + arrival +
                       ", departure=" + departure +
                       ", sequence=" + sequence +
                       '}';
            }

        };
    }

    static Bus.Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new Bus.Alert(){

            private final String alertID = requireNonNull(feedEntity::getId);

            private final List<TransitAlertPeriod> periods;

            private final List<String> routeIDs;
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

                final List<String> routes = new ArrayList<>();
                final List<Integer> stops = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        routes.add(entity.getRouteId());
                    else if(entity.hasStopId())
                        stops.add(Integer.valueOf(entity.getStopId()));
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
                        routes.add(mta.getBusRoute(id));
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
                        stops.add(mta.getBusStop(id));
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
                return "Bus.Alert{" +
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