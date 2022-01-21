<div id="top" align="center">
    <h3>OneMTA</h3>
    <h5>Java wrapper for the MTA Bus and Subway API</h5>
    <div>
        <a href="https://github.com/KatsuteDev/OneMTA/issues">Issues</a>
        •
        <a href="https://github.com/KatsuteDev/OneMTA/discussions">Discussions</a>
        •
        <a href="https://new.mta.info/developers">MTA Developer Resources</a>
        •
        <a href="https://groups.google.com/g/mtadeveloperresources">MTA Google Group</a>
    </div>
</div>

<br>

<div align="center">
    <a href="https://github.com/KatsuteDev/OneMTA/actions/workflows/java_ci.yml"><img alt="Java CI" src="https://github.com/KatsuteDev/OneMTA/actions/workflows/java_ci.yml/badge.svg"></a>
    <a href="https://github.com/KatsuteDev/OneMTA/actions/workflows/mta_ci.yml"><img alt="MTA CI" src="https://github.com/KatsuteDev/OneMTA/actions/workflows/mta_ci.yml/badge.svg"></a>
    <a href="https://mvnrepository.com/artifact/dev.katsute/onemta"><img alt="Maven Central" src="https://img.shields.io/maven-central/v/dev.katsute/onemta"></a>
    <a href="https://github.com/KatsuteDev/OneMTA/releases"><img alt="version" src="https://img.shields.io/github/v/release/KatsuteDev/OneMTA"></a>
    <a href="https://github.com/KatsuteDev/OneMTA/blob/main/LICENSE"><img alt="license" src="https://img.shields.io/github/license/KatsuteDev/OneMTA"></a>
</div>

# Overview

