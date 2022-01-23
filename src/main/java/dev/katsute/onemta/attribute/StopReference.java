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

package dev.katsute.onemta.attribute;

import dev.katsute.onemta.types.TransitStop;

/**
 * Indicates that there is a stop reference.
 *
 * @param <S> transit stop type
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public interface StopReference<S extends TransitStop<?,?,?>> {

    /**
     * For bus vehicles retrieved from a stop it will return the stop. <br>
     * For all other vehicles it will return the next stop on the route. <br>
     * For trip stops it will return the trip stop.
     *
     * @return stop
     *
     * @since 1.0.0
     */
    S getStop();

}
