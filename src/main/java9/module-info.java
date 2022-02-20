/**
 * <h1>OneMTA Documentation</h1>
 *
 * <h2>Key Classes:</h2>
 * <ul>
 *     <li>{@link dev.katsute.onemta.DataResource}</li>
 *     <li>{@link dev.katsute.onemta.MTA}</li>
 * </ul>
 *
 * <h2>Frequently Used Objects:</h2>
 * <ul>
 *     <li>
 *         {@link dev.katsute.onemta.bus.Bus}
 *         <ul>
 *             <li>{@link dev.katsute.onemta.bus.Bus.Route}</li>
 *             <li>{@link dev.katsute.onemta.bus.Bus.Stop}</li>
 *             <li>{@link dev.katsute.onemta.bus.Bus.Vehicle}</li>
 *             <li>{@link dev.katsute.onemta.bus.Bus.Trip}</li>
 *             <li>{@link dev.katsute.onemta.bus.Bus.TripStop}</li>
 *             <li>{@link dev.katsute.onemta.bus.Bus.Alert}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link dev.katsute.onemta.subway.Subway}
 *         <ul>
 *             <li>{@link dev.katsute.onemta.subway.Subway.Route}</li>
 *             <li>{@link dev.katsute.onemta.subway.Subway.Stop}</li>
 *             <li>{@link dev.katsute.onemta.subway.Subway.Vehicle}</li>
 *             <li>{@link dev.katsute.onemta.subway.Subway.Trip}</li>
 *             <li>{@link dev.katsute.onemta.subway.Subway.TripStop}</li>
 *             <li>{@link dev.katsute.onemta.subway.Subway.Alert}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link dev.katsute.onemta.railroad.LIRR}
 *         <ul>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.Route}</li>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.Stop}</li>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.Vehicle}</li>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.Trip}</li>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.TripStop}</li>
 *             <li>{@link dev.katsute.onemta.railroad.LIRR.Alert}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link dev.katsute.onemta.railroad.MNR}
 *         <ul>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.Route}</li>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.Stop}</li>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.Vehicle}</li>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.Trip}</li>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.TripStop}</li>
 *             <li>{@link dev.katsute.onemta.railroad.MNR.Alert}</li>
 *         </ul>
 *     </li>
 * </ul>
 */
module OneMTA {

    requires com.google.protobuf;

    exports dev.katsute.onemta;
    exports dev.katsute.onemta.attribute;
    exports dev.katsute.onemta.bus;
    exports dev.katsute.onemta.exception;
    exports dev.katsute.onemta.railroad;
    exports dev.katsute.onemta.subway;
    exports dev.katsute.onemta.types;

}
