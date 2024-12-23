# Android File Manager

A simple and efficient Android file manager application that allows users to browse and manage files on their device.

## Features

- Browse files and directories on your Android device
- View text files (txt, json, xml, md)
- Navigate through directory hierarchy
- Clean and intuitive user interface
- Proper handling of storage permissions for Android 10+ (API level 29+)

## Screenshots

(Coming soon)

## Requirements

- Android 6.0 (API level 23) or higher
- Storage permission for accessing files

## Setup

1. Clone the repository:
```bash
git clone https://github.com/giolynx104/file-manager.git
```

2. Open the project in Android Studio

3. Build and run the application:
```bash
./gradlew installDebug
```

## Permissions

The app requires the following permissions:
- `READ_EXTERNAL_STORAGE` - For accessing files on the device
- `MANAGE_EXTERNAL_STORAGE` - For Android 10+ to access all files

## Architecture

- Written in Kotlin
- Uses Android View system for UI
- Follows Android best practices and Material Design guidelines
- Implements proper permission handling for different Android versions

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 