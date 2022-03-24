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

import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;
import dev.katsute.onemta.subway.SubwayDirection;

/**
 * Represents the MTA API.
 * <br>
 * Authenticate using {@link #create(String, String, DataResource...)}
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Katsute
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class MTA {

    MTA(){ }

    /**
     * Creates an MTA API interface. Note that bus alerts require a subway token.
     *
     * @param busToken bus token
     * @param subwayToken subway token
     * @param resources <b>(required)</b> static data resource, see {@link DataResource}
     * @return MTA
     *
     * @see #create(String, String, int, DataResource...)
     * @since 1.0.0
     */
    public static MTA create(final String busToken, final String subwayToken, final DataResource... resources){
        return create(busToken, subwayToken, -1, resources);
    }

    /**
     * Creates an MTA API interface. Note that bus alerts require a subway token.
     *
     * @param busToken bus token
     * @param subwayToken subway token
     * @param cacheSeconds how long to cache responses for
     * @param resources <b>(required)</b> static data resource, see {@link DataResource}
     * @return MTA
     *
     * @see #create(String, String, DataResource...)
     * @since 1.0.0
     */
    public static MTA create(final String busToken, final String subwayToken, final int cacheSeconds, final DataResource... resources){
        return new MTAImpl(busToken, subwayToken, cacheSeconds, resources);
    }

// bus methods

    /**
     * Returns a bus route. Much slower than {@link #getBusRoute(String, DataResourceType)}, consider passing the {@link DataResourceType} the route is in for faster results.
     *
     * @param route_id bus route ID
     *
     * @return bus route
     *
     * @see Bus.Route
     * @see #getBusRoute(String, DataResourceType)
     * @since 1.0.0
     */
    public abstract Bus.Route getBusRoute(final String route_id);

    /**
     * Returns a bus route.
     *
     * @param route_id bus route ID
     * @param type data resource that the bus route is in, must have been added to {@link #create(String, String, DataResource...)}
     * @return bus route
     *
     * @see DataResourceType
     * @see #getBusRoute(String)
     * @since 1.0.0
     */
    public abstract Bus.Route getBusRoute(final String route_id, final DataResourceType type);

    /**
     * Returns a bus stop. Much slower than {@link #getBusStop(int, DataResourceType)}, consider passing the {@link DataResourceType} the stop is in for faster results.
     *
     * @param stop_id bus stop ID
     *
     * @return bus stop
     *
     * @see Bus.Stop
     * @see #getBusStop(int, DataResourceType)
     * @since 1.0.0
     */
    public abstract Bus.Stop getBusStop(final int stop_id);

    /**
     * Returns a bus stop.
     *
     * @param stop_id bus stop ID
     * @param type data resource that the bus stop is in, must have been added to {@link #create(String, String, DataResource...)}
     *
     * @return bus stop
     *
     * @see Bus.Stop
     * @see #getBusStop(int)
     * @since 1.0.0
     */
    public abstract Bus.Stop getBusStop(final int stop_id, final DataResourceType type);

    /**
     * Returns a bus. Does not work for Bus Company.
     *
     * @param bus_id bus number as it is seen on the front and side of the bus
     *
     * @return bus
     *
     * @see Bus.Vehicle
     * @since 1.0.0
     */
    public abstract Bus.Vehicle getBus(final int bus_id);

    /**
     * Returns bus alerts. Requires a subway token.
     *
     * @return bus alerts
     *
     * @see Bus.Alert
     * @since 1.0.0
     */
    public abstract Bus.Alert[] getBusAlerts();

