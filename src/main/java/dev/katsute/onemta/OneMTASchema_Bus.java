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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static dev.katsute.onemta.bus.Bus.*;

@SuppressWarnings({"SpellCheckingInspection", "ConstantConditions"})
abstract class OneMTASchema_Bus extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final String route_id){
        return asRoute(mta, route_id, null);
    }

    static Route asRoute(final OneMTA mta, final String route_id, final DataResourceType type){
        final String id = route_id.toUpperCase();
        // populate bus resources
        final List<DataResource> resources = new ArrayList<>();

        final List<DataResource> defaults = Arrays.asList(
            /* 0 */ getDataResource(mta, DataResourceType.Bus_Brooklyn),
            /* 1 */ getDataResource(mta, DataResourceType.Bus_Bronx),
            /* 2 */ getDataResource(mta, DataResourceType.Bus_Manhattan),
            /* 3 */ getDataResource(mta, DataResourceType.Bus_Queens),
            /* 4 */ getDataResource(mta, DataResourceType.Bus_StatenIsland),
            /* 5 */ getDataResource(mta, DataResourceType.Bus_Company)
        );

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
                        (d = dr).getData("routes.csv"))
                    .getRow("route_id", id)
                ) != null
            )
                break;

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

            private final Boolean SBS     = routeShortName.endsWith("+") || routeLongName.contains("Select Bus Service");
            private final Boolean express = routeShortName.startsWith("X") || routeShortName.endsWith("X") || routeLongName.contains("Express");
            private final Boolean shuttle = routeLongName.contains("Shuttle");
            private final Boolean limited = routeLongName.contains("Limited");


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
                    Objects.equals(id, ((Route) o).getRouteID()));
            }

        };
    }

    static Stop asStop(final OneMTA mta, final int stop_id){
        return asStop(mta, stop_id, null);
    }

    static Stop asStop(final OneMTA mta, final int stop_id, final DataResourceType type){
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
                        (d = dr).getData("stops.csv"))
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
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));
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
            public final String getRouteDescription(){
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
                    Objects.equals(stopID, ((Stop) o).getStopID()));
            }

        };
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    static Vehicle asVehicle(final OneMTA mta, final JsonObject monitoredVehicleJourney, final Route optionalRoute, final Stop optionalStop){
        final JsonObject monitoredCall = monitoredVehicleJourney.getJsonObject("MonitoredCall");
        return new Vehicle() {

            private final Integer vehicleID = requireNonNull(() -> Integer.valueOf(monitoredVehicleJourney.getString("VehicleRef").substring(9)));

            private final Double latitude  = requireNonNull(() -> monitoredVehicleJourney.getJsonObject("VehicleLocation").getDouble("Latitude"));
            private final Double longitude = requireNonNull(() -> monitoredVehicleJourney.getJsonObject("VehicleLocation").getDouble("Longitude"));
            // bearing is inverted
            @SuppressWarnings("GrazieInspection")
            private final Double bearing   = requireNonNull(() -> 360 - monitoredVehicleJourney.getDouble("Bearing"));

            private final BusDirection direction = requireNonNull(() -> BusDirection.asDirection(monitoredVehicleJourney.getInt("DirectionRef")));

            private final String routeID  = requireNonNull(() -> monitoredVehicleJourney.getString("LineRef").substring(9));
            private final Integer stopID  = requireNonNull(() -> Integer.valueOf(monitoredCall.getString("StopPointRef").substring(4)));
            private final String stopName = requireNonNull(() -> monitoredCall.getString("StopPointName"));

            private final Integer originStopCode = requireNonNull(() -> Integer.valueOf(monitoredVehicleJourney.getString("OriginRef").substring(4)));

            private final String destinationName = requireNonNull(() -> monitoredVehicleJourney.getString("DestinationName"));

            private final String progressRate   = requireNonNull(() -> monitoredVehicleJourney.getString("ProgressRate"));
            private final String progressStatus = requireNonNull(() -> monitoredVehicleJourney.getString("ProgressStatus"));

            private final Long aimedArrivalTime      = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("AimedArrivalTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private final Long expectedArrivalTime   = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("ExpectedArrivalTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private final Long expectedDepartureTime = requireNonNull(() -> {
                try{
                    return sdf.parse(monitoredCall.getString("ExpectedDepartureTime")).getTime();
                }catch(final ParseException e){
                    throw new RuntimeException(e);
                }
            });

            private final Double stopDistanceFromOrigin = requireNonNull(() -> monitoredCall.getJsonObject("Extensions").getJsonObject("Distances").getDouble("CallDistanceAlongRoute"));
            private final String stopDistanceMessage    = requireNonNull(() -> monitoredCall.getJsonObject("Extensions").getJsonObject("Distances").getString("PresentableDistance"));
            private final Double distanceFromStop       = requireNonNull(() -> monitoredCall.getJsonObject("Extensions").getJsonObject("Distances").getDouble("DistanceFromCall"));
            private final Integer stopsFromStop         = requireNonNull(() -> monitoredCall.getJsonObject("Extensions").getJsonObject("Distances").getInt("StopsFromCall"));

            private final Integer estimatedCapacity = requireNonNull(() -> monitoredCall.getJsonObject("Extensions").getJsonObject("Capacities").getInt("EstimatedPassengerCount"));

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
            public final String getProgressStatus(){
                return progressStatus;
            }

            @Override
            public final Date getAimedArrivalTime(){
                return aimedArrivalTime != null ? new Date(aimedArrivalTime) : null;
            }

            @Override
            public final Date getExpectedArrivalTime(){
                return expectedArrivalTime != null ? new Date(expectedArrivalTime) : null;
            }

            @Override
            public final Date getExpectedDepartureTime(){
                return expectedDepartureTime != null ? new Date(expectedDepartureTime) : null;
            }

            @Override
            public final Double getStopDistanceFromOrigin(){
                return stopDistanceFromOrigin;
            }

            @Override
            public final String getStopDistanceMessage(){
                return stopDistanceMessage;
            }

            @Override
            public final Double getDistanceFromStop(){
                return distanceFromStop;
            }

            @Override
            public final Integer getStopsFromStop(){
                return stopsFromStop;
            }

            @Override
            public final Integer getEstimatedPassengerCount(){
                return estimatedCapacity;
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
                return null;
            }

        };
    }

    // onwardcall seems to be always blank at the moment

    static Trip asTrip(final OneMTA mta, final JsonObject onwardCalls, final Vehicle referringVehicle){
        return null;
    }

    static TripStop asTripStop(final OneMTA mta, final JsonObject onwardCall, final Trip referringTrip){
        return null;
    }

}
