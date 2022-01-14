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

package dev.katsute.onemta.bus;

import dev.katsute.onemta.attribute.*;
import dev.katsute.onemta.types.*;

import java.util.Date;

public abstract class Bus {

    public abstract static class Route extends TransitRoute<String,Vehicle> implements RouteShortName, RouteDescription {

        public abstract Boolean isSelectBusService();

        public abstract Boolean isExpress();

        public abstract Boolean isShuttle();

        public abstract Boolean isLimited();

    }

    public abstract static class Stop extends TransitStop<Integer,Vehicle> implements RouteDescription { }

    public abstract static class Vehicle extends TransitVehicle<Route,Trip,Stop,Integer,String,Integer> implements BearingReference, LocationReference {

        public abstract Boolean isSelectBusService();

        public abstract Boolean isExpress();

        public abstract Boolean isShuttle();

        public abstract Boolean isLimited();

        public abstract BusDirection getDirection();

        public abstract Integer getOriginStopCode();

        public abstract Stop getOriginStop();

        public abstract String getDestinationName();

        public abstract String getProgressRate();

        public abstract String getProgressStatus();

        public abstract Date getAimedArrivalTime();

        public abstract Date getExpectedArrivalTime();

        public abstract Date getExpectedDepartureTime();

        public abstract Double getStopDistanceFromOrigin();

        public abstract String getStopDistanceMessage();

        public abstract Double getDistanceFromStop();

        public abstract Integer getStopsFromStop();

        public abstract Integer getEstimatedPassengerCount();

        public abstract Integer getVisitNumber();

        public abstract String getStopName();

    }

    public abstract static class Trip extends TransitTrip<Vehicle,Route,TripStop> { }

    public abstract static class TripStop extends TransitStopUpdate<Stop,Trip,Integer> { }

}
