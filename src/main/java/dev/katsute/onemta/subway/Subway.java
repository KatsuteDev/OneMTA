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

package dev.katsute.onemta.subway;

import dev.katsute.onemta.attribute.*;
import dev.katsute.onemta.types.*;

public abstract class Subway {

    public abstract static class Route extends TransitRoute<String,Vehicle> implements RouteShortName, RouteDescription { }

    public abstract static class Stop extends TransitStop<String,Vehicle> implements Direction<SubwayDirection> {

        public abstract Boolean isSameStop(final Stop stop);

    }

    public abstract static class Vehicle extends GTFSVehicle<Route,Stop,Trip,String,String,String> { }

    public abstract static class Trip extends GTFSTransitTrip<Vehicle,Route,TripStop> implements Direction<SubwayDirection> { }

    public abstract static class TripStop extends GTFSTripStop<Stop,Trip,String,Integer> {

        public abstract Integer getActualTrack();

    }

}
