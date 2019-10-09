# STPhotoMap - Android

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/mikelanza/st-photo-map-android/blob/master/LICENSE)

**STPhotoMap** is an Android library designed to display photos from [Streetography](https://streetography.com) on the `Google` map.

- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Issues](#issues)
- [TODOs](#todos)
- [License](#license)

## Screenshots

<img src="https://user-images.githubusercontent.com/6670019/66481412-8bf56a80-eaa9-11e9-9769-ea1216a5f691.png" width="18%"></img> 
<img src="https://user-images.githubusercontent.com/6670019/66481442-9b74b380-eaa9-11e9-8875-a69f811579e3.png" width="18%"></img> 
<img src="https://user-images.githubusercontent.com/6670019/66481462-a7f90c00-eaa9-11e9-8186-f78f37a44ffa.png" width="18%"></img>
<img src="https://user-images.githubusercontent.com/6670019/66481483-b8a98200-eaa9-11e9-97cd-47dc812ca48e.png" width="18%"></img> 
<img src="https://user-images.githubusercontent.com/6670019/66481522-d0810600-eaa9-11e9-8051-4e59202c9298.png" width="18%"></img>

## Installation

1. Add the `JitPack` repository to your root `build.gradle` at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency to your app's `build.gradle`:
```
dependencies {
    implementation 'com.github.mikelanza:st-photo-map-android:latest-release'
}
```

## Usage

See the [STPhotoMap - Android Examples](https://github.com/mikelanza/st-photo-map-android-examples) project for usage.

## Issues

∙ Selecting a photo marker from a cluster item does not work. It needs to be implemented.

## TODOs

∙ Implement photo carousel. Checkout the branch: `feature/carousel_implementation`.
∙ Implement photo collection activity or a library for photo collection.
∙ Implement photo details activity or a library for photo details.

## Contributing

Issues and pull requests are welcome!

## Author

[Streetography](https://streetography.com/)

## License

**STPhotoMap** is released under the MIT license. See [LICENSE](https://github.com/mikelanza/st-photo-map-android/blob/master/LICENSE) for details.
