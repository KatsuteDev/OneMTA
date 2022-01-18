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

import dev.katsute.onemta.exception.DataResourceException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class DataResource {

    public static DataResource create(final DataResourceType type, final File file){
        return new DataResource(){

            private final DataResourceType datatype = type;
            private final Map<String,CSV> data = new HashMap<>();

            {
                try(final ZipFile zip = new ZipFile(file)){
                    final Enumeration<? extends ZipEntry> entries = zip.entries();

                    while(entries.hasMoreElements()){
                        final ZipEntry entry = entries.nextElement();
                        final String name = entry.getName();
                        switch(name){
                            case "agency.txt":
                            case "routes.txt":
                            case "stops.txt":
                                try(final BufferedReader IN = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))){
                                    data.put(entry.getName(), new CSV(IN.lines().collect(Collectors.joining("\n"))));
                                }catch(final IOException e){
                                    throw new DataResourceException("Failed to read zip entry: " + entry.getName(), e);
                                }
                        }
                    }
                }catch(final IOException e){
                    throw new DataResourceException("Failed to load zip file: " + file.getPath(), e);
                }
            }

            @Override
            final CSV getData(final String key){
                return Objects.requireNonNull(data.get(key), "Failed to find csv with name '" + key + "'");
            }

            @Override
            final DataResourceType getType(){
                return datatype;
            }

        };
    }

    //

    private DataResource(){ }

    abstract CSV getData(final String key);

    abstract DataResourceType getType();

}
