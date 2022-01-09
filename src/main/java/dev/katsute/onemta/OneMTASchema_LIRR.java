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

import java.util.List;
import java.util.Objects;

import static dev.katsute.onemta.LIRR.*;

@SuppressWarnings("SpellCheckingInspection")
abstract class OneMTASchema_LIRR extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final String route_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV csv               = resource.getData("routes.csv");
        final List<String> row      = csv.getRow("route_id", route_id);

        // instantiate
        Objects.requireNonNull(row, "Failed to find LIRR route with id '" + route_id + "'");

        return new Route() {

            private final String routeID        = route_id;
            private final String routeLongName  = row.get(csv.getHeaderIndex("route_long_name"));
            private final String routeColor     = row.get(csv.getHeaderIndex("route_color"));
            private final String routeTextColor = row.get(csv.getHeaderIndex("route_text_color"));

            private final TransitAgency agency = asAgency("LI", resource);

            // static data

            @Override
            public final String getRouteID(){
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

    static Stop asStop(final OneMTA mta, final int stop_id){
        // find row
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV csv               = resource.getData("stops.csv");
        final List<String> row      = csv.getRow("stop_id", String.valueOf(stop_id));

        // instantiate
         Objects.requireNonNull(row, "Failed to find LIRR stop with id '" + stop_id + "'");

        return new Stop(){

            private final Integer stopID  = stop_id;
            private final String stopCode = row.get(csv.getHeaderIndex("stop_code"));
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));

            private final double stopLat = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lat")));
            private final double stopLon = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lon")));

            private final boolean wheelchairAccessible = !row.get(csv.getHeaderIndex("wheelchair_boarding")).equals("2");

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
            public final double getStopLatitude(){
                return stopLat;
            }

            @Override
            public final double getStopLongitude(){
                return stopLon;
            }

            @Override
            public final boolean hasWheelchairBoarding(){
                return wheelchairAccessible;
            }

            //

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
