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

import dev.katsute.onemta.types.TransitAlert;

/**
 * Indicates that transit alerts can be retrieved.
 *
 * @param <A> transit alert type
 *
 * @since 1.0.0
 * @version 1.1.0
 * @author Katsute
 */
public interface Alerts<A extends TransitAlert<?,?,?,?>> {

    /**
     * Returns all active alerts. Only fetches alerts for the first method call, subsequent calls will return the same set of alerts. Use {@link #getAlerts(boolean)} to re-fetch latest alerts.
     *
     * @return alerts
     *
     * @see #getAlerts(boolean)
     * @since 1.0.0
     */
    A[] getAlerts();

    /**
     * Returns all active alerts.
     *
     * @param update if false, will use alerts from previous calls; if true, will re-fetch alerts
     * @return alerts
     *
     * @see #getAlerts()
     * @since 1.1.0
     */
    A[] getAlerts(final boolean update);

}
