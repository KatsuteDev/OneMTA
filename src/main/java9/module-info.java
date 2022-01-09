module OneMTA {

    requires jdk.httpserver;
    requires java.logging;

    exports dev.katsute.onemta;
    exports dev.katsute.onemta.bus;
    exports dev.katsute.onemta.exception;
    exports dev.katsute.onemta.railroad;
    exports dev.katsute.onemta.subway;
    exports dev.katsute.onemta.types;

}