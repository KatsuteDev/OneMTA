module OneMTA {

    requires jdk.httpserver;
    requires java.logging;

    exports dev.katsute.onemta;
    exports dev.katsute.onemta.attribute;
    exports dev.katsute.onemta.exception;
    exports dev.katsute.onemta.types;

}