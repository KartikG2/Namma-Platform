# 🚂 Namma-Platform — ನಮ್ಮ ಪ್ಲಾಟ್ಫಾರ್ಮ್

> **A Kannada-first railway guide for rural Karnataka passengers**

NammaPlatform is a premium, localized Android railway utility app built for Karnataka. It helps rural passengers — especially the elderly and non-English speakers — find their platform number, coach position, and live train location in Kannada, at small stations where announcements are often inaudible or only in Hindi/English.

---

## 📌 Problem Statement

At small railway stations across Karnataka (Arsikere, Tumkur, Hassan, Birur, Tiptur), passengers face three core problems:

- Announcements are in Hindi or English — rural and elderly passengers cannot understand them
- No digital display boards showing platform numbers or real-time delays
- Passengers don't know where the General or Ladies coach stops on the platform

**No existing app solves this in Kannada, offline-first, with a UI designed for low-literacy users.**

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏠 **Station Selection** | Search stations in Kannada script or English, with recent stations memory |
| 🚉 **Next 3 Trains Dashboard** | Platform number, delay status, departure time for the next 3 trains |
| 🪑 **Coach Layout Strip** | Visual horizontal strip showing where General / Ladies / Sleeper coaches stop |
| 🗺️ **Live Train Map Tracker** | Animated train marker on OpenStreetMap, moves station-to-station every 5 min |
| 🔊 **Kannada TTS Announcements** | "Help Me" button speaks platform and coach info aloud in Kannada |
| 📴 **Offline Mode** | Core timetable and station data works with no internet connection |
| 🌐 **Kannada / English Toggle** | Switch between Kannada and English UI at any time |

---

## 📸 Screenshots



| Splash Screen | Station Selection |
|:---:|:---:|
| <img width="166" height="367" alt="Splash Screen" src="https://github.com/user-attachments/assets/0a212838-b4d4-4f81-9a5c-8b7d37bb015b" /> | <img width="166" height="367" alt="Station Selection" src="https://github.com/user-attachments/assets/e849292c-b713-4b17-a69c-58f19c72c48c" /> |
 
| Train Dashboard | Coach Layout | Live Map |
|:---:|:---:|:---:|
| <img width="164" height="366" alt="Train Dashboard" src="https://github.com/user-attachments/assets/38c48c68-aff4-430e-8240-2c40f42d2d12" /> | <img width="166" height="370" alt="Coach Layout" src="https://github.com/user-attachments/assets/5a29abb9-da8e-4b04-909b-456dab0db77e" /> | <img width="166" height="370" alt="Live Map" src="https://github.com/user-attachments/assets/39ab20c6-b8f0-4882-9522-8ce091edd14a" /> |
---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| Maps | OSMDroid (OpenStreetMap) — no API key needed |
| Map Tiles | OpenFreeMap + OpenRailwayMap overlay |
| Train Data API | eRail.in API (free tier) |
| Local Database | Room (SQLite) — offline cache |
| Preferences | DataStore |
| Networking | Retrofit + OkHttp + Kotlin Serialization |
| Dependency Injection | Hilt |
| Text-to-Speech | Android TextToSpeech (Kannada locale) |
| Backend/Sync | Firebase Realtime Database (Spark free tier) |
| Analytics & Crashes | Firebase Analytics + Crashlytics |
| Image Loading | Coil |
| Min SDK | API 24 (Android 7.0) — covers 94% of Indian Android devices |

---

## 🏗️ Architecture

```
[ UI Layer — Jetpack Compose Screens ]
           |
[ Presentation Layer — ViewModels + StateFlow (Hilt) ]
           |
[ Domain Layer — UseCases + Domain Models ]
           |
[ Data Layer — RepositoryImpl ]
           |                          |
[ Remote — Retrofit + eRail API ]   [ Local — Room DB + DataStore ]
           |
[ Infrastructure — Firebase · OSMDroid · Android TTS ]
```

**Clean Architecture layers:**
- `presentation/` — Compose screens + ViewModels per screen (Splash, Station, Dashboard, Coach, Map)
- `domain/` — UseCases: `GetNextTrainsUseCase`, `GetTrainPositionUseCase`, `GetCoachLayoutUseCase`, `SearchStationsUseCase`
- `data/` — `TrainRepositoryImpl` with remote (Retrofit) and local (Room) sources
- `di/` — Hilt modules for all dependencies

---

## 📁 Folder Structure

```
app/src/main/
├── java/com/namma/platform/
│   ├── di/                        # Hilt dependency injection modules
│   ├── data/
│   │   ├── remote/                # Retrofit API service + DTOs + mappers
│   │   ├── local/                 # Room database, DAOs, entities
│   │   └── repository/            # Repository implementations
│   ├── domain/
│   │   ├── model/                 # Train, Station, TrainPosition, CoachLayout
│   │   └── usecase/               # All use cases
│   ├── presentation/
│   │   ├── splash/                # SplashScreen
│   │   ├── station/               # StationSelectionScreen + ViewModel
│   │   ├── dashboard/             # TrainDashboardScreen + ViewModel
│   │   ├── coach/                 # CoachLayoutScreen + ViewModel
│   │   └── map/                   # TrainMapScreen + ViewModel
│   ├── ui/
│   │   ├── theme/                 # Color, Typography, Theme (Material 3)
│   │   └── components/            # PlatformChip, DelayBadge, ShimmerCard, OfflineBanner
│   └── util/                      # TtsManager, NetworkUtil, Constants
└── res/
    └── assets/
        ├── stations.json          # 50 Karnataka stations with lat/lng
        └── coach_layouts.json     # Coach data for 20 popular Karnataka trains
```

