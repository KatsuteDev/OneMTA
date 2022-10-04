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

import dev.katsute.onemta.exception.JsonSyntaxException;

import java.util.*;

final class Json {

    private final String json;
    private final long len;

    private Json(final String json){
        this.json = json;
        this.len  = json.length();
    }

    static Object parse(final String json){
        return new Json(json).parse();
    }

    private int i;

    private synchronized Object parse(){
        Objects.requireNonNull(json);

        if(len < 1)
            throw new JsonSyntaxException("Json string was empty");

        i = 0;

        switch(json.charAt(0)){
            case '{':
                return parseObject(json);
            case '[':
                return parseArray(json);
            default:
                throw new JsonSyntaxException("Unexpected starting character: '" + json.charAt(0) + "' expected '{' or '['");
        }
    }

    @SuppressWarnings("StringConcatenationInLoop")
    private JsonObject parseObject(final String json){
        final JsonObject obj = new JsonObject();

        boolean isEscaped = false;

        String currentKey = null;
        boolean EOK = false; // end of key

        String currentValue = null;
        boolean EOV = false; // end of value

        for(; i < len; i++){
            switch(json.charAt(i)){
                case '\\':
                    isEscaped = !isEscaped;
                    break;
                case '"':

                    break;
                default:
                    if(EOV)
                        throw new JsonSyntaxException("Unexpected token " + json.charAt(i) + ", expected ',' or '}'");
                    else if(EOK)
                        throw new JsonSyntaxException("Unexpected token " + json.charAt(i) + ", expected ':'");
                    else if(currentKey != null)
                        currentKey += json.charAt(i);
                    else if(currentValue != null)
                        currentValue += json.charAt(i);
                    else if(currentKey == null)
                        throw new JsonSyntaxException("Unexpected token " + json.charAt(i) + ", expected ':'");
            }
        }

        return null;
    }

    private enum Type {

        ARRAY,
        OBJECT,
        STRING,
        NUMBER,
        BOOLEAN,
        LITERAL

    }

    private List<?> arrayParser(final String json){
        final List<Object> list = new ArrayList<>();

        Type type = null;

        for(; i < len; i++){
            switch(json.charAt(i)){
                case '[':

            }
        }

        return null;
    }

    //

    static class JsonObject{

        private final Map<String,Object> map = new HashMap<>();

        JsonObject(){}

        public final Object get(final String key){
            return map.get(key);
        }

        public final String getString(final String key){
            final Object value = map.get(key);
            return
                value == null
                ? null
                : value instanceof String
                    ? (String) value
                    : value.toString();
        }

        public final int getInt(final String key){
            final Object value = map.get(key);
            return value instanceof String ? Integer.parseInt((String) value) : ((Number) value).intValue();
        }

        public final double getDouble(final String key){
            final Object value = map.get(key);
            return value instanceof String ? Double.parseDouble((String) value) : ((Number) value).doubleValue();
        }

        public final float getFloat(final String key){
            final Object value = map.get(key);
            return value instanceof String ? Float.parseFloat((String) value) : ((Number) value).floatValue();
        }

        public final long getLong(final String key){
            final Object value = map.get(key);
            return value instanceof String ? Long.parseLong((String) value) : ((Number) value).longValue();
        }

        public final boolean getBoolean(final String key){
            final Object value = map.get(key);
            return value instanceof String ? Boolean.parseBoolean((String) value) : (boolean) value;
        }

        public final boolean containsKey(final String key){
            return map.containsKey(key);
        }

        public final int size(){
            return map.size();
        }

        private void set(final String key, final Object value){
            map.put(key, value);
        }

    }

}