|⚠ The MTA API [Terms and Conditions](https://api.mta.info/#/DataFeedAgreement) prohibits developers from giving users direct access to MTA servers.|
|:-:|
|Any realtime data that is retrieved in this library must be served to users on your own servers.|

- [Overview](#overview)
- [Installation](#installation)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)
- [Disclaimer](#disclaimer)

# Installation

OneMTA requires at least Java 8. OneMTA also requires the [protobuf-java](https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java) library to be installed.

Compiled jars can be found on Maven Central and the releases tab.

Request a bus token at <http://bt.mta.info/wiki/Developers/Index>.

Request a subway token at <https://api.mta.info/#/signup>.

OneMTA requires static data from the MTA for most route and stop information. Latest static data for the MTA is available at <http://web.mta.info/developers/developer-data-terms.html#data>.

Only include static data for the API endpoints you are using. For the bus API you must include all boroughs plus bus company.

```java
DataResource staticBusBx  = DataResource.create(DataResourceType.Bus_Bronx, new File("bronx_google_transit.zip"));
DataResource staticBusBk  = DataResource.create(DataResourceType.Bus_Brooklyn, new File("brooklyn_google_transit.zip"));
DataResource staticBusMt  = DataResource.create(DataResourceType.Bus_Manhattan, new File("manhattan_google_transit.zip"));
DataResource staticBusQn  = DataResource.create(DataResourceType.Bus_Queens, new File("queens_google_transit.zip"));
DataResource staticBusSI  = DataResource.create(DataResourceType.Bus_StatenIsland, new File("staten_island_google_transit.zip"));
DataResource staticBusBC  = DataResource.create(DataResourceType.Bus_Company, new File("bus_company_google_transit.zip"));
DataResource staticSubway = DataResource.create(DataResourceType.Subway, new File("subway_google_transit.zip"));
DataResource staticLIRR   = DataResource.create(DataResourceType.LongIslandRailroad, new File("lirr_google_transit.zip"));
DataResource staticMNR    = DataResource.create(DataResourceType.MetroNorthRailroad, new File("mnr_google_transit.zip"));

MTA mta = MTA.create(busToken, subwayToken, staticBusBx, staticBusBk, staticBusMt, staticBusQn, staticBusSI, staticBusBC, staticSubway, staticLIRR, staticMNR);
```

<p align="right">(<a href="#top">back to top</a>)</p>

# Features

### 🗺 Routes

Retrieve routes along with alerts and vehicles on the route.

```java
Bus.Route M1    = mta.getBusRoute("M1", DataResourceType.Bus_Manhattan);
Subway.Route SI = mta.getSubwayRoute("SI");
LIRR.Route PW   = mta.getLIRRRoute(9);
MNR.Route HM    = mta.getMNRRoute(2);
```

### 🚏 Stops

Retrieve stops with alerts and vehicles en route.

```java
Bus.Stop stop   = mta.getBusStop(400561);
Subway.Stop GCT = mta.getSubwayStop("631");
LIRR.Stop FLS   = mta.getLIRRStop("FLS");
MNR.Stop WLN    = mta.getMNRStop("1WN");
```

### 🚍 Vehicles

Retrieve live vehicle information from stops and routes.

```java
Bus.Vehicle[]     busses = stop.getVehicles();
Subway.Vehicle[] subways = SI.getVehicles();
LIRR.Vehicle[] lirr      = PW.getVehicles();
MNR.Vehicle[] mnr        = WLN.getVehicles();
```

### ⚠ Alerts

Retrieve alerts for all stops and routes.

```java
Bus.Alert[] busAlerts       = mta.getBusAlerts();
Subway.Alert[] subwayAlerts = SI.getAlerts();
LIRR.Alert[] lirrAlerts     = FLS.getAlerts();
MNR.Alert[] mnrAlerts       = mta.getMNRAlerts();
```

<p align="right">(<a href="#top">back to top</a>)</p>

## Contributing

<!-- GitHub Copilot Disclaimer -->
<table>
    <img alt="GitHub Copilot" align="left" src="https://raw.githubusercontent.com/KatsuteDev/.github/main/profile/copilot-dark.png#gh-dark-mode-only" width="50"><img alt="GitHub Copilot" align="left" src="https://raw.githubusercontent.com/KatsuteDev/.github/main/profile/copilot-light.png#gh-light-mode-only" width="50">
    <p>GitHub Copilot is <b>strictly prohibited</b> on this repository.<br>Pulls using this will be rejected.</p>
</table>
<!-- GitHub Copilot Disclaimer -->

- Found a bug? Post it in [issues](https://github.com/KatsuteDev/OneMTA/issues).
- Have a suggestion or looking for inspiration? Check out our [discussions](https://github.com/KatsuteDev/OneMTA/discussions).
- Want to further expand our project or site? [Fork](https://github.com/KatsuteDev/OneMTA/fork) this repository and submit a [pull request](https://github.com/KatsuteDev/OneMTA/pulls).

Sample response data available on the [`reference`](https://github.com/KatsuteDev/OneMTA/tree/reference) branch.

### Updating Protobuf Files

- Download latest protobuf from [OneBusAway/onebusaway-gtfs-realtime-api](https://github.com/OneBusAway/onebusaway-gtfs-realtime-api/tree/master/src/main/proto/com/google/transit/realtime)
- Save proto files in `/protobuf`
- Change options and imports as defined in the `protobuf.sh` comments
- Run `protobuf.sh`
- Change all classes to abstract package private and add `SuppressWarnings("all")`

### Notice About Tests

- Tests are most reliable around rush hour.
- Tests may not work during overnight hours.
- Tests may not work if the selected routes are out of service.
- Tests may not work it the select stops are out of service.

### Running Tests Locally

For local tests you can use Java 8+, however only methods in the Java 8 API may be used. The `src/main/java9` folder should not be marked as a source root.

- (Bus) Run tests locally by adding a text file named `bus.txt` that contains the bus token in the `src/test/java/resources` directory.
- (Subway) Run tests locally by adding a text file named `subway.txt` that contains the subway in the `src/test/java/resources` directory.

### Running Tests Remotely

Devs running remote tests may do so by running the `MTA CI` workflow manually in the actions tab of your fork. Note that this requires two secrets, a `BUS_TOKEN` which contains the bus token, and a `SUBWAY_TOKEN` which contains the subway token.

<p align="right">(<a href="#top">back to top</a>)</p>

---

### License

This library is released under the [GNU General Public License (GPL) v2.0](https://github.com/KatsuteDev/OneMTA/blob/main/LICENSE).

### Disclaimer

- [@Katsute](https://github.com/Katsute) and [@KatsuteDev](https://github.com/KatsuteDev) are not affiliated with the MTA.
- By using the MTA API you are subject to their [Terms and Conditions](https://api.mta.info/#/DataFeedAgreement).

  > In developing your app, you will provide that the MTA data feed is available to others only from a non-MTA server. Accordingly, you will download and store the MTA data feed on a non-MTA server which users of your App will access in order to obtain data. MTA prohibits the development of an app that would make the data available to others directly from MTA's server(s).

<p align="right">(<a href="#top">back to top</a>)</p>