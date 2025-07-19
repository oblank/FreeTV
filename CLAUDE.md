# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FreeTV is an Android TV application written in Kotlin that serves as a web-based TV streaming interface. The app launches directly into a WebView-based interface rather than a traditional Android TV leanback experience.

## Build Commands

- **Build the project**: `./gradlew build`
- **Clean build**: `./gradlew clean build`
- **Install debug APK**: `./gradlew installDebug`
- **Build release APK**: `./gradlew assembleRelease`

## Project Structure

- **Package**: `com.oblank.freetv`
- **Target SDK**: 36, Min SDK: 21
- **Main entry point**: `MainActivity.kt` - immediately launches `WebViewActivity`
- **Core functionality**: WebView-based streaming interface in `WebViewActivity.kt`
- **Architecture**: Traditional Android TV app structure with leanback components, but primarily web-based UI

## Key Components

- **MainActivity**: Entry point that immediately redirects to WebViewActivity
- **WebViewActivity**: Core WebView implementation for the streaming interface
- **Leanback Activities**: DetailsActivity, PlaybackActivity, BrowseErrorActivity (traditional Android TV components)
- **Presenters**: CardPresenter, DetailsDescriptionPresenter for leanback UI components
- **Data Models**: Movie, MovieList for content representation

## Dependencies

- AndroidX Leanback library for TV UI components
- Glide for image loading
- AndroidX WebKit for enhanced WebView functionality
- Core Kotlin extensions

## Development Notes

- App is configured for landscape orientation only
- Requires android.software.leanback feature
- Uses LEANBACK_LAUNCHER category for Android TV launcher integration
- WebView handles the primary user interface instead of native Android TV components