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

        DataResource staticBusBx = DataResource.create(
            DataResourceType.Bus_Bronx,
            new File("bronx_google_transit.zip")
        );
        DataResource staticBusBk = DataResource.create(
            DataResourceType.Bus_Brooklyn,
            new File("brooklyn_google_transit.zip")
        );
        DataResource staticBusMt = DataResource.create(
            DataResourceType.Bus_Manhattan,
            new File("manhattan_google_transit.zip")
        );
        DataResource staticBusQn = DataResource.create(
            DataResourceType.Bus_Queens,
            new File("queens_google_transit.zip")
        );
        DataResource staticBusSI = DataResource.create(
            DataResourceType.Bus_StatenIsland,
            new File("staten_island_google_transit.zip")
        );
        DataResource staticBusBC = DataResource.create(
            DataResourceType.Bus_Company,
            new File("bus_company_google_transit.zip")
        );
        DataResource staticSubway = DataResource.create(
            DataResourceType.Subway,
            new File("subway_google_transit.zip")
        );
        DataResource staticLIRR = DataResource.create(
            DataResourceType.LongIslandRailroad,
            new File("lirr_google_transit.zip")
        );
        DataResource staticMNR = DataResource.create(
            DataResourceType.MetroNorthRailroad,
            new File("mnr_google_transit.zip")
        );

        MTA mta = MTA.create(busToken, subwayToken, staticBusBx, staticBusBk, staticBusMt, staticBusQn, staticBusSI, staticBusBC, staticSubway, staticLIRR, staticMNR);

        // route

        Bus.Route M1    = mta.getBusRoute("M1", DataResourceType.Bus_Manhattan);
        Subway.Route SI = mta.getSubwayRoute("SI");
        LIRR.Route PW   = mta.getLIRRRoute(9);
        MNR.Route HM    = mta.getMNRRoute(2);

        // stop

        Bus.Stop stop   = mta.getBusStop(400561);
        Subway.Stop GCT = mta.getSubwayStop("631");
        LIRR.Stop FLS   = mta.getLIRRStop("FLS");
        MNR.Stop WLN    = mta.getMNRStop("1WN");

        // vehicle

        Bus.Vehicle[] busses     = stop.getVehicles();
        Subway.Vehicle[] subways = SI.getVehicles();
        LIRR.Vehicle[] lirr      = PW.getVehicles();
        MNR.Vehicle[] mnr        = WLN.getVehicles();

        // alerts

        Bus.Alert[] busAlerts       = mta.getBusAlerts();
        Subway.Alert[] subwayAlerts = SI.getAlerts();
        LIRR.Alert[] lirrAlerts     = FLS.getAlerts();
        MNR.Alert[] mnrAlerts       = mta.getMNRAlerts();
    }

}