---

## ⚙️ Setup & Installation

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK API 24+
- A free eRail.in API key ([register here](https://erail.in/))

### Step 1 — Clone the repository

```bash
git clone https://github.com/kartik-gangavati/namma-platform.git
cd namma-platform
```

### Step 2 — Add your eRail API key

Open (or create) `local.properties` in the project root and add:

```properties
ERAIL_API_KEY="your_api_key_here"
```

> ⚠️ Never commit `local.properties` to Git. It is already in `.gitignore`.

### Step 3 — Firebase setup (optional)

Firebase is used for Crashlytics and Analytics only. The app runs fully without it.

To enable Firebase:
1. Go to [Firebase Console](https://console.firebase.google.com/) → Create a project
2. Add an Android app with package name `com.namma.platform`
3. Download `google-services.json` and place it in the `app/` directory

> A placeholder `google-services.json.example` is included in `app/` to show the required format.

### Step 4 — Build and run

```bash
# From project root — command line build
./gradlew assembleDebug

# Or open in Android Studio → Sync Gradle → Run on device/emulator
```

The app will install on any device running Android 7.0 (API 24) or higher.

---

## 🗺️ How Live Train Tracking Works

Indian Railways does not expose real-time GPS publicly. NammaPlatform uses a **station-hop tracking** approach:

1. App calls eRail API every 5 minutes: `/live-train-status/{trainNo}/{date}`
2. Response returns `lastStation` + `nextStation` + `delayMinutes`
3. App places an animated marker at `lastStation` coordinates on the OSMDroid map
4. Marker smoothly animates toward `nextStation` using `ValueAnimator` over 5 minutes
5. On next API poll, marker repositions to the new `lastStation`

**Result:** User sees a train icon moving smoothly along OpenRailwayMap track lines — identical experience to live GPS tracking, zero cost.

---

## 📡 APIs & Services Used

| Service | Purpose | Cost |
|---|---|---|
| [eRail.in API](https://erail.in/) | Live train status, schedule, platform info | Free |
| [OpenFreeMap](https://openfreemap.org/) | Map tiles (no API key required) | Free |
| [OpenRailwayMap](https://openrailwaymap.org/) | Railway track overlay layer | Free (small apps) |
| [Firebase](https://console.firebase.google.com/) | Analytics + Crashlytics (optional) | Free (Spark plan) |
| Android TextToSpeech | Kannada voice announcements | Built-in, free |

**Total infrastructure cost: ₹0**

---

## 🎯 Target Users

- **Rukmini Amma (68)** — Elderly traveller at Arsikere, BSNL prepaid data, first-time smartphone user. Needs Kannada audio to know her platform.
- **Ravi (27)** — Daily factory worker commuter from Tumkur. Needs a fast, simple delay check without draining battery.

---

## 📋 Success Criteria

- Task completion (find platform number): ≥ 90% of users in < 30 seconds
- Kannada TTS plays correctly for all tested trains
- Coach layout renders matching IRCTC data
- Map loads train marker in < 3 seconds on 4G
- App works in airplane mode (cached data shown with offline banner)
- Crash rate < 1% (Firebase Crashlytics)

---

## 🗓️ What's Built & What's Next

| Phase | Timeline | Scope |
|---|---|---|
| **Phase 1 — MVP** ✅ | Weeks 1–6 | Project setup · Station search · Train dashboard · Coach layout strip · Live map tracker · Kannada TTS · Offline mode |
| **Phase 2 — Scale** | Months 2–4 | 500+ Karnataka stations · Train search by number · Delay push notifications · Hindi & Telugu TTS · PNR status |
| **Phase 3 — Platform** | Months 5–12 | All-India coverage · GenAI coach suggestions · Crowd-sourced platform reports · Play Store publish |

---

## 🔒 Privacy

- No personal data collected
- Firebase Anonymous Auth only (no login required)
- No location permission requested — map shows train position, not user position
- API key stored in `local.properties`, never committed to Git
- All network calls over HTTPS

---

## 🤝 Acknowledgements

- [eRail.in](https://erail.in/) for the free Indian Railways API
- [OpenStreetMap contributors](https://www.openstreetmap.org/) for map data
- [OpenRailwayMap](https://openrailwaymap.org/) for railway track tiles
- [OSMDroid](https://github.com/osmdroid/osmdroid) for the Android map library
- MindMatrix VTU Internship Program for the project opportunity

---

## 👤 Author

**Kartik I Gangavati** — 1CC22CS040
MindMatrix VTU Internship Program · Bengaluru, Karnataka

---

*Built with ₹0 budget · 100% open-source stack · Offline-first · Kannada-first*
