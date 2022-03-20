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

package dev.katsute.onemta.types;

import dev.katsute.onemta.attribute.*;

/**
 * Represents a transit route.
 *
 * @param <RID> route ID format
 * @param <V> transit vehicle type
 * @param <A> transit alert type
 *
 * @since 1.0.0
 * @version 1.1.0
 * @author Katsute
 */
public abstract class TransitRoute<RID, V extends TransitVehicle<?,?,?,?,?,?>, A extends TransitAlert<?,?,?,?>> implements Alerts<A>, VehiclesReference<V>, Updatable {

    /**
     * Returns the transit agency that is operating the route.
     *
     * @return transit agency
     *
     * @see TransitAgency
     * @since 1.0.0
     */
    public abstract TransitAgency getAgency();

    /**
     * Returns the route ID.
     *
     * @return route ID
     *
     * @since 1.0.0
     */
    public abstract RID getRouteID();

    /**
     * Returns the route name.
     *
     * @return route name
     *
     * @since 1.0.0
     */
    public abstract String getRouteName();

    /**
     * Returns the route background color.
     *
     * @return route color
     *
     * @since 1.0.0
     */
    public abstract String getRouteColor();

    /**
     * Returns the route text color.
     *
     * @return route text color
     *
     * @since 1.0.0
     */
    public abstract String getRouteTextColor();

    /**
     * Returns if the route has the exact same route ID. Includes express and select bus service denotations.
     *
     * @param object route, route ID, or route code
     * @return if route ID matches
     *
     * @since 1.1.0
     * @see #isSameRoute(Object)
     */
    public abstract boolean isExactRoute(final Object object);

    /**
     * Returns if the route is the same route. Ignores express and select bus service denotations.
     *
     * @param object route, route ID, or route code
     * @return if route is referring to the same route
     *
     * @since 1.1.0
     * @see #isExactRoute(Object)
     */
    public abstract boolean isSameRoute(final Object object);

}
