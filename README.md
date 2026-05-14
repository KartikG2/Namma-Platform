# NammaPlatform

NammaPlatform is a premium, localized railway utility app tailored for Karnataka. It provides live train status, upcoming stations, and coach layouts, with full Kannada language support.

## Setup Instructions

1. **Clone the repository**
2. **Add API Keys:** Add your eRail API key to `local.properties`:
   ```properties
   ERAIL_API_KEY="your_api_key_here"
   ```
3. **Firebase (Optional):** To enable Crashlytics and Analytics, add your `google-services.json` file to the `app/` directory.
4. **Build and Run:** Use Android Studio to sync Gradle and build the project.

## APIs & Services Used
- [eRail.in API](https://erail.in/) - For live train status, schedule, and live tracking.
- [OpenRailwayMap](https://openrailwaymap.org/) - Map tiles overlaid on OpenFreeMap for detailed railway tracks.
- [Firebase Console](https://console.firebase.google.com/) - For Analytics and Crashlytics.

## Architecture Diagram

```
[ UI Layer - Jetpack Compose ]
       |
[ Presentation Layer - MVVM (Hilt, ViewModels) ]
       |
[ Domain Layer - UseCases & Models ]
       |
[ Data Layer - RepositoryImpl ]
       |-----------------------------------|
       |                                   |
[ Remote (Retrofit + KotlinX) ]     [ Local (Room + DataStore) ]
```

## Screenshots
*(Add screenshots here)*
