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
import java.util.function.Supplier;

import static dev.katsute.onemta.Json.Expect.*;
import static dev.katsute.onemta.Json.Type.*;

final class Json {

    private final String json;
    private final int len;

    private Json(final String json){
        this.json = json;
        this.len  = json.length();
    }

    static Supplier<?> parse(final String json){
        return new Json(json).parse();
    }

    private Supplier<?> parse(){
        Objects.requireNonNull(json);

        if(len < 1)
            throw new JsonSyntaxException("Json string was empty");

        switch(json.charAt(0)){
            case '{':
                if(json.charAt(json.length() - 1) == '}')
                    return parseObject(json, 0 , len);
                else
                    throw new JsonSyntaxException("Missing closing bracket '}'");
            case '[':
                if(json.charAt(json.length() - 1) == ']')
                    return parseArray(json, 0 , len);
                else
                    throw new JsonSyntaxException("Missing closing bracket ']'");
            default:
                throw new JsonSyntaxException("Unexpected starting character: '" + json.charAt(0) + "' expected '{' or '['");
        }
    }

    enum Expect {

        START_OF_VALUE,
        LITERAL,
        NUMBER,
        END_OF_VALUE

    }

    enum Type {

        NULL,
        BOOLEAN,
        INTEGER,
        DOUBLE,
        STRING

    }

    private Supplier<List<?>> parseArray(final String json, final int start, final int end){
        return () -> {
            final List<Object> list = new ArrayList<>();

            Expect E = START_OF_VALUE;
            Type T   = null; // expected type

            boolean isEscaped = false;

            String V = null; // current value

            for(int i = start; i < end; i++){
                final char ch = json.charAt(i);
                switch(E){
                    case START_OF_VALUE: // expecting beginning of value
                        switch(ch){
                            case ' ': // whitespace
                            case '\t':
                            case '\r':
                            case '\n':
                                continue;
                            case '[': // array
                                list.add(parseArray(json, i, i = findEndToken(json, i, '[', ']')));
                                E = END_OF_VALUE;
                                continue;
                            case '{': // object
                                list.add(parseObject(json, i, i = findEndToken(json, i, '{', '}')));
                                E = END_OF_VALUE;
                                continue;
                            case '"': // string
                                T = STRING;
                                E = LITERAL;
                                V = "";
                                continue;
                            case 'f': // boolean
                            case 't':
                                T = BOOLEAN;
                                E = LITERAL;
                                V = String.valueOf(ch);
                                continue;
                            case 'n': // null
                                T = NULL;
                                E = LITERAL;
                                V = String.valueOf(ch);
                                continue;
                            default:
                                if(Character.isDigit(ch)){ // number
                                    T = INTEGER;
                                    E = NUMBER;
                                    V = String.valueOf(ch);
                                    continue;
                                }else // unknown
                                    throw new JsonSyntaxException("Unexpected literal '" + ch + "'");
                        }
                    case END_OF_VALUE: // expecting end of value
                        switch(ch){
                            case ' ': // whitespace
                            case '\t':
                            case '\r':
                            case '\n':
                                continue;
                            case ',': // next in array
                            case ']': // end of array
                                E = START_OF_VALUE; // prepare for next
                                switch(T){ // push value
                                    case NULL:
                                        list.add(null);
                                        break;
                                    case BOOLEAN:
                                        list.add(Boolean.parseBoolean(V));
                                        break;
                                    case INTEGER:
                                        list.add(Long.parseLong(V));
                                        break;
                                    case DOUBLE:
                                        list.add(Double.parseDouble(V));
                                        break;
                                    case STRING:
                                        list.add(V);
                                        break;
                                }
                                if(ch == ']') // end of array
                                    return list;
                        }
                        continue;
                    case LITERAL: // expecting literal value
                        switch(T){
                            case NULL: // parse null
                                V += ch;
                                if(!"null".startsWith(V)) // looks like null
                                    throw new JsonSyntaxException("Unexpected value \"" + V + "\", expected null");
                                else if("null".equals(V)) // is null
                                    E = END_OF_VALUE;
                                continue;
                            case BOOLEAN: // parse boolean
                                V += ch;
                                if(!"false".startsWith(V) && !"true".startsWith(V)) // looks like a boolean
                                    throw new JsonSyntaxException("Unexpected value \"" + V + "\", expected a boolean");
                                else if("false".equals(V) || "true".equals(V)) // is a boolean
                                    E = END_OF_VALUE;
                                continue;
                            case INTEGER:
                                switch(ch){ // parse integer
                                    case ' ': // whitespace
                                    case '\t':
                                    case '\r':
                                    case '\n':
                                        E = END_OF_VALUE;
                                        continue;
                                    case '.': // convert to decimal
                                        T = DOUBLE;
                                        E = NUMBER;
                                        V += ch;
                                        continue;
                                    case ',': // end of value
                                        E = END_OF_VALUE;
                                        i--;
                                        continue;
                                    default:
                                        if(Character.isDigit(ch)) // number
                                            V += ch;
                                        else // unknown
                                            throw new JsonSyntaxException("Unexpected token '" + ch + "', expected a number or ','");
                                }
                                continue;
                            case DOUBLE:
                                switch(ch){ // parse double
                                    case ' ': // whitespace
                                    case '\t':
                                    case '\r':
                                    case '\n':
                                        E = END_OF_VALUE;
                                        continue;
                                    case '.': // extra decimal
                                        throw new JsonSyntaxException("Unexpected token '.'");
                                    case ',': // end of value
                                        E = END_OF_VALUE;
                                        i--;
                                        continue;
                                    default:
                                        if(Character.isDigit(ch)) // number
                                            V += ch;
                                        else // unknown
                                            throw new JsonSyntaxException("Unexpected token '" + ch + "', expected a number");
                                }
                                continue;
                            case STRING:
                                switch(ch){
                                    case '\r': // illegal whitespace
                                    case '\n':
                                       throw new JsonSyntaxException("Unexpected token '" + ch + "'");
                                    case '\\': // escaped
                                        isEscaped = !isEscaped;
                                        continue;
                                    case '"': // quote
                                        if(!isEscaped) // end of value
                                            E = END_OF_VALUE;
                                        else // escaped quote
                                            V += ch;
                                        continue;
                                    default:
                                        if(!isEscaped) // literal
                                            V += ch;
                                        else // escaped
                                            switch(ch){
                                                case 't': // tab
                                                    V += '\t';
                                                    continue;
                                                case 'n': // new line
                                                    V += '\n';
                                                    continue;
                                                default: // unknown
                                                    throw new JsonSyntaxException("Unknown escape character '\\" + ch + "'");
                                            }
                                }
                        }
                    case NUMBER:
                        if(Character.isDigit(ch)){
                            E = LITERAL;
                            V += ch;
                        }else
                            throw new JsonSyntaxException("Unexpected token '" + ch + "', expected a number");
                }
            }
            throw new JsonSyntaxException("Missing closing bracket ']'");
        };
    }

