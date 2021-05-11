# Android CustomTabs
![Maven Central](https://img.shields.io/maven-central/v/de.peilicke.sascha/android-customtabs)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20CustomTabs-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5872)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/saschpe/android-customtabs.svg?branch=master)](https://travis-ci.org/saschpe/android-customtabs)
<a href="http://www.methodscount.com/?lib=saschpe.android%3Acustomtabs%3A2.0.0"><img src="https://img.shields.io/badge/Methods and size-core: 100 | deps: 19640 | 25 KB-e91e63.svg"/></a>

Chrome CustomTabs for Android demystified. Simplifies development and provides
higher level classes including fallback in case Chrome isn't available on device.

# Usage
How to create a new custom tab intent and start it with a keep-alive service
as well as a fallback to plain old WebView should Chrome not be available on
the device:

```java
CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
        .addDefaultShareMenuItem()
        .setToolbarColor(this.getResources().getColor(R.color.colorPrimary))
        .setShowTitle(true)
        .setCloseButtonIcon(backArrow)
        .build();

// This is optional but recommended
CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);

// This is where the magic happens...
CustomTabsHelper.openCustomTab(this, customTabsIntent,
        Uri.parse("https://github.com/saschpe/android-customtabs"),
        new WebViewFallback());
```

Preload CustomTabs in your Application.java to warm-up early and reduce start-up
time:

```java
// Preload custom tabs service for improved performance
// This is optional but recommended
registerActivityLifecycleCallbacks(new CustomTabsActivityLifecycleCallbacks());
```

## Screenshots
<img alt="Screenshot 1" src="assets/device-art/customtabs-1.png" width="256" />
<img alt="Screenshot 2" src="assets/device-art/customtabs-2.png" width="256" />
<img alt="Screenshot 3" src="assets/device-art/customtabs-3.png" width="256" />

# Download
Artifacts are published to [Maven Central][maven-central]:
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("de.peilicke.sascha:android-customtabs:3.0.3")
}
```

# In use by
* [Alpha+ Player - Unofficial player for Soma FM](https://play.google.com/store/apps/details?id=saschpe.alphaplus)
* [GameOn - Get games on sale](https://play.google.com/store/apps/details?id=saschpe.gameon)
* [Planning Poker - SCRUM Cards](https://play.google.com/store/apps/details?id=saschpe.poker) - Open Source on [Github](https://github.com/saschpe/PlanningPoker)

# License

    Copyright 2017 Sascha Peilicke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[maven-central]: https://search.maven.org/artifact/de.peilicke.sascha/android-customtabs
