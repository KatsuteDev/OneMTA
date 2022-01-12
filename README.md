<div id="top" align="center">
    <h3>OneMTA</h3>
    <h3>Java wrapper for the MTA Bus Time and MTA Subway Time API</h3>
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

> ⚠ The MTA API [Terms and Conditions](https://api.mta.info/#/DataFeedAgreement) prohibits developers from giving users direct access to MTA servers. Any realtime data that is retrieved in this library must be served to users on your own servers.

- [Overview](#overview)
- [Installation](#installation)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)
- [Disclaimer](#disclaimer)

# Installation

OneMTA requires at least Java 8. OneMTA also requires the [protobuf-java](https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java) library to be installed.

Compiled jars can be found on Maven Central and the releases tab.

TBD

<p align="right">(<a href="#top">back to top</a>)</p>

# Features

TBD

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

### Updating Protobufs

- Download latest protobufs from [OneBusAway/onebusaway-gtfs-realtime-api](https://github.com/OneBusAway/onebusaway-gtfs-realtime-api/tree/master/src/main/proto/com/google/transit/realtime)
- Save proto files in `/protobuf`
- Change options and imports as defined in the `profobuf.sh` comments
- Run `protobuf.sh`
- Change all classes to abstract package private and add `SuppressWarnings("all")`

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