## [ GitHub app ] for Undabot

A simple android app that consumes the GitHub API (https://api.github.com/) in order to show a list of repositories from GitHub.

This project brings to table set of best practices, tools, and solutions:

* Pure Kotlin
* Model-View-Intent Architecture
* Android Jetpack
* Dependency Injection
* Material design


## Tech-stack

This project takes advantage of many popular libraries and tools of the Android ecosystem. Most of the libraries are in the stable version.

 * Tech-stack
     * [Kotlin](https://kotlinlang.org/)
     * [Dagger](https://https://dagger.dev/) - dependency injection
     * [Jetpack](https://developer.android.com/jetpack) - navigation
     * [RxJava](https://github.com/ReactiveX/RxAndroid) - reactive programming and background operations
     * [Chuck](https://github.com/jgilfelt/chuck) - http introspector for OkHttp - available on debug build only!!!

 * Architecture
     * [MVI]([https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started](https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started)) - application level

 ## Getting started

 This project was written with Android Studio Arctic Fox 2020.3.1 Patch 2 (Build #AI-203.7717.56.2013.7678000) and Gradle Plugin (7.0.2) and is compatible
 all the way to Android 21 (Lollipop). It also includes 3rd party dependencies which include (but not limited to):
 * Dagger Hilt (2.38.1)
 * Retrofit (2.9.0) + OkHttp (4.9.0) + OkHttpLoggingInterceptor (4.9.0)
 * Moshi (1.11.0)
 * RxJava (2.2.21) + RxBindings (3.1.0)
 * AndroidX navigation (2.3.5)
 * Glide (4.12.0)


Apart for the above mentioned dependencies this project requires nothing else to be ran on an Android enabled device.

### Android Studio

 1. Android Studio -> File -> New -> From Version control -> Git
 2. Enter `https://github.com/NedimKuj/github-app` into URL field
 3. Run default configuration

### Command line + Android Studio

 1. Run `git clone https://github.com/NedimKuj/github-app`
 2. Android Studio -> File -> Open
 3. Run default configuration
