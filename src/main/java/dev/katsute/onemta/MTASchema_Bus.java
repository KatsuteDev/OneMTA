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
import dev.katsute.onemta.bus.BusDirection;
import dev.katsute.onemta.types.TransitAgency;
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static dev.katsute.onemta.bus.Bus.*;

@SuppressWarnings({"SpellCheckingInspection", "ConstantConditions"})
abstract class MTASchema_Bus extends MTASchema {

    private static final Pattern type = Pattern.compile("^[X]|[XC+]$", Pattern.CASE_INSENSITIVE);

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
                    final JsonObject json = cast(mta).service.bus.getVehicle(cast(mta).busToken, null, routeID, null, bc);

                    final JsonObject vehicleMonitoringDelivery = json
                        .getJsonObject("Siri")
                        .getJsonObject("ServiceDelivery")
                        .getJsonArray("VehicleMonitoringDelivery")[0];

                    final JsonObject[] vehicleActivity =
                        vehicleMonitoringDelivery.containsKey("VehicleActivity")
                        ? vehicleMonitoringDelivery.getJsonArray("VehicleActivity")
                        : new JsonObject[0];

                    final List<Vehicle> vehicles = new ArrayList<>();
                    for(final JsonObject obj : vehicleActivity)
                        vehicles.add(asVehicle(mta, obj.getJsonObject("MonitoredVehicleJourney"), this, null));
                    this.vehicles = Collections.unmodifiableList(vehicles);
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            private List<Alert> alerts = null;

