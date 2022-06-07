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

/**
 * Represents a transit alert.
 *
 * @param <RID> route ID format
 * @param <R> transit route type
 * @param <SID> stop ID format
 * @param <S> transit stop type
 *
 * @see TransitAlertPeriod
 * @since 1.0.0
 * @version 1.2.0
 * @author Katsute
 */
public abstract class TransitAlert<RID, R extends TransitRoute<?,?,?>, SID, S extends TransitStop<?,?,?>> {

    /**
     * Returns the alert ID.
     *
     * @return alert ID
     *
     * @since 1.0.0
     */
    public abstract String getAlertID();

    /**
     * Returns when the alert is active.
     *
     * @return alert period
     *
     * @see TransitAlertPeriod
     * @since 1.0.0
     */
    public abstract TransitAlertPeriod[] getActivePeriods();

    /**
     * Returns which route IDs are affected.
     *
     * @return affected route IDs
     *
     * @see #getRoutes()
     * @since 1.0.0
     */
    public abstract RID[] getRouteIDs();

    /**
     * Returns which routes are affected.
     *
     * @return affected routes
     *
     * @see #getRouteIDs()
     * @since 1.0.0
     */
    public abstract R[] getRoutes();

    /**
     * Returns which stop IDs are affected.
     *
     * @return affected stop IDs
     *
     * @see #getStops()
     * @since 1.0.0
     */
    public abstract SID[] getStopIDs();

    /**
     * Returns which stops are affected.
     *
     * @return affected stop IDs
     */
    public abstract S[] getStops();

    /**
     * Returns the alert header.
     *
     * @return alert header
     *
     * @since 1.0.0
     */
    public abstract String getHeader();

    /**
     * Returns the alert description.
     *
     * @return alert description
     *
     * @since 1.0.0
     */
    public abstract String getDescription();

    /**
     * Returns the alert type.
     *
     * @return alert type
     *
     * @since 1.0.0
     */
    public abstract String getAlertType();

    /**
     * Returns the effect of the current alert.
     *
     * @return alert effect
     *
     * @since 1.2.0
     */
    public abstract String getEffect();

}
