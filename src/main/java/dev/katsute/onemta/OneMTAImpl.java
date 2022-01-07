package dev.katsute.onemta;

import dev.katsute.onemta.types.DataResource;

import java.util.Arrays;

import static dev.katsute.onemta.MTAService.*;

final class OneMTAImpl extends OneMTA {

    private final transient String busToken;
    private final transient String subwayToken;
    private final DataResource[] resources;

    private final MTABusService busService       = MTABusService.create();
    private final MTASubwayService subwayService = MTASubwayService.create();

    OneMTAImpl(final String busToken, final String subwayToken){
        this(busToken, subwayToken, new DataResource[0]);
    }

    OneMTAImpl(final String busToken, final String subwayToken, final DataResource... resources){
        this.busToken    = busToken;
        this.subwayToken = subwayToken;
        this.resources   = Arrays.copyOf(resources, resources.length);
    }

    //

    @Override
    public void print(){
        System.out.println(busService.getVehicle(busToken, null, "M1", null).raw());
    }

}
