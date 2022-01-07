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

@SuppressWarnings({"DefaultAnnotationParam", "SpellCheckingInspection"})
interface MTASubwayService {

    String baseURL = "https://api-endpoint.mta.info/Dataservice/mtagtfsfeeds/";

    static MTASubwayService create(){
        return APICall.create(baseURL, MTASubwayService.class);
    }

    @Endpoint(method="GET", value="nyct%2Fgtfs-ace")
    Response<JsonObject> getACE(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-bdfm")
    Response<JsonObject> getBDFM(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-g")
    Response<JsonObject> getG(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-jz")
    Response<JsonObject> getJZ(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-nqrw")
    Response<JsonObject> getNQRW(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-l")
    Response<JsonObject> getL(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs")
    Response<JsonObject> get1234567(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-si")
    Response<JsonObject> getSI(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-lirr")
    Response<JsonObject> getLIRR(
        @Header("x-api-key") final String token
    );

    @Endpoint(method="GET", value="nyct%2Fgtfs-mnr")
    Response<JsonObject> getMNRR(
        @Header("x-api-key") final String token
    );

}
