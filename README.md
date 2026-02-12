<p align="center">
  <h1 align="center">🎓 Campus Connect</h1>
  <p align="center">
    <strong>Your all-in-one university companion app</strong>
  </p>
  <p align="center">
    <a href="#features">Features</a> •
    <a href="#tech-stack">Tech Stack</a> •
    <a href="#architecture">Architecture</a> •
    <a href="#getting-started">Getting Started</a> •
    <a href="#project-structure">Project Structure</a> •
    <a href="#api-endpoints">API</a> •
    <a href="#contributing">Contributing</a>
  </p>
</p>

---

## 📖 Overview

**Campus Connect** is a native Android application built with **Kotlin** that serves as a centralized hub for university students. It streamlines campus life by providing event discovery, interactive campus navigation, mentor communication, academic scheduling, and quick access to university resources — all within a single, elegant interface.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Secure JWT-based user registration and login |
| 📅 **Event Discovery** | Browse upcoming campus events with details, categories, pricing, and location info |
| 🗺️ **Interactive Campus Map** | Navigate campus buildings and facilities with Google Maps integration and path-finding |
| 💬 **Mentor Chat** | Real-time messaging with academic mentors for guidance and support |
| 📆 **Schedule & Calendar** | Manage your academic schedule with an intuitive calendar view |
| ❓ **Resources & FAQ** | Access curated university resources and frequently asked questions |
| 📍 **Location Services** | Get directions to event venues and campus locations |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **Min SDK** | 26 (Android 8.0 Oreo) |
| **Target SDK** | 35 |
| **UI Framework** | Android Views + Material Design Components |
| **Networking** | Retrofit 2 + OkHttp 3 |
| **Serialization** | Gson |
| **Image Loading** | Glide 4 |
| **Maps** | Google Maps SDK + Google Play Services Location |
| **Calendar** | Kizitonwose Calendar View 2.3 |
| **Async** | Kotlin Coroutines + Lifecycle Scope |
| **Build System** | Gradle (Kotlin DSL) |

---

## 🏗️ Architecture

The project follows a clean, layered architecture organized by feature concern:

```
┌─────────────────────────────────────┐
│              UI Layer               │
│   Activities (Login, Dashboard,     │
│   Chat, Map, Schedule, etc.)        │
├─────────────────────────────────────┤
│           Adapter Layer             │
│   RecyclerView Adapters for         │
│   Events, Mentors, Messages, FAQ    │
├─────────────────────────────────────┤
│          Network Layer              │
│   Retrofit ApiService + Client      │
│   (JWT Auth Interceptor)            │
├─────────────────────────────────────┤
│           Model Layer               │
│   Data Classes for all entities     │
│   (Events, Mentors, Messages, etc.) │
└─────────────────────────────────────┘
```

- **Token Management** — JWT tokens are stored in `SharedPreferences` and automatically attached to every API request via an OkHttp interceptor.
- **Coroutines** — All network calls are `suspend` functions executed within `lifecycleScope` for lifecycle-aware asynchronous operations.

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 11** or higher
- **Android SDK 35** installed
- A running backend server (the app expects a REST API at `http://10.0.2.2:5000/` when using the emulator)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Jaimex-3/CampusConnect.git
   cd CampusConnect
   ```

2. **Open in Android Studio**
   - Select *File → Open* and choose the project directory.
   - Wait for Gradle sync to complete.

3. **Configure Google Maps API Key**
   - The Maps API key is defined in `AndroidManifest.xml`.
   - Replace the existing key with your own from the [Google Cloud Console](https://console.cloud.google.com/).
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```

4. **Start the Backend Server**
   - Ensure your backend API is running on `localhost:5000`.
   - If running on a physical device, update `BASE_URL` in `RetrofitClient.kt` to your server's IP address.

5. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or simply press **▶ Run** in Android Studio targeting an emulator or device.

---

## 📁 Project Structure

```
CampusConnect/
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/example/campusconnect/
│           │   ├── MainActivity.kt
│           │   ├── adapter/
│           │   │   ├── EventsAdapter.kt       # Events list adapter
│           │   │   ├── FaqAdapter.kt           # FAQ items adapter
│           │   │   ├── MentorsAdapter.kt       # Mentors list adapter
│           │   │   └── MessagesAdapter.kt      # Chat messages adapter
│           │   ├── model/
│           │   │   └── Models.kt               # All data classes
│           │   ├── network/
│           │   │   ├── ApiService.kt           # Retrofit API interface
│           │   │   └── RetrofitClient.kt       # HTTP client with JWT auth
│           │   └── ui/
│           │       ├── LoginActivity.kt        # User login
│           │       ├── RegisterActivity.kt     # User registration
│           │       ├── DashboardActivity.kt    # Main dashboard with events
│           │       ├── ScheduleActivity.kt     # Calendar & schedule view
│           │       ├── MentorsListActivity.kt  # Browse available mentors
│           │       ├── ChatActivity.kt         # Messaging with mentors
│           │       ├── MapActivity.kt          # Interactive campus map
│           │       ├── ResourcesFaqActivity.kt # Resources & FAQ
│           │       └── EventDetailsActivity.kt # Event detail view
│           └── res/
│               ├── drawable/                   # Icons, backgrounds, event images
│               ├── layout/                     # XML layout files
│               ├── values/                     # Colors, strings, themes
│               └── xml/                        # Network security config
├── build.gradle.kts                            # Root build config
├── settings.gradle.kts                         # Project settings
├── gradle.properties                           # Gradle configuration
└── gradlew / gradlew.bat                       # Gradle wrappers
```

---

## 🌐 API Endpoints

The app communicates with a REST backend via the following endpoints:

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/register` | Create a new user account |
| `POST` | `/login` | Authenticate and receive a JWT token |
| `GET` | `/events` | Fetch all campus events |
| `GET` | `/mentors` | List available mentors |
| `GET` | `/messages?mentor_id={id}` | Retrieve messages with a mentor |
| `POST` | `/messages` | Send a message to a mentor |
| `GET` | `/faqs` | Get frequently asked questions |
| `GET` | `/resources` | Get university resource links |
| `GET` | `/locations` | Get campus building locations |
| `GET` | `/map_paths` | Get navigation paths between locations |

> **Note:** All endpoints except `/register` and `/login` require a valid JWT token in the `Authorization: Bearer <token>` header.

---

## 🔧 Configuration

| Setting | Location | Default |
|---|---|---|
| Base API URL | `network/RetrofitClient.kt` | `http://10.0.2.2:5000/` |
| Google Maps API Key | `AndroidManifest.xml` | Bundled key (replace for production) |
| JWT Token Storage | `SharedPreferences` (`app_prefs`) | — |
| Min SDK Version | `app/build.gradle.kts` | 26 |
| Target SDK Version | `app/build.gradle.kts` | 35 |

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Guidelines

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Write meaningful commit messages
- Add documentation for new features
- Test on both emulator and physical devices

---

## 📄 License

This project is for educational purposes. See the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Made with ❤️ for campus life
</p>
