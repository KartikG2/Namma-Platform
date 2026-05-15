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
<img width="166" height="367" alt="Screenshot 2026-05-07 022931" src="https://github.com/user-attachments/assets/b7646d22-4cbe-47d1-bfd4-aa7cd87a1e99" />
<img width="166" height="367" alt="Screenshot 2026-05-07 130527" src="https://github.com/user-attachments/assets/2c3dca41-2048-4a82-a8ab-fb461a89a142" />
<img width="166" height="367" alt="Screenshot 2026-05-07 130607" src="https://github.com/user-attachments/assets/00be9cdf-3c9d-4060-b5b0-6de1aaeb9e58" />
<img width="166" height="367" alt="Screenshot 2026-05-07 130631" src="https://github.com/user-attachments/assets/e9e782df-a706-4195-8418-e2f04aca9fee" />
<img width="166" height="367" alt="Screenshot 2026-05-07 130648" src="https://github.com/user-attachments/assets/093086ef-3f86-4493-8b7a-b8d4ed725c3b" />

