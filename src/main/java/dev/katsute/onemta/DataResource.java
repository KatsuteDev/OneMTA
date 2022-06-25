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

/**
 * Represents a static data resource. Latest data can be retrieved from <a href="http://web.mta.info/developers/developer-data-terms.html#data">http://web.mta.info/developers/developer-data-terms.html#data</a>.
 *
 * @see #create(DataResourceType, File)
 * @since 1.0.0
 * @version 1.2.1
 * @author Katsute
 */
public abstract class DataResource {

    /**
     * Creates a data resource from a zip file.
     * <br>
     * At the moment the zip file only needs to have:
     * <ul>
     *     <li><code>agency.txt</code></li>
     *     <li><code>stops.txt</code></li>
     *     <li><code>routes.txt</code></li>
     * </ul>
     * All other files are ignored.
     *
     * @param type resource type
     * @param file google_transit zip file
     * @return data resource
     *
     * @see DataResourceType
     * @since 1.0.0
     */
    public static DataResource create(final DataResourceType type, final File file){
        Objects.requireNonNull(type, "DataResourceType must not be null");
        Objects.requireNonNull(file, "File must not be null");
        if(!file.exists())
            throw new DataResourceException("Failed to find file " + file.getPath());
        else if(!file.getName().endsWith(".zip"))
            throw new DataResourceException("DataResource must be a zip (.zip) file");

        return new DataResource(){

            private final DataResourceType datatype = type;
            private final Map<String,CSV> data      = new HashMap<>();
            private final File source               = file;

            {
                try(final ZipFile zip = new ZipFile(file)){
                    final Enumeration<? extends ZipEntry> entries = zip.entries();

                    while(entries.hasMoreElements()){
                        final ZipEntry entry = entries.nextElement();
                        final String path = entry.getName();
                        final String[] chunk = path.split("/");
                        final String filename = chunk[chunk.length - 1];
                        switch(filename){
                            case "agency.txt":
                            case "routes.txt":
                            case "stops.txt":
                            case "transfers.txt":
                                try(
                                    final InputStream IS = zip.getInputStream(entry);
                                    final InputStreamReader ISR = new InputStreamReader(IS);
                                    final BufferedReader IN = new BufferedReader(ISR)
                                ){
                                    data.put(filename, new CSV(IN.lines().collect(Collectors.joining("\n"))));
                                }catch(final IOException e){
                                    throw new DataResourceException("Failed to read zip entry: " + path, e);
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

            // Java

            @Override
            public final String toString(){
                return "DataResource{" +
                       "type=" + datatype +
                       ", source=" + source +
                       ", data=" + data.keySet() +
                       '}';
            }

        };
    }

    //

    private DataResource(){ }

    abstract CSV getData(final String key);

    abstract DataResourceType getType();

}
