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

import dev.katsute.onemta.types.DataResource;

import java.io.File;
import java.util.*;

public class OneMTABuilder {

    private String busToken, subwayToken;

    private final Map<File,DataResource> resources = new HashMap<>();

    OneMTABuilder(){ }

    public final OneMTABuilder withBusToken(final String token){
        this.busToken = token;
        return this;
    }

    public final OneMTABuilder withSubwayToken(final String token){
        this.subwayToken = token;
        return this;
    }

    public final OneMTABuilder addResource(final File resource, final DataResource resourceType){
        resources.put(resource, resourceType);
        return this;
    }

    public final OneMTA build(){
        return new OneMTA() {

        };
    }

}
