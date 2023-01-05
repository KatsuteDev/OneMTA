package dev.katsute.onemta;

import dev.katsute.onemta.bus.Bus;
import dev.katsute.onemta.railroad.LIRR;
import dev.katsute.onemta.railroad.MNR;
import dev.katsute.onemta.subway.Subway;

import java.io.File;

@SuppressWarnings("unused")
abstract class CompilerTests {

    @SuppressWarnings("SpellCheckingInspection")
    private void setup(){
        String busToken = "";
        String subwayToken = "";

        // setup

        MTA mta = mta = MTA.create(
            busToken,
            subwayToken,
            DataResource.create(DataResourceType.Bus_Bronx, new File("google_transit_bronx.zip")),
            DataResource.create(DataResourceType.Bus_Brooklyn, new File("google_transit_brooklyn.zip")),
            DataResource.create(DataResourceType.Bus_Manhattan, new File("google_transit_manhattan.zip")),
            DataResource.create(DataResourceType.Bus_Queens, new File("google_transit_queens.zip")),
            DataResource.create(DataResourceType.Bus_StatenIsland, new File("google_transit_staten_island.zip")),
            DataResource.create(DataResourceType.Bus_Company, new File("google_transit_bus_company.zip")),
            DataResource.create(DataResourceType.Subway, new File("google_transit_subway.zip")),
            DataResource.create(DataResourceType.LongIslandRailroad, new File("google_transit_lirr.zip")),
            DataResource.create(DataResourceType.MetroNorthRailroad, new File("google_transit_mnr.zip"))
        );

        // route

        Bus.Route M1 = mta.getBusRoute("M1", DataResourceType.Bus_Manhattan);
        Subway.Route SI = mta.getSubwayRoute("SI");
        LIRR.Route PW = mta.getLIRRRoute(9);
        MNR.Route HM = mta.getMNRRoute(2);

        // stop

        Bus.Stop stop = mta.getBusStop(400561);
        Subway.Stop GCT = mta.getSubwayStop("631");
        LIRR.Stop FLS = mta.getLIRRStop("FLS");
        MNR.Stop WLN = mta.getMNRStop("1WN");

        // vehicle

        Bus.Vehicle[] busses = stop.getVehicles();
        Subway.Vehicle[] subways = SI.getVehicles();
        LIRR.Vehicle[] lirr = PW.getVehicles();
        MNR.Vehicle[] mnr = WLN.getVehicles();

        // alerts

        Bus.Alert[] busAlerts = mta.getBusAlerts();
        Subway.Alert[] subwayAlerts = SI.getAlerts();
        LIRR.Alert[] lirrAlerts = FLS.getAlerts();
        MNR.Alert[] mnrAlerts = mta.getMNRAlerts();
    }

}