            @Override
            public final Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    final List<Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getBus(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final Alert alert = MTASchema_Bus.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getRouteIDs()).contains(route_id))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new Alert[0]);
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
                    final JsonObject json = cast(mta).service.bus.getStop(cast(mta).busToken, stop_id, null, null);

                    final JsonObject stopMonitoringDelivery = json
                        .getJsonObject("Siri")
                        .getJsonObject("ServiceDelivery")
                        .getJsonArray("StopMonitoringDelivery")[0];

                    final JsonObject[] monitoredStopVisit =
                        stopMonitoringDelivery.containsKey("MonitoredStopVisit")
                        ? stopMonitoringDelivery.getJsonArray("MonitoredStopVisit")
                        : new JsonObject[9];

                    final List<Vehicle> vehicles = new ArrayList<>();
                    for(final JsonObject obj : monitoredStopVisit)
                        vehicles.add(asVehicle(mta, obj.getJsonObject("MonitoredVehicleJourney"), null, this));
                    this.vehicles = Collections.unmodifiableList(vehicles);
                }
                return vehicles.toArray(new Vehicle[0]);
            }

            private List<Alert> alerts = null;

            @Override
            public final Alert[] getAlerts(){
                return getAlerts(false);
            }

            private Alert[] getAlerts(final boolean update){
                if(alerts == null || update){
                    final List<Alert> alerts = new ArrayList<>();
                    final GTFSRealtimeProto.FeedMessage feed = cast(mta).service.alerts.getBus(cast(mta).subwayToken);
                    final int len = feed.getEntityCount();
                    for(int i = 0; i < len; i++){
                        final Alert alert = MTASchema_Bus.asTransitAlert(mta, feed.getEntity(i));
                        if(Arrays.asList(alert.getStopIDs()).contains(stop_id))
                            alerts.add(alert);
                    }
                    this.alerts = alerts;
                }
                return alerts.toArray(new Alert[0]);
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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    static Vehicle asVehicle(final MTA mta, final JsonObject monitoredVehicleJourney, final Route optionalRoute, final Stop optionalStop){
        final JsonObject monitoredCall = monitoredVehicleJourney.getJsonObject("MonitoredCall");
        return new Vehicle() {

            private final Integer vehicleID = requireNonNull(() -> Integer.valueOf(monitoredVehicleJourney.getString("VehicleRef").substring(9)));

            private Double latitude  = requireNonNull(() -> monitoredVehicleJourney.getJsonObject("VehicleLocation").getDouble("Latitude"));
            private Double longitude = requireNonNull(() -> monitoredVehicleJourney.getJsonObject("VehicleLocation").getDouble("Longitude"));
            // bearing is inverted
            private Double bearing   = requireNonNull(() -> 360 - monitoredVehicleJourney.getDouble("Bearing"));

            private BusDirection direction = requireNonNull(() -> BusDirection.asDirection(monitoredVehicleJourney.getInt("DirectionRef")));

            private String routeID  = requireNonNull(() -> monitoredVehicleJourney.getString("LineRef").substring(monitoredVehicleJourney.getString("LineRef").indexOf('_') + 1));
            private Integer stopID  = requireNonNull(() -> Integer.valueOf(monitoredCall.getString("StopPointRef").substring(4)));
            private String stopName = requireNonNull(() -> monitoredCall.getStringArray("StopPointName")[0]);

            private Integer originStopCode = requireNonNull(() -> Integer.valueOf(monitoredVehicleJourney.getString("OriginRef").substring(4)));

            private String destinationName = requireNonNull(() -> monitoredVehicleJourney.getStringArray("DestinationName")[0]);

            private String progressRate     = requireNonNull(() -> monitoredVehicleJourney.getString("ProgressRate"));
            private String[] progressStatus = requireNonNull(() -> monitoredVehicleJourney.getStringArray("ProgressStatus"));

            private Long aimedArrivalTime      = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("AimedArrivalTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private Long expectedArrivalTime   = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("ExpectedArrivalTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private Long expectedDepartureTime = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("ExpectedDepartureTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private String arrivalProximityText = requireNonNull(() -> monitoredCall.getString("ArrivalProximityText"));
            private Integer distanceFromStop      = requireNonNull(() -> monitoredCall.getInt("DistanceFromStop"));
            private Integer stopsAway = requireNonNull(() -> monitoredCall.getInt("NumberOfStopsAway"));

            private Trip trip = monitoredVehicleJourney.containsKey("OnwardCalls") ? asTrip(mta, monitoredVehicleJourney.getJsonObject("OnwardCalls"), this) : null;

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
            public final Integer getOriginStopCode(){
                return originStopCode;
            }

            private Stop originStop = null;

            @Override
            public final Stop getOriginStop(){
                return originStopCode != null
                    ? originStop != null
                        ? originStop
                        : (originStop = mta.getBusStop(originStopCode))
                    : null;
            }

            @Override
            public final String getDestinationName(){
                return destinationName;
            }

            @Override
            public final String getProgressRate(){
                return progressRate;
            }

            @Override
            public final String[] getProgressStatus(){
                return progressStatus;
            }

            @Override
            public final Date getAimedArrivalTime(){
                return aimedArrivalTime != null ? new Date(aimedArrivalTime) : null;
            }

            @Override
            public final Long getAimedArrivalTimeEpochMillis(){
                return aimedArrivalTime;
            }

            @Override
            public final Date getExpectedArrivalTime(){
                return expectedArrivalTime != null ? new Date(expectedArrivalTime) : null;
            }

            @Override
            public final Long getExpectedArrivalTimeEpochMillis(){
                return expectedArrivalTime;
            }

            @Override
            public final Date getExpectedDepartureTime(){
                return expectedDepartureTime != null ? new Date(expectedDepartureTime) : null;
            }

            @Override
            public final Long getExpectedDepartureTimeEpochMillis(){
                return expectedDepartureTime;
            }

            @Override
            public final String getArrivalProximityText(){
                return arrivalProximityText;
            }

            @Override
            public final Integer getDistanceFromStop(){
                return distanceFromStop;
            }

            @Override
            public final Integer getStopsAway(){
                return stopsAway;
            }

            @Override
            public final String getRouteID(){
                return routeID;
            }

            @Override
            public final Integer getStopID(){
                return stopID;
            }

            @Override
            public final String getStopName(){
                return stopName;
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

            private Stop stop = optionalStop;

            @Override
            public final Stop getStop(){
                return stopID != null
                   ? stop != null
                        ? stop
                        : (stop = mta.getBusStop(stopID))
                   : null;
            }

            @Override
            public final Trip getTrip(){
                return getTrip(false);
            }

            private Trip getTrip(final boolean update){
                if(trip != null && !update)
                    return trip;
                else{
                    final Vehicle bus = mta.getBus(vehicleID);
                    if(bus != null)
                        return (trip = bus.getTrip());
                }
                return null;
            }

            @Override
            public final void refresh(){
                getTrip(true);

                Vehicle vehicle = null;

                // if stop was provided, must use vehicle from stop
                // so arrival times are based on this stop only, rather than next stop
                if(optionalStop != null){
                    for(final Vehicle veh : mta.getBusStop(optionalStop.getStopID()).getVehicles()){
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
                stopName   = vehicle.getStopName();
                originStopCode  = vehicle.getOriginStopCode();
                destinationName = vehicle.getDestinationName();
                progressRate    = vehicle.getProgressRate();
                progressStatus  = vehicle.getProgressStatus();
                aimedArrivalTime      = vehicle.getAimedArrivalTimeEpochMillis();
                expectedArrivalTime   = vehicle.getExpectedArrivalTimeEpochMillis();
                expectedDepartureTime = vehicle.getExpectedDepartureTimeEpochMillis();
                arrivalProximityText  = vehicle.getArrivalProximityText();
                distanceFromStop      = vehicle.getDistanceFromStop();
                stopsAway             = vehicle.getStopsAway();
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
                       ", stopName='" + stopName + '\'' +
                       ", originStopCode=" + originStopCode +
                       ", destinationName='" + destinationName + '\'' +
                       ", progressRate='" + progressRate + '\'' +
                       ", progressStatus=" + Arrays.toString(progressStatus) +
                       ", aimedArrivalTime=" + aimedArrivalTime +
                       ", expectedArrivalTime=" + expectedArrivalTime +
                       ", expectedDepartureTime=" + expectedDepartureTime +
                       ", arrivalProximityText='" + arrivalProximityText + '\'' +
                       ", distanceFromStop=" + distanceFromStop +
                       ", stopsAway=" + stopsAway +
                       '}';
            }

        };
    }

    static Trip asTrip(final MTA mta, final JsonObject onwardCalls, final Vehicle referringVehicle){
        return new Trip() {

            private final Vehicle vehicle = referringVehicle;

            private final List<TripStop> tripStops;

            {
                final List<TripStop> stops = new ArrayList<>();

                if(onwardCalls != null && onwardCalls.containsKey("OnwardCall"))
                    for(final JsonObject obj : onwardCalls.getJsonArray("OnwardCall"))
                        stops.add(asTripStop(mta, obj, this));
                tripStops = Collections.unmodifiableList(stops);
            }

            // onemta methods

            @Override
            public final Vehicle getVehicle(){
                return vehicle;
            }

            @Override
            public final Route getRoute(){
                return vehicle.getRoute();
            }

            @Override
            public final TripStop[] getTripStops(){
                return tripStops.toArray(new TripStop[0]);
            }

            // Java

            @Override
            public final String toString(){
                return "Bus.Trip{" +
                       '}';
            }

        };
    }

    static TripStop asTripStop(final MTA mta, final JsonObject onwardCall, final Trip referringTrip){
        return new TripStop() {

            private final Trip trip = referringTrip;

            private final Integer stopID  = requireNonNull(() -> Integer.valueOf(onwardCall.getString("StopPointRef").substring(4)));
            private final String stopName = requireNonNull(() -> onwardCall.getStringArray("StopPointName")[0]);

            private final Long expectedArrivalTime = requireNonNull(() -> {
                try{
                    return sdf.parse(onwardCall.getString("ExpectedArrivalTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private final String arrivalProximityText = requireNonNull(() -> onwardCall.getString("ArrivalProximityText"));
            private final Integer distanceFromStop    = requireNonNull(() -> onwardCall.getInt("DistanceFromStop"));
            private final Integer stopsAway           = requireNonNull(() -> onwardCall.getInt("NumberOfStopsAway"));

            @Override
            public final Long getExpectedArrivalTimeEpochMillis(){
                return expectedArrivalTime;
            }

            @Override
            public final Date getExpectedArrivalTime(){
                return expectedArrivalTime != null ? new Date(expectedArrivalTime) : null;
            }

            @Override
            public final String getArrivalProximityText(){
                return arrivalProximityText;
            }

            @Override
            public final Integer getDistanceFromStop(){
                return distanceFromStop;
            }

            @Override
            public final Integer getStopsAway(){
                return stopsAway;
            }

            @Override
            public final String getStopName(){
                return stopName;
            }

            @Override
            public final Integer getStopID(){
                return stopID;
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
                return "Bus.TripStop{" +
                       "stopID=" + stopID +
                       ", stopName='" + stopName + '\'' +
                       ", expectedArrivalTime=" + expectedArrivalTime +
                       ", arrivalProximityText='" + arrivalProximityText + '\'' +
                       ", distanceFromStop=" + distanceFromStop +
                       ", stopsAway=" + stopsAway +
                       '}';
            }

        };
    }

    static Alert asTransitAlert(final MTA mta, final GTFSRealtimeProto.FeedEntity feedEntity){
        final GTFSRealtimeProto.Alert alert = feedEntity.getAlert();
        return new Alert() {

            private final String alertID = requireNonNull(feedEntity::getId);

            private final String headerText      = requireNonNull(() -> alert.getHeaderText().getTranslation(0).getText());
            private final String descriptionText = requireNonNull(() -> alert.getDescriptionText().getTranslation(0).getText());

            private final String alertType = requireNonNull(() -> alert.getExtension(ServiceStatusProto.mercuryAlert).getAlertType());

            private final String effect = requireNonNull(() -> alert.getEffect().name());

            private final List<TransitAlertPeriod> alertPeriods;
            private final List<String> routeIDs;
            private final List<Integer> stopIDs;

            {
                final List<TransitAlertPeriod> alertPeriods = new ArrayList<>();
                for(final GTFSRealtimeProto.TimeRange range : alert.getActivePeriodList())
                    alertPeriods.add(asTransitAlertTimeframe(range));
                this.alertPeriods = Collections.unmodifiableList(alertPeriods);

                final List<String> routeIDs = new ArrayList<>();
                final List<Integer> stopIDs = new ArrayList<>();
                final int len = alert.getInformedEntityCount();
                for(int i = 0; i < len; i++){
                    final GTFSRealtimeProto.EntitySelector entity = alert.getInformedEntity(i);
                    if(entity.hasRouteId())
                        routeIDs.add(entity.getRouteId());
                    else if(entity.hasStopId())
                        stopIDs.add(Integer.valueOf(entity.getStopId()));
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
            public final String[] getRouteIDs(){
                return routeIDs.toArray(new String[0]);
            }

            private List<Route> routes = null;

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

            private List<Stop> stops = null;

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
                return "Bus.Alert{" +
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
