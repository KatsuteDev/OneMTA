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
import dev.katsute.onemta.types.TransitAlertPeriod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

import static dev.katsute.onemta.GTFSRealtimeProto.*;

abstract class MTASchema {

    protected static DataResource getDataResource(final MTA mta, final DataResourceType type){
        return cast(mta).getDataResource(type);
    }

    protected static MTAImpl cast(final MTA mta){
        return (MTAImpl) mta;
    }

    protected static <T> T requireNonNull(final Supplier<T> supplier){
        try{
            return supplier.get();
        }catch(final NullPointerException ignored){
            return null;
        }
    }

    //

    static TransitAgency asAgency(final String agency_id, final DataResource resource){
        return new TransitAgency() {

            private final String agencyID   = agency_id.toUpperCase();
            private final String agencyName = resource.getData("agency.txt").getValue("agency_id", agencyID, "agency_name");

            // static data

            @Override
            public final String getAgencyID(){
                return agencyID;
            }

            @Override
            public final String getAgencyName(){
                return agencyName;
            }

            // Java

            @Override
            public final boolean equals(final Object o){
                return this == o ||
                   (o != null &&
                    getClass() == o.getClass() &&
                    Objects.equals(agencyID, ((TransitAgency) o).getAgencyID()) &&
                    Objects.equals(agencyName, ((TransitAgency) o).getAgencyName()));
            }

            @Override
            public final String toString(){
                return "TransitAgency{" +
                       "agencyID='" + agencyID + '\'' +
                       ", agencyName='" + agencyName + '\'' +
                       '}';
            }

        };
    }

    static TransitAlertPeriod asTransitAlertTimeframe(final MTA mta, final TimeRange timeRange){
        return new TransitAlertPeriod() {

            private final Long start = requireNonNull(timeRange::getStart);
            private final Long end = requireNonNull(timeRange::getEnd);

            @Override
            public final Long getStartEpochMillis(){
                return start;
            }

            @Override
            public final Date getStart(){
                return start != null ? new Date(start) : null;
            }

            @Override
            public final Long getEndEpochMillis(){
                return end;
            }

            @Override
            public final Date getEnd(){
                return end != null ? new Date(end) : null;
            }

        };
    }

}
