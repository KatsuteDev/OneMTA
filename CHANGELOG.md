# Changelog

## 3.0.0

### Breaking Change

* Tokens are no longer required for GTFS feeds [#204](https://github.com/KatsuteDev/OneMTA/pull/204) ([@Katsute](https://github.com/Katsute))

  API tokens are no longer required for GTFS requests, this includes all alerts, subway, LIRR, and MNR.

  The `subwayToken` parameter has been removed from the `MTA.create` method.

### Dependencies

* Bump org.apache.maven.plugins:maven-javadoc-plugin from 3.6.0 to 3.6.2 [#180](https://github.com/KatsuteDev/OneMTA/pull/180) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-surefire-plugin from 3.2.1 to 3.2.2 [#179](https://github.com/KatsuteDev/OneMTA/pull/179) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#178](https://github.com/KatsuteDev/OneMTA/pull/178) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#182](https://github.com/KatsuteDev/OneMTA/pull/182) ([@dependabot](https://github.com/dependabot))
* Bump actions/setup-java from 3 to 4 [#184](https://github.com/KatsuteDev/OneMTA/pull/184) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-javadoc-plugin from 3.6.2 to 3.6.3 [#185](https://github.com/KatsuteDev/OneMTA/pull/185) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-surefire-plugin from 3.2.2 to 3.2.3 [#186](https://github.com/KatsuteDev/OneMTA/pull/186) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-compiler-plugin from 3.11.0 to 3.12.1 [#187](https://github.com/KatsuteDev/OneMTA/pull/187) ([@dependabot](https://github.com/dependabot))
* Bump onebusaway-gtfs-realtime-api from `fa611a5` to `f1105e7` [#190](https://github.com/KatsuteDev/OneMTA/pull/190) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#191](https://github.com/KatsuteDev/OneMTA/pull/191) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-surefire-plugin from 3.2.3 to 3.2.5 [#192](https://github.com/KatsuteDev/OneMTA/pull/192) ([@dependabot](https://github.com/dependabot))
* Bump the junit group with 2 updates [#194](https://github.com/KatsuteDev/OneMTA/pull/194) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#198](https://github.com/KatsuteDev/OneMTA/pull/198) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-gpg-plugin from 3.1.0 to 3.2.0 [#200](https://github.com/KatsuteDev/OneMTA/pull/200) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-gpg-plugin from 3.2.0 to 3.2.1 [#202](https://github.com/KatsuteDev/OneMTA/pull/202) ([@dependabot](https://github.com/dependabot))
* Bump protobuf from 3.25.3 to 4.26.0 [#203](https://github.com/KatsuteDev/OneMTA/pull/203) ([@kdevbot](https://github.com/kdevbot))

**Full Changelog**: [`2.1.0...3.0.0`](https://github.com/KatsuteDev/OneMTA/compare/2.1.0...3.0.0)

## 2.1.0

### Removed

* LIRR / MNR - Remove stop description [#174](https://github.com/KatsuteDev/OneMTA/pull/174) ([@Katsute](https://github.com/Katsute))

### Dependencies

* Bump org.apache.maven.plugins:maven-surefire-plugin from 3.1.2 to 3.2.1 [#169](https://github.com/KatsuteDev/OneMTA/pull/169) ([@dependabot](https://github.com/dependabot))
* Bump the junit group with 2 updates [#171](https://github.com/KatsuteDev/OneMTA/pull/171) ([@dependabot](https://github.com/dependabot))
* Upgrade to protobuf 25 [#172](https://github.com/KatsuteDev/OneMTA/pull/172) ([@Katsute](https://github.com/Katsute))

**Full Changelog**: [`2.0.5...2.1.0`](https://github.com/KatsuteDev/OneMTA/compare/2.0.5...2.1.0)

## 2.0.5

### Dependencies

* Bump the protobuf group with 2 updates [#157](https://github.com/KatsuteDev/OneMTA/pull/157) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#158](https://github.com/KatsuteDev/OneMTA/pull/158) ([@dependabot](https://github.com/dependabot))
* Bump actions/checkout from 3 to 4 [#160](https://github.com/KatsuteDev/OneMTA/pull/160) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#161](https://github.com/KatsuteDev/OneMTA/pull/161) ([@dependabot](https://github.com/dependabot))
* Bump org.apache.maven.plugins:maven-javadoc-plugin from 3.5.0 to 3.6.0 [#162](https://github.com/KatsuteDev/OneMTA/pull/162) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#165](https://github.com/KatsuteDev/OneMTA/pull/165) ([@dependabot](https://github.com/dependabot))
* Upgrade to Java 21 [#163](https://github.com/KatsuteDev/OneMTA/pull/163) ([@Katsute](https://github.com/Katsute))
* Update service status proto [#166](https://github.com/KatsuteDev/OneMTA/pull/166) ([@Katsute](https://github.com/Katsute))

**Full Changelog**: [`2.0.4...2.0.5`](https://github.com/KatsuteDev/OneMTA/compare/2.0.4...2.0.5)

## 2.0.4

### Dependencies

* Bump maven-surefire-plugin from 3.1.0 to 3.1.2 [#148](https://github.com/KatsuteDev/OneMTA/pull/148) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.23.2 to 3.23.3 [#149](https://github.com/KatsuteDev/OneMTA/pull/149) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.23.2 to 3.23.3 [#150](https://github.com/KatsuteDev/OneMTA/pull/150) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.23.3 to 3.23.4 [#153](https://github.com/KatsuteDev/OneMTA/pull/153) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.23.3 to 3.23.4 [#152](https://github.com/KatsuteDev/OneMTA/pull/152) ([@dependabot](https://github.com/dependabot))
* Bump the junit group with 2 updates [#154](https://github.com/KatsuteDev/OneMTA/pull/154) ([@dependabot](https://github.com/dependabot))
* Bump the protobuf group with 2 updates [#155](https://github.com/KatsuteDev/OneMTA/pull/155) ([@dependabot](https://github.com/dependabot))
* Upgrade to protobuf 24 [#156](https://github.com/KatsuteDev/OneMTA/pull/156) ([@Katsute](https://github.com/Katsute))

**Full Changelog**: [`2.0.3...2.0.4`](https://github.com/KatsuteDev/OneMTA/compare/2.0.3...2.0.4)

## 2.0.3

### Dependencies

* Bump protobuf-java from 3.22.2 to 3.22.3 [#130](https://github.com/KatsuteDev/OneMTA/pull/130) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.22.2 to 3.22.3 [#131](https://github.com/KatsuteDev/OneMTA/pull/131) ([@dependabot](https://github.com/dependabot))
* Bump junit-jupiter-api from 5.9.2 to 5.9.3 [#135](https://github.com/KatsuteDev/OneMTA/pull/135) ([@dependabot](https://github.com/dependabot))
* Bump junit-jupiter-params from 5.9.2 to 5.9.3 [#134](https://github.com/KatsuteDev/OneMTA/pull/134) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.22.3 to 3.22.4 [#139](https://github.com/KatsuteDev/OneMTA/pull/139) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.22.3 to 3.22.4 [#137](https://github.com/KatsuteDev/OneMTA/pull/137) ([@dependabot](https://github.com/dependabot))
* Bump maven-surefire-plugin from 3.0.0 to 3.1.0 [#136](https://github.com/KatsuteDev/OneMTA/pull/136) ([@dependabot](https://github.com/dependabot))
* Bump maven-gpg-plugin from 3.0.1 to 3.1.0 [#138](https://github.com/KatsuteDev/OneMTA/pull/138) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.22.4 to 3.23.0 [#141](https://github.com/KatsuteDev/OneMTA/pull/141) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.22.4 to 3.23.0 [#142](https://github.com/KatsuteDev/OneMTA/pull/142) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.23.0 to 3.23.1 [#144](https://github.com/KatsuteDev/OneMTA/pull/144) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.23.0 to 3.23.1 [#143](https://github.com/KatsuteDev/OneMTA/pull/143) ([@dependabot](https://github.com/dependabot))
* Bump maven-source-plugin from 3.2.1 to 3.3.0 [#145](https://github.com/KatsuteDev/OneMTA/pull/145) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.23.1 to 3.23.2 [#147](https://github.com/KatsuteDev/OneMTA/pull/147) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.23.1 to 3.23.2 [#146](https://github.com/KatsuteDev/OneMTA/pull/146) ([@dependabot](https://github.com/dependabot))

**Full Changelog**: [`2.0.2...2.0.3`](https://github.com/KatsuteDev/OneMTA/compare/2.0.2...2.0.3)

## 2.0.2

### 🐞 Bug Fixes

* Update service status [`4fb9157`](https://github.com/KatsuteDev/OneMTA/commit/4fb9157feb763a77e02573800eaae0be4b47dcbf) ([@Katsute](https://github.com/Katsute))

**Full Changelog**: [`2.0.1...2.0.2`](https://github.com/KatsuteDev/OneMTA/compare/2.0.1...2.0.2)

## 2.0.1

### 📘 Dependencies

* Bump maven-surefire-plugin from 3.0.0-M7 to 3.0.0-M8 [#114](https://github.com/KatsuteDev/OneMTA/pull/114) ([@dependabot](https://github.com/dependabot))
* Bump maven-surefire-plugin from 3.0.0-M8 to 3.0.0-M9 [#115](https://github.com/KatsuteDev/OneMTA/pull/115) ([@dependabot](https://github.com/dependabot))
* Bump maven-javadoc-plugin from 3.4.1 to 3.5.0 [#116](https://github.com/KatsuteDev/OneMTA/pull/116) ([@dependabot](https://github.com/dependabot))
* Bump maven-compiler-plugin from 3.10.1 to 3.11.0 [#120](https://github.com/KatsuteDev/OneMTA/pull/120) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.12 to 3.22.1 [#123](https://github.com/KatsuteDev/OneMTA/pull/123) ([@mashiro-san](https://github.com/mashiro-san))
* Bump protobuf-java from 3.22.1 to 3.22.2 [#125](https://github.com/KatsuteDev/OneMTA/pull/125) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.22.1 to 3.22.2 [#126](https://github.com/KatsuteDev/OneMTA/pull/126) ([@dependabot](https://github.com/dependabot))
* Bump maven-surefire-plugin from 3.0.0-M9 to 3.0.0 [#127](https://github.com/KatsuteDev/OneMTA/pull/127) ([@dependabot](https://github.com/dependabot))

**Full Changelog**: [`2.0.0...2.0.1`](https://github.com/KatsuteDev/OneMTA/compare/2.0.0...2.0.1)

## 2.0.0

### ⚠️ Breaking Change

* Use GTFS bus feed [#111](https://github.com/KatsuteDev/OneMTA/pull/111) ([@Katsute](https://github.com/Katsute))
  <h3 align="center">🛑 This major update includes breaking changes 🛑</h3>

  This update drops the SIRI bus API in favor of the GTFS bus API. This update is most likely **not compatible** with previous versions of the bus API; all other modes should still be compatible with this update.

   * Removed `Json` and `JsonSyntaxException`
   * Removed `RailroadStop`
   * Removed `RailroadTripStop`
   * Replaced merged GTFS types into transit types
   * Optimizations to request headers
   * Optimizations to token usage
   * Optimizations to vehicle feeds
   * Optimizations to vehicle requests
   * Optimizations to alert requests

  #### Attributes

  Inlined several attributes and removed respective interfaces:

   * Removed `Bearing`
   * Condensed `RouteReference`, `StopReference`, `TripReference`, and `VehiclesReference` into a single `Reference` class
   * Removed `RouteDescription`
   * Removed `RouteShortName`

  #### Alerts

   * Added created time
   * Added updated time
   * Removed alert effect
   * Added alert type

  #### Bus

  Dropped SIRI API in favor of GTFS API.

  ##### Stop

   * Removed origin stop
   * Removed destination
   * Removed progress rate
   * Removed progress status
   * Removed aimed arrival time
   * Removed expected departure time

  ##### Vehicle

   * Added passenger count
   * Removed arrival proximity
   * Removed stop distance

  ##### Trip

   * Added stop sequence
   * Added delay

  #### Subway

  ##### Vehicle

   * Added assigned indicator
   * Added rerouted indicator
   * Added skip stop indicator
   * Added turn train indicator
   * Added stop sequence

  #### Long Island Railroad (LIRR)

  ##### Trip

   * Added trip stop sequence
   * Added trip stop schedule relationship

  #### Metro North Railroad (MNR)

  ##### Vehicle

   * Added status

### 📘 Dependencies

* Bump protobuf-java from 3.21.7 to 3.21.8 [#98](https://github.com/KatsuteDev/OneMTA/pull/98) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.7 to 3.21.8 [#99](https://github.com/KatsuteDev/OneMTA/pull/99) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.8 to 3.21.9 [#102](https://github.com/KatsuteDev/OneMTA/pull/102) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.8 to 3.21.9 [#103](https://github.com/KatsuteDev/OneMTA/pull/103) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.9 to 3.21.10 [#104](https://github.com/KatsuteDev/OneMTA/pull/104) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.9 to 3.21.10 [#105](https://github.com/KatsuteDev/OneMTA/pull/105) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.10 to 3.21.11 [#107](https://github.com/KatsuteDev/OneMTA/pull/107) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.10 to 3.21.11 [#106](https://github.com/KatsuteDev/OneMTA/pull/106) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.11 to 3.21.12 [#109](https://github.com/KatsuteDev/OneMTA/pull/109) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.11 to 3.21.12 [#108](https://github.com/KatsuteDev/OneMTA/pull/108) ([@dependabot](https://github.com/dependabot))
* Bump junit-jupiter-api from 5.9.1 to 5.9.2 [#113](https://github.com/KatsuteDev/OneMTA/pull/113) ([@dependabot](https://github.com/dependabot))
* Bump junit-jupiter-params from 5.9.1 to 5.9.2 [#112](https://github.com/KatsuteDev/OneMTA/pull/112) ([@dependabot](https://github.com/dependabot))

**Full Changelog**: [`1.2.3...2.0.0`](https://github.com/KatsuteDev/OneMTA/compare/1.2.3...2.0.0)

## 1.2.3

### 🐞 Bug Fixes

* Fix express routes using unknown feed [#97](https://github.com/KatsuteDev/OneMTA/pull/97) ([@Katsute](https://github.com/Katsute))

### 🔧 Optimizations

* Optimize json [#94](https://github.com/KatsuteDev/OneMTA/pull/94) ([@Katsute](https://github.com/Katsute))
  * Increase parsing speed
  * Parse partials rather than full json
* Optimize cache [#95](https://github.com/KatsuteDev/OneMTA/pull/95) ([@Katsute](https://github.com/Katsute))
  * Optimize cache GET
  * Optimize cache WRITE for multiple threads
  * Fix cache expiry not working properly

### 📘 Dependencies

* Bump protobuf-java from 3.21.6 to 3.21.7 [#93](https://github.com/KatsuteDev/OneMTA/pull/93) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.6 to 3.21.7 [#92](https://github.com/KatsuteDev/OneMTA/pull/92) ([@dependabot](https://github.com/dependabot))

**Full Changelog**: [`1.2.2...1.2.3`](https://github.com/KatsuteDev/OneMTA/compare/1.2.2...1.2.3)

## 1.2.2

### 🔧 Optimizations

* Optimize static data [#90](https://github.com/KatsuteDev/OneMTA/pull/90) ([@Katsute](https://github.com/Katsute))

### 📘 Dependencies

* Bump protobuf-java from 3.21.3 to 3.21.4 [#70](https://github.com/KatsuteDev/OneMTA/pull/70) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.3 to 3.21.4 [#72](https://github.com/KatsuteDev/OneMTA/pull/72) ([@dependabot](https://github.com/dependabot))
* Bump junit-jupiter-params and junit-jupiter-api from 5.8.2 to 5.9.0 [#75](https://github.com/KatsuteDev/OneMTA/pull/75) ([@mashiro-san](https://github.com/mashiro-san))
* Bump protobuf-java from 3.21.4 to 3.21.5 [#76](https://github.com/KatsuteDev/OneMTA/pull/76) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.4 to 3.21.5 [#77](https://github.com/KatsuteDev/OneMTA/pull/77) ([@dependabot](https://github.com/dependabot))
* Bump maven-javadoc-plugin from 3.4.0 to 3.4.1 [#78](https://github.com/KatsuteDev/OneMTA/pull/78) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java from 3.21.5 to 3.21.6 [#80](https://github.com/KatsuteDev/OneMTA/pull/80) ([@dependabot](https://github.com/dependabot))
* Bump protobuf-java-util from 3.21.5 to 3.21.6 [#81](https://github.com/KatsuteDev/OneMTA/pull/81) ([@dependabot](https://github.com/dependabot))
* Bump maven-jar-plugin from 3.2.2 to 3.3.0 [#83](https://github.com/KatsuteDev/OneMTA/pull/83) ([@dependabot](https://github.com/dependabot))

**Full Changelog**: [`1.2.1...1.2.2`](https://github.com/KatsuteDev/OneMTA/compare/1.2.1...1.2.2)

## 1.2.1

### 🐞 Bug Fixes

* Fix updated MTA bus resource https://github.com/KatsuteDev/OneMTA/pull/63 (@Katsute)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.2.0...1.2.1

## 1.2.0

### ⭐ New Features

* Add canceled trip https://github.com/KatsuteDev/OneMTA/pull/58 (@Katsute)

### 📘 Dependencies

* Bump jcore from 2.0.0 to 2.0.1 https://github.com/KatsuteDev/OneMTA/pull/50 (@dependabot)
* Bump protobuf-java-util from 3.20.1 to 3.21.0 https://github.com/KatsuteDev/OneMTA/pull/53 (@dependabot)
* Bump protobuf-java from 3.20.1 to 3.21.0 https://github.com/KatsuteDev/OneMTA/pull/52 (@dependabot)
* Bump protobuf-java-util from 3.21.0 to 3.21.1 https://github.com/KatsuteDev/OneMTA/pull/55 (@dependabot)
* Bump protobuf-java from 3.21.0 to 3.21.1 https://github.com/KatsuteDev/OneMTA/pull/54 (@dependabot)
* Bump maven-surefire-plugin from 3.0.0-M6 to 3.0.0-M7 https://github.com/KatsuteDev/OneMTA/pull/57 (@dependabot)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.1.1...1.2.0

## 1.1.1

### 📘 Dependencies

* Bump maven-surefire-plugin from 3.0.0-M5 to 3.0.0-M6 https://github.com/KatsuteDev/OneMTA/pull/42 (@dependabot)
* Bump protobuf-java from 3.19.4 to 3.20.0 https://github.com/KatsuteDev/OneMTA/pull/43 (@dependabot)
* Bump protobuf-java-util from 3.19.4 to 3.20.0 https://github.com/KatsuteDev/OneMTA/pull/44 (@dependabot)
* Bump maven-javadoc-plugin from 3.3.2 to 3.4.0 https://github.com/KatsuteDev/OneMTA/pull/45 (@dependabot)
* Bump protobuf-java-util from 3.20.0 to 3.20.1 https://github.com/KatsuteDev/OneMTA/pull/48 (@dependabot)
* Bump protobuf-java from 3.20.0 to 3.20.1 https://github.com/KatsuteDev/OneMTA/pull/47 (@dependabot)
* Bump nexus-staging-maven-plugin from 1.6.8 to 1.6.13 https://github.com/KatsuteDev/OneMTA/pull/49 (@dependabot)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.1.0...1.1.1

## 1.1.0

### ⭐ New Features

* Add methods to refresh realtime data https://github.com/KatsuteDev/OneMTA/pull/34 (@Katsute)
* Add comparator methods to stops and routes https://github.com/KatsuteDev/OneMTA/pull/36 (@Katsute)
* Retrieve transfer stations for subways https://github.com/KatsuteDev/OneMTA/pull/39 (@Katsute)

### 🐞 Bug Fixes

* Fix vehicles for Bus Company https://github.com/KatsuteDev/OneMTA/pull/38 (@Katsute)

### 📘 Dependencies

* Bump maven-compiler-plugin from 3.10.0 to 3.10.1 https://github.com/KatsuteDev/OneMTA/pull/33 (@dependabot)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.0.3...1.1.0

## 1.0.3

### ⭐ New Features

* Add method to check if subway train is running express https://github.com/KatsuteDev/OneMTA/pull/32 (@Katsute)

### 🐞 Bug Fixes

* Fix incorrect time for alert periods https://github.com/KatsuteDev/OneMTA/pull/31 (@Katsute)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.0.2...1.0.3

## 1.0.2

### 🐞 Bug Fixes

* Fix incorrect time for gtfs https://github.com/KatsuteDev/OneMTA/pull/26 (@Katsute)

### 📘 Dependencies

* Bump actions/setup-java from 2.5.0 to 3 https://github.com/KatsuteDev/OneMTA/pull/20 (@dependabot)
* Update service status proto https://github.com/KatsuteDev/OneMTA/pull/22 (@Katsute)
* Bump actions/checkout from 2.4.0 to 3 https://github.com/KatsuteDev/OneMTA/pull/27 (@dependabot)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.0.1...1.0.2

## 1.0.1

### ❌ Removed

* Remove bus stop description https://github.com/KatsuteDev/OneMTA/pull/18 (@Katsute)

### 📘 Dependencies

* Bump maven-compiler-plugin from 3.9.0 to 3.10.0 https://github.com/KatsuteDev/OneMTA/pull/16 (@dependabot)

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/compare/1.0.0...1.0.1

## 1.0.0

Initial Release

**Full Changelog**: https://github.com/KatsuteDev/OneMTA/commits/1.0.0