    private Supplier<JsonObject> parseObject(final String json, final int start, final int end){
        return () -> {
            final JsonObject obj = new JsonObject();

            Expect E = START_OF_VALUE;
            Type T   = null;

            boolean isEscaped = false;
            boolean isKey = false;

            String K = null;
            String V = null;

            for(int i = start; i < end; i++){
                final char ch = json.charAt(i);
                switch(E){
                    case START_OF_VALUE: // expecting beginning of value
                        if(K == null){ // expecting key start of value

                            K = "";
                            isKey = true;
                        }else{ // expecting value start of value
                            switch(ch){
                                case ' ': // whitespace
                                case '\t':
                                case '\r':
                                case '\n':
                                    continue;
                                case '[': // array
                                    obj.set(K, parseArray(json, i, i = findEndToken(json, i, '[', ']')));
                                    E = END_OF_VALUE;
                                    continue;
                                case '{': // object
                                    obj.set(K, parseObject(json, i, i = findEndToken(json, i, '{', '}')));
                                    E = END_OF_VALUE;
                                    continue;
                                case '"': // string
                                    T = STRING;
                                    E = LITERAL;
                                    V = "";
                                    continue;
                                case 'f': // boolean
                                case 't':
                                    T = BOOLEAN;
                                    E = LITERAL;
                                    V = String.valueOf(ch);
                                    continue;
                                case 'n': // null
                                    T = NULL;
                                    E = LITERAL;
                                    V = String.valueOf(ch);
                                    continue;
                                default:
                                    if(Character.isDigit(ch)){ // number
                                        T = INTEGER;
                                        E = NUMBER;
                                        V = String.valueOf(ch);
                                        continue;
                                    }else // unknown
                                        throw new JsonSyntaxException("Unexpected literal '" + ch + "'");
                            }
                        }
                        continue;
                    case END_OF_VALUE: // expecting end of value
                        if(isKey){ // expecting :
                            switch(ch){
                                case ' ': // whitespace
                                case '\t':
                                case '\r':
                                case '\n':
                                    continue;
                                case ':': // colon
                                    E = START_OF_VALUE;
                                    isKey = false;
                            }
                        }else{
                            switch(ch){
                                case ' ': // whitespace
                                case '\t':
                                case '\r':
                                case '\n':
                                    continue;
                                case ',': // next in array
                                case '}': // end of object
                                    E = START_OF_VALUE; // prepare for next
                                    switch(T){ // push value
                                        case NULL:
                                            obj.set(K, null);
                                            break;
                                        case BOOLEAN:
                                            obj.set(K, Boolean.parseBoolean(V));
                                            break;
                                        case INTEGER:
                                            obj.set(K, Long.parseLong(V));
                                            break;
                                        case DOUBLE:
                                            obj.set(K, Double.parseDouble(V));
                                            break;
                                        case STRING:
                                            obj.set(K, V);
                                            break;
                                    }
                                    K = null;
                                    if(ch == '}') // end of array
                                        return obj;
                            }
                        }
                        continue;
                    case LITERAL: // expecting literal value
                        if(isKey){ // expecting string value

                        }else{ // expecting literal value

                        }
                    case NUMBER: // expecting number value
                        if(Character.isDigit(ch)){
                            E = LITERAL;
                            V += ch;
                        }else
                            throw new JsonSyntaxException("Unexpected token '" + ch + "', expected a number");
                }
            }

            throw new JsonSyntaxException("Missing closing bracket '}'");
        };
    }

    // start index should not include opening token
    private int findEndToken(final String json, final int start, final char openToken, final char closeToken){
        boolean isString = false;
        boolean isEscaped = false;

        int depth = 0;
        for(int i = start; i < len; i++){
            final char ch = json.charAt(i);
            if(ch == '\\')
                isEscaped = !isEscaped;
            else if(isEscaped)
                switch(ch){
                    case 'r':
                    case 'n':
                    case 't':
                    case '"':
                        isEscaped = false;
                }
            else if(ch == '"')
                isString = !isString;
            else if(!isString)
                if(ch == openToken)
                    depth++;
                else if(ch == closeToken)
                    if(depth == 0)
                        return i;
                    else
                        depth--;
        }
        throw new JsonSyntaxException("Missing closing bracket '" + closeToken + "'");
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
