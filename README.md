# FriendlyNeighborAndroid

![Android CI](https://github.com/BroadwayFriends/FriendlyNeighborAndroid/workflows/Android%20CI/badge.svg)

The Android Application for FriendlyNeighbor. Discoverand ask for help from people in your neighborhood.
This is a vanilla Android project written in Java and Kotlin and supports **Android Marshmallow** and above.

Related backend repositories:
- [FriendlyNeighborCore service](https://github.com/2DSharp/FriendlyNeighborCore)
- [FriendlyNeighbor REST Server](https://github.com/agnibha-chatterjee/friendly-neighbor-api)

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/BroadwayFriends/FriendlyNeighborAndroid.git
```

## Configuration

Update the `strings.xml` with your own Firebase Cloud Messaging and Firebase Auth keys.

You need to be connected to https://fn.twodee.me to be able to communicate over REST.

## Build variants
Use the Android Studio *Build Variants* button to choose between **production** and **staging** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## Maintainers
This project is mantained by:
* [Dedipyaman Das](https://github.com/2dsharp)
* [Akhil Surendran](https://github.com/akhillllldev)
* [Priyam Sahoo](https://github.com/priyamsahoo)
* [Ritwik Badola](https://github.com/ritwikbadola)
* [Agnibha Chatterjee](https://github.com/agnibha-chatterjee)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Push your branch (git push origin my-new-feature)
5. Create a new Pull Request
