/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
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

import dev.katsute.onemta.APIStruct.*;
import dev.katsute.onemta.Json.JsonObject;

@SuppressWarnings("DefaultAnnotationParam")
interface MTABusService {

    String baseURL = "http://bustime.mta.info/api/siri/";

    static MTABusService create(){
        return APICall.create(baseURL, MTABusService.class);
    }

    @Endpoint(method="GET", value="vehicle-monitoring.json")
    Response<JsonObject> getVehicle(
        @Query("key")           final String token,
        @Query("VehicleRef")    final int vehicle,
        @Query("LineRef")       final String line,
        @Query("DirectionRef")  final int direction
    );

    @Endpoint(method="GET", value="stop-monitoring.json")
    Response<JsonObject> getStop(
        @Query("key")           final String token,
        @Query("MonitoringRef") final int stop,
        @Query("LineRef")       final String line,
        @Query("DirectionRef")  final int direction
    );

}
