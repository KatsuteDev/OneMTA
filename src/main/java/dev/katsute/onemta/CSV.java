/*
 * Copyright (C) 2023 Katsute <https://github.com/Katsute>
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

    @SuppressWarnings("SpellCheckingInspection")
    private final int lenm1;

    CSV(final String csv){
        final String[] lns = csv.trim().split("\n");
        headers = Collections.unmodifiableList(parseLine(lns[0]));
        final List<List<String>> rows = new ArrayList<>();
        for(int i = 1; i < lns.length; i++)
            rows.add(parseLine(lns[i]));
        rows.sort(comparator); // sort for binary search
        this.rows = Collections.unmodifiableList(rows);

        lenm1 = rows.size() - 1;
    }

    private static final Comparator<List<String>> comparator = Comparator.comparing(o -> o.get(0)); // compare key

    // (?:^|,)\s*(?:(?=")"([^"].*?)?"|(?!")(.*?))(?=,|$)
    private static final Pattern split = Pattern.compile("(?:^|,)\\s*(?:(?=\")\"([^\"].*?)?\"|(?!\")(.*?))(?=,|$)");

    private static List<String> parseLine(final String line){
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

    public final List<String> getRow(final String keyHeader, final String keyValue){
        final int keyIndex = getHeaderIndex(keyHeader);
        if(keyIndex == -1) return null;

        if(keyIndex == 0){
            final List<String> compare = new ArrayList<>(keyIndex);
            compare.add(keyValue);

            final int index = Collections.binarySearch(rows, compare, comparator); // binary search

            return index > -1 ? new ArrayList<>(rows.get(index)) : null;
        }else{
            for(List<String> row : rows)
                if(row.get(keyIndex).equals(keyValue))
                    return new ArrayList<>(row);
            return null;
        }
    }

    public final List<List<String>> getRows(final String keyHeader, final String keyValue){
        final int keyIndex = getHeaderIndex(keyHeader);
        if(keyIndex == -1) return null;

        if(keyIndex == 0){
            final List<String> compare = new ArrayList<>(keyIndex);
            compare.add(keyValue);

            final int index = Collections.binarySearch(rows, compare, comparator); // binary search
            int first = index;
            int last = index;

            if(index > -1){
                final List<List<String>> match = new ArrayList<>();
                match.add(rows.get(index));
                while(first > 0 && rows.get(first - 1).get(keyIndex).equals(keyValue)){ // check all values before
                    match.add(0, rows.get(first - 1)); // add to head of list
                    first--;
                }
                while(last < lenm1 && rows.get(last + 1).get(keyIndex).equals(keyValue)){ // check all values after
                    match.add(rows.get(last - 1)); // add to tail of list
                    last++;
                }
                return match;
            }
        }else{
            final List<List<String>> match = new ArrayList<>();
            for(final List<String> row : rows)
                if(row.get(keyIndex).equals(keyValue))
                    match.add(new ArrayList<>(row));
            return !match.isEmpty() ? match : null;
        }
        return null;
    }

    public final String getValue(final String keyHeader, final String keyValue, final String valueHeader){
        final int valIndex = getHeaderIndex(valueHeader);
        if(valIndex == -1) return null;
        final List<String> row = getRow(keyHeader, keyValue);
        return
            row != null && valIndex < row.size()
            ? row.get(valIndex)
            : null;
    }

    public final String[] getValues(final String keyHeader, final String keyValue, final String valueHeader){
        final int keyIndex = getHeaderIndex(keyHeader);
        final int valIndex = getHeaderIndex(valueHeader);
        if(keyIndex == -1 || valIndex == -1) return new String[0];
        final List<List<String>> matches = getRows(keyHeader, keyValue);
        final List<String> values = new ArrayList<>();
        if(matches != null)
            for(final List<String> match : matches)
                values.add(match.get(valIndex));
        return values.toArray(new String[0]);
    }

}