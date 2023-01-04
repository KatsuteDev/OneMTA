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

import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.bus.BusDirection;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.util.*;
import java.util.regex.Pattern;

import static dev.katsute.onemta.GTFSRealtimeProto.*;
import static dev.katsute.onemta.bus.Bus.*;

@SuppressWarnings({"SpellCheckingInspection", "Convert2Diamond"})
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
        DataResource d  = null;
        CSV c           = null;
        List<String> r  = null;

        for(final DataResource dr : resources)
            if(
                (r =
                    (c =
                        (d = dr).getData("routes.txt"))
                    .getRow("route_id", id)
                ) != null
            )
                break;

        final boolean bc = d == resources.get(5); // Bus Company is always index 5

        // instantiate
        Objects.requireNonNull(r, "Failed to find bus route with id '" + id + "'");

        final DataResource resource = d;
        final CSV csv               = c;
        final List<String> row      = r;

        return new Route() {

            private final String routeID = id;

            private final String routeShortName = row.get(csv.getHeaderIndex("route_short_name"));
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeDesc      = row.get(csv.getHeaderIndex("route_desc"));

            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final Boolean SBS     = route_id.endsWith("+") ||
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

            // static data

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

            // onemta methods

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

            // live feed

            private List<Vehicle> vehicles = null;

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final FeedMessage feed = cast(mta).service.bus.getTripUpdates();
                    final int len          = feed.getEntityCount();

                    final List<Vehicle> vehicles = new ArrayList<>();
                    for(int i = 0; i < len; i++){
                        final FeedEntity entity = feed.getEntity(i);
                        // only include trips with vehicles
                        if(entity.hasVehicle() && entity.hasTripUpdate())
                            // only include vehicles on this route
                            if(entity.getTripUpdate().getTrip().getRouteId().equals(routeID))
                                vehicles.add(asVehicle(mta, entity.getVehicle(), entity.getTripUpdate(), this));
                    }

                    this.vehicles = Collections.unmodifiableList(vehicles);
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            private List<Bus.Alert> alerts = null;

            @Override
            public final Bus.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Bus.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    this.alerts = Arrays.asList(cast(mta).getAlerts(
                        cast(mta).service.alerts.getBus(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert;
                            final int len;
                            if(ent.hasAlert() && (len = (alert = ent.getAlert()).getInformedEntityCount()) > 0)
                                for(int i = 0; i < len; i++)
                                    if(alert.getInformedEntity(i).getRouteId().equals(route_id))
                                        return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Bus.Alert[0]
                    ));
                }
                return alerts.toArray(new Bus.Alert[0]);
            }

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            // Java

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
        DataResource d  = null;
        CSV c           = null;
        List<String> r  = null;

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
        final CSV csv               = c;
        final List<String> row      = r;

        return new Stop() {

            private final Integer stopID  = stop_id;
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final Double stopLat  = Double.valueOf(row.get(csv.getHeaderIndex("stop_lat")));
            private final Double stopLon  = Double.valueOf(row.get(csv.getHeaderIndex("stop_lon")));

            // static data

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

            // live feed

            private List<Vehicle> vehicles = null;

            @Override
            public final Vehicle[] getVehicles(){
                return getVehicles(false);
            }

            private Vehicle[] getVehicles(final boolean update){
                if(vehicles == null || update){
                    final String stop = String.valueOf(stopID);

                    final FeedMessage feed = cast(mta).service.mnr.getMNR();
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

            private List<Bus.Alert> alerts = null;

            @Override
            public final Bus.Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Bus.Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    final String id = String.valueOf(stop_id);
                    this.alerts = Arrays.asList(cast(mta).getAlerts(
                        cast(mta).service.alerts.getBus(),
                        ent -> {
                            final GTFSRealtimeProto.Alert alert;
                            final int len;
                            if(ent.hasAlert() && (len = (alert = ent.getAlert()).getInformedEntityCount()) > 0)
                                for(int i = 0; i < len; i++)
                                    if(alert.getInformedEntity(i).getStopId().equals(id))
                                        return true;
                            return false;
                        },
                        ent -> asTransitAlert(mta, ent),
                        new Bus.Alert[0]
                    ));
                }
                return alerts.toArray(new Bus.Alert[0]);
            }

            // onemta methods

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

            @Override
            public final void refresh(){
                getAlerts(true);
                getVehicles(true);
            }

            // Java

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
        final CrowdingProto.CrowdingDescriptor crowdingDescriptor = vehicle.getExtension(CrowdingProto.crowdingDescriptor);
        return new Vehicle() {

            private final Integer vehicleID = requireNonNull(() -> Integer.parseInt(vehicle.getVehicle().getId().split("_")[1]));

            private Double latitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLatitude()));
            private Double longitude = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getLongitude()));
            private Double bearing = requireNonNull(() -> Double.valueOf(vehicle.getPosition().getBearing()));

            private Integer stopID = requireNonNull(() -> Integer.valueOf(vehicle.getStopId()));
            private String routeID = requireNonNull(() -> tripUpdate.getTrip().getRouteId());

            private BusDirection direction = requireNonNull(() -> BusDirection.asDirection(tripUpdate.getTrip().getDirectionId()));

            private final Integer passengers = requireNonNull(crowdingDescriptor::getEstimatedCount);

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
            public final Integer getPassengerCount() {
                return passengers;
            }

            @Override
            public final String getRouteID(){
                return routeID;
            }

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            // onemta methods

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

            private Route route = optionalRoute;

            @Override
            public final Route getRoute(){
                return route != null ? route : (route = mta.getBusRoute(routeID));
            }

            private Stop stop = null;

            @Override
            public final Stop getStop(){
                return stopID != null
                   ? stop != null
                        ? stop
                        : (stop = mta.getBusStop(stopID))
                   : null;
            }

            private Trip trip = asTrip(mta, tripUpdate, this);

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            private Trip getTrip(final boolean update){
                return !update ? trip : (trip = mta.getBus(vehicleID).getTrip());
            }

            @Override
            public final void refresh(){
                getTrip(true);

                Vehicle vehicle = null;

                if(stop != null){
                    for(final Vehicle veh : mta.getBusStop(stop.getStopID()).getVehicles()){
                        if(Objects.equals(vehicleID, veh.getVehicleID())){
                            vehicle = veh;
                            break;
                        }
                    }
                }

                if(vehicle == null)
                    vehicle = mta.getBus(vehicleID);

                latitude   = vehicle.getLatitude();
                longitude  = vehicle.getLongitude();
                bearing    = vehicle.getBearing();
                direction  = vehicle.getDirection();
                routeID    = vehicle.getRouteID();
                stopID     = vehicle.getStopID();
            }

            // Java

            @Override
            public String toString(){
                return "Bus.Vehicle{" +
                       "vehicleID=" + vehicleID +
                       ", latitude=" + latitude +
                       ", longitude=" + longitude +
                       ", bearing=" + bearing +
                       ", direction=" + direction +
                       ", routeID='" + routeID + '\'' +
                       ", stopID=" + stopID +
                       '}';
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

            @Override
            public final Integer getDelay(){
                return null;
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

            // Java

            @Override
            public final String toString(){
                return "Bus.Trip{" +
                       "tripID='" + tripID + '\'' +
                       ", routeID='" + routeID + '\'' +
                       '}';
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final TripUpdate.StopTimeUpdate stopTimeUpdate, final Trip referringTrip){
        return new TripStop() {

            private final Trip trip = referringTrip;

            private final Integer stopID = requireNonNull(() -> Integer.parseInt(stopTimeUpdate.getStopId()));

            private final Long arrival   = requireNonNull(() -> stopTimeUpdate.getArrival().getTime() * 1000);
            private final Long departure = requireNonNull(() -> stopTimeUpdate.getDeparture().getTime() * 1000);

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
            public final Integer getStopSequence(){
                return null;
            }

            // onemta methods

            private Stop stop = null;

            @Override
            public final Stop getStop(){
                return stop != null ? stop : (stop = mta.getBusStop(Objects.requireNonNull(stopID, "Stop ID must not be null")));
            }

            @Override
            public final Trip getTrip(){
                return trip;
            }

            // Java

            @Override
            public final String toString(){
                return "TripStop{" +
                       "stopID='" + stopID + '\'' +
                       ", arrival=" + arrival +
                       ", departure=" + departure +
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
