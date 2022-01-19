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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CSV {

    private final List<String> headers;
    private final List<List<String>> rows;

    CSV(final String csv){
        final String[] lns = csv.trim().split("\n");
        headers = Collections.unmodifiableList(parseLine(lns[0]));
        final List<List<String>> rows = new ArrayList<>();
        for(int i = 1; i < lns.length; i++)
            rows.add(parseLine(lns[i]));
        this.rows = Collections.unmodifiableList(rows);
    }

    // (?:^|,)\s*(?:(?=")"([^"].*?)?"|(?!")(.*?))(?=,|$)
    private static final Pattern split = Pattern.compile("(?:^|,)\\s*(?:(?=\")\"([^\"].*?)?\"|(?!\")(.*?))(?=,|$)");

    private List<String> parseLine(final String line){
        final Matcher matcher = split.matcher(line);
        final List<String> row = new ArrayList<>();

        while(matcher.find())
            row.add(
                matcher.group(2) != null
                ? matcher.group(2)
                : matcher.group(1) != null
                    ? matcher.group(1)
                    : ""
        );
        return row;
    }

    public final List<String> getHeaders(){
        return new ArrayList<>(headers);
    }

    public final int getHeaderIndex(final String header){
        return headers.indexOf(header);
    }

    public final List<List<String>> getRows(){
        return new ArrayList<>(rows);
    }

    public final List<String> getRow(final String keyHeader, final String key){
        final int index = getHeaderIndex(keyHeader);
        if(index == -1) return null;
        for(final List<String> row : rows)
            if(row.get(index).equals(key))
                return new ArrayList<>(row);
        return null;
    }

    public final String getValue(final String keyHeader, final String key, final String valueHeader){
         final int index = getHeaderIndex(valueHeader);
        if(index == -1) return null;
        final List<String> row = getRow(keyHeader, key);
        return
            row != null && index < row.size()
            ? row.get(index)
            : null;
    }

}