// subway methods

    /**
     * Returns a subway route.
     *
     * @param route_id route id
     * @return subway route
     *
     * @see Subway.Route
     * @see #getSubwayRoute(String)
     * @since 1.0.0
     */
    public abstract Subway.Route getSubwayRoute(final int route_id);

    /**
     * Returns a subway route.
     *
     * @param route_id route id
     * @return subway route
     *
     * @see Subway.Route
     * @see #getSubwayRoute(int)
     * @since 1.0.0
     */
    public abstract Subway.Route getSubwayRoute(final String route_id);

    /**
     * Returns a subway stop.
     *
     * @param stop_id stop id
     * @return subway stop
     *
     * @see Subway.Stop
     * @see #getSubwayStop(String)
     * @see #getSubwayStop(int, SubwayDirection)
     * @see #getSubwayStop(String, SubwayDirection)
     * @since 1.0.0
     */
    public abstract Subway.Stop getSubwayStop(final int stop_id);

    /**
     * Returns a subway stop.
     *
     * @param stop_id stop id
     * @return subway stop
     *
     * @see SubwayDirection
     * @see Subway.Stop
     * @see #getSubwayStop(int)
     * @see #getSubwayStop(int, SubwayDirection)
     * @see #getSubwayStop(String, SubwayDirection)
     * @since 1.0.0
     */
    public abstract Subway.Stop getSubwayStop(final String stop_id);

    /**
     * Returns a subway stop.
     *
     * @param stop_id stop id
     * @param direction subway direction
     * @return subway stop
     *
     * @see SubwayDirection
     * @see Subway.Stop
     * @see #getSubwayStop(int)
     * @see #getSubwayStop(String)
     * @see #getSubwayStop(String, SubwayDirection)
     * @since 1.0.0
     */
    public abstract Subway.Stop getSubwayStop(final int stop_id, final SubwayDirection direction);

    /**
     * Returns a subway stop.
     *
     * @param stop_id stop id
     * @param direction subway direction
     * @return subway stop
     *
     * @see SubwayDirection
     * @see Subway.Stop
     * @see #getSubwayStop(int)
     * @see #getSubwayStop(String)
     * @see #getSubwayStop(int, SubwayDirection)
     * @since 1.0.0
     */
    public abstract Subway.Stop getSubwayStop(final String stop_id, final SubwayDirection direction);

    /**
     * Returns a subway train.
     *
     * @param train_id train id
     * @return subway train
     *
     * @see Subway.Vehicle
     * @since 1.0.0
     */
    public abstract Subway.Vehicle getSubwayTrain(final String train_id);

    /**
     * Returns subway alerts.
     *
     * @return subway alerts
     *
     * @see Subway.Alert
     * @since 1.0.0
     */
    public abstract Subway.Alert[] getSubwayAlerts();

// lirr methods

    /**
     * Returns a Long Island Railroad (LIRR) route.
     *
     * @param route_id route id
     * @return LIRR route
     *
     * @see LIRR.Route
     * @since 1.0.0
     */
    public abstract LIRR.Route getLIRRRoute(final int route_id);

    /**
     * Returns a Long Island Railroad (LIRR) stop.
     *
     * @param stop_id stop id
     * @return LIRR stop
     *
     * @see LIRR.Stop
     * @see #getLIRRStop(String)
     * @since 1.0.0
     */
    public abstract LIRR.Stop getLIRRStop(final int stop_id);

    /**
     * Returns a Long Island Railroad (LIRR) stop.
     *
     * @param stop_code stop code
     * @return LIRR stop
     *
     * @see LIRR.Stop
     * @see #getLIRRStop(int)
     * @since 1.0.0
     */
    public abstract LIRR.Stop getLIRRStop(final String stop_code);

    /**
     * Returns a Long Island Railroad (LIRR) train.
     *
     * @param train_id train id
     * @return LIRR train
     *
     * @see LIRR.Vehicle
     * @since 1.0.0
     */
    public abstract LIRR.Vehicle getLIRRTrain(final String train_id);

    /**
     * Returns Long Island Railroad (LIRR) alerts.
     *
     * @return LIRR alerts
     *
     * @see LIRR.Alert
     * @since 1.0.0
     */
    public abstract LIRR.Alert[] getLIRRAlerts();

// mnr methods

    /**
     * Returns a Metro North Railroad (MNR) route.
     *
     * @param route_id route id
     * @return MNR route
     *
     * @see MNR.Route
     * @since 1.0.0
     */
    public abstract MNR.Route getMNRRoute(final int route_id);

    /**
     * Returns a Metro North Railroad (MNR) stop.
     *
     * @param stop_id stop id
     * @return MNR stop
     *
     * @see MNR.Stop
     * @see #getMNRStop(String)
     * @since 1.0.0
     */
    public abstract MNR.Stop getMNRStop(final int stop_id);

    /**
     * Returns a Metro North Railroad (MNR) stop.
     *
     * @param stop_code stop code
     * @return MNR stop
     *
     * @see MNR.Stop
     * @see #getMNRStop(int)
     * @since 1.0.0
     */
    public abstract MNR.Stop getMNRStop(final String stop_code);

    /**
     * Returns a Metro North Railroad (MNR) train.
     *
     * @param train_id train id
     * @return MNR train
     *
     * @see MNR.Vehicle
     * @since 1.0.0
     */
    public abstract MNR.Vehicle getMNRTrain(final String train_id);

    /**
     * Returns Metro North Railroad (MNR) alerts.
     *
     * @return MNR alerts
     *
     * @see MNR.Alert
     * @since 1.0.0
     */
    public abstract MNR.Alert[] getMNRAlerts();

}
