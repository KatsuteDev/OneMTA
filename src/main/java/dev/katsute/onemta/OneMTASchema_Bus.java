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
import dev.katsute.onemta.types.TransitAgency;

import java.util.List;
import java.util.Objects;

import static dev.katsute.onemta.bus.Bus.*;

abstract class OneMTASchema_Bus extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final String route_id){
        // populate bus resources
        final DataResource[] resources = new DataResource[]{
            getDataResource(mta, DataResourceType.Bus_Brooklyn),
            getDataResource(mta, DataResourceType.Bus_Bronx),
            getDataResource(mta, DataResourceType.Bus_Manhattan),
            getDataResource(mta, DataResourceType.Bus_Queens),
            getDataResource(mta, DataResourceType.Bus_StatenIsland)
        };

        // find row
        DataResource d  = null;
        CSV c           = null;
        List<String> r  = null;

        final String id = route_id.toUpperCase();
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

            private final String routeID        = id;
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
                    Objects.equals(id, ((Route) o).getRouteID()));
            }

        };
    }

    static Stop asStop(final OneMTA mta, final int stop_id){
        // populate bus resources
        final DataResource[] resources = new DataResource[]{
            getDataResource(mta, DataResourceType.Bus_Brooklyn),
            getDataResource(mta, DataResourceType.Bus_Bronx),
            getDataResource(mta, DataResourceType.Bus_Manhattan),
            getDataResource(mta, DataResourceType.Bus_Queens),
            getDataResource(mta, DataResourceType.Bus_StatenIsland)
        };

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

            private final int stopID      = stop_id;
            private final String stopName = row.get(csv.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(csv.getHeaderIndex("stop_desc"));
            private final double stopLat  = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lat")));
            private final double stopLon  = Double.parseDouble(row.get(csv.getHeaderIndex("stop_lon")));

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
            public final double getLatitude(){
                return stopLat;
            }

            @Override
            public final double getLongitude(){
                return stopLon;
            }

            // live feed

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

    static Vehicle asVehicle(final OneMTA mta, final JsonObject json){
        return null;
    }

}
