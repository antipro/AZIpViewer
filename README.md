# ZipViewer - Private Archive Viewer

An Android native app for viewing images directly from encrypted ZIP, RAR, and 7Z archives.

## Features
- **Privacy First**: All data is stored in internal storage and cannot be accessed by other apps.
- **Encrypted Archives**: Full support for password-protected ZIP files using `zip4j`.
- **Files Grid and List**: Show zip files in two different views.
- **Zip Editor**: Registers itself as a zip file editor, so can be called by other apps (like Telegram) to open zip files.

## Requirements
- Java 17 or later (Java 21 recommended)
- Gradle 8.2+
- Android SDK 26+
- Package: `com.bitifyware.zipviewer`

## Project Structure
```
ZipViewer/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/bitifyware/zipviewer/
│   │       │   └── MainActivity.java
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml
│   │       │   ├── values/
│   │       │   │   ├── colors.xml
│   │       │   │   ├── strings.xml
│   │       │   │   └── themes.xml
│   │       │   └── mipmap-*/
│   │       │       └── ic_launcher.*
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── gradlew
```

## Android Manifest
The app is registered as a generic File Viewer (appears in "Open With" menus) with this `<intent-filter>` in `app/src/main/AndroidManifest.xml`:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="content" />
    <data android:mimeType="application/zip" />
    <data android:mimeType="application/x-zip-compressed" />
    <data android:mimeType="application/x-rar-compressed" />
    <data android:mimeType="application/x-7z-compressed" />
</intent-filter>
```

## Building the App
```bash
./gradlew assembleDebug
```

## Dependencies
- AndroidX AppCompat
- Material Design Components
- RecyclerView for grid/list views
- zip4j for encrypted ZIP file handling

## Privacy
All archive files opened by the app are copied to the app's internal storage directory, which is sandboxed and cannot be accessed by other applications, ensuring complete privacy.

