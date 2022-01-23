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

import java.util.Date;

/**
 * Represents a {@link TransitAlert} period.
 *
 * @see TransitAlert
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
public abstract class TransitAlertPeriod {

    /**
     * Returns when the alert starts.
     *
     * @return alert start
     *
     * @see #getStartEpochMillis()
     * @since 1.0.0
     */
    public abstract Date getStart();

    /**
     * Returns when the alert starts as milliseconds since epoch.
     *
     * @return alert start
     *
     * @see #getStart()
     * @since 1.0.0
     */
    public abstract Long getStartEpochMillis();

    /**
     * Returns when the alert ends.
     *
     * @return alert end
     *
     * @see #getEndEpochMillis()
     * @since 1.0.0
     */
    public abstract Date getEnd();

    /**
     * Returns when the alert ends as milliseconds since epoch.
     *
     * @return alert end
     *
     * @see #getEndEpochMillis()
     * @since 1.0.0
     */
    public abstract Long getEndEpochMillis();

}
