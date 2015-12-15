# Android Echoprint

## Code from the MASL blog post available [here](http://masl.cis.gvsu.edu/2012/01/25/android-echoprint/)

[Echoprint](http://echoprint.me) is an open source library to fingerprint Audio, in this repo you'll find the code needed to make it run under Android.

This repo contains 3 folders:

`EchoprintLib` Is an Eclipse project with an already compiled native Echoprint library that you can include in your own projects as an external library project.

`EchoprintTest` Is a sample Eclipse project that uses the EchoprintLib library.

`jni` Contains the NDK/jni code needed to compile the native library yourself. You'll need to download the Echoprint source, as well as the Boost libraries in order to compile it.

## CODE ADDED BY ME 
This directory contains the following:
1. New UI for the Android application
2. We can add fingerprints to the local database for the songs available on the user's phone
3. A 15-20 seconds huming of the song can be used to identify the song

PLEASE READ README-ORIGINAL.MARKDOWN FOR MORE DETAILS ABOUT THE ORIGINAL PROJECT.
THANKS TO THE ORIGINAL DEVELOPER :)