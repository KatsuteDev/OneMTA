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

import dev.katsute.onemta.types.SubwayDirection;
import dev.katsute.onemta.types.TransitAgency;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static dev.katsute.onemta.Subway.*;

abstract class OneMTASchema_Subway extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final String route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.Subway);
        final CSV csv               = resource.getData("routes.csv");
        final List<String> row      = csv.getRow("route_id", route_id);

        // instantiate
        Objects.requireNonNull(row, "Failed to find subway route with id '" + route_id + "'");

        return new Route() {

            private final String routeID        = route_id;
            private final String routeShortName = row.get(csv.getHeaderIndex("route_short_name"));
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeDesc      = row.get(csv.getHeaderIndex("route_desc"));
            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

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
            public final TransitAgency getAgency(){
                return agency;
            }

            // live feed

            // Java

            @Override
            public final boolean equals(final Object o){
                return this == o ||
                   (o != null &&
                    getClass() == o.getClass() &&
                    route_id.equals(((Route) o).getRouteID()));
            }

        };
    }

    static Stop asStop(final OneMTA mta, final String stop_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.Subway);
        final CSV csv               = resource.getData("stops.csv");
        final List<String> row      = csv.getRow("stop_id", stop_id);

        // instantiate
        Objects.requireNonNull(row, "Failed to find subway stop with id '" + stop_id + "'");

        return new Stop() {

            private final String stopID   = stop_id;
            private final String stopCode = row.get(csv.getHeaderIndex("stop_code"));
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));

            private final double stopLat = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lat")));
            private final double stopLon = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lon")));

            private final SubwayDirection stopDirection =
                stop_id.endsWith("N") || stop_id.endsWith("S")
                ? stop_id.endsWith("N")
                    ? SubwayDirection.NORTH
                    : SubwayDirection.SOUTH
                : null;

            // static data

            @Override
            public final String getStopID(){
                return stopID;
            }

            @Override
            public final String getStopName(){
                return stopName;
            }

            @Override
            public final double getStopLatitude(){
                return stopLat;
            }

            @Override
            public final double getStopLongitude(){
                return stopLon;
            }

            @Override
            public final SubwayDirection getDirection(){
                return stopDirection;
            }

            private final Pattern direction = Pattern.compile("N|S$");

            @Override
            public final boolean isSameStop(final Stop stop){
                return this == stop ||
                   (stop != null &&
                    direction.matcher(getStopID()).replaceAll("").equals(direction.matcher(stop.getStopID()).replaceAll("")));
            }

            // live feed

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

    static Vehicle asVehicle(final OneMTA mta, final int id, final Route route){

    }

}
