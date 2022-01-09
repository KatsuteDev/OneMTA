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

import java.util.List;

import static dev.katsute.onemta.LIRR.*;

abstract class OneMTASchema_LIRR extends OneMTASchema {

    static Route asRoute(final OneMTA mta, final String route){

    }

    static Stop asStop(final OneMTA mta, final int stop, final Route route){
        final DataResource resource = getDataResource(mta, DataResourceType.LongIslandRailroad);
        final CSV stops = resource.getData("stops.csv");
        final List<String> row = stops.getRow("stop_id", String.valueOf(stop));
        return new Stop(){

            private final Integer stopID  = stop;
            private final String stopCode = row.get(stops.getHeaderIndex("stop_code"));
            private final String stopName = row.get(stops.getHeaderIndex("stop_name"));
            private final String stopDesc = row.get(stops.getHeaderIndex("stop_desc"));

            private final double stopLat = Double.parseDouble(row.get(stops.getHeaderIndex("stop_lat")));
            private final double stopLon = Double.parseDouble(row.get(stops.getHeaderIndex("stop_lon")));

            private final boolean wheelchairAccessible = row.get(stops.getHeaderIndex("wheelchair_boarding")).equals("1");

            @Override
            public final Integer getStopID(){
                return stop;
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
