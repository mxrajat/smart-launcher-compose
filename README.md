# SmartLauncherCompose 🌸

An open-source, ultra-clean Android Home Launcher built with **Jetpack Compose**, **Kotlin Coroutines**, and **Material 3**, inspired by the signature features of Smart Launcher (`ginlemon.flowerfree`).

---

## ✨ Features

1. **🌸 Signature Flower Layout**:
   - Radial / circular quick launch apps positioned like flower petals around the center.
   - Smooth animated ripple and touch feedback.
   - Long-press to toggle favorite pin.

2. **⏰ Minimalist Clock & Date Widget**:
   - Real-time digital clock with clean typographic hierarchy.
   - Live date display with zero battery drain.

3. **🗂️ Categorized App Drawer**:
   - Auto-sorts installed apps into Smart Categories:
     - 💬 **Communication** (WhatsApp, Telegram, Phone, Messages)
     - 🌐 **Internet** (Chrome, Firefox, Social Media)
     - 🎵 **Media** (YouTube, Spotify, Camera, Gallery)
     - 🎮 **Games** (Auto-detected games)
     - 🛠️ **Tools & Utilities** (Calculator, Notes, Files)
     - ⚙️ **Settings**
     - 📱 **All Apps**
   - Quick Category Navigation Rail on the left side.

4. **🔍 Instant Search**:
   - Search apps by title instantly with high-performance real-time filtering.

5. **🖼️ True Wallpaper Transparency**:
   - Edge-to-edge transparent system bars showing your live/static system wallpaper underneath.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin 2.0
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel) + StateFlow
- **System Integration:** Android `PackageManager` + `QUERY_ALL_PACKAGES`
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)

---

## 🚀 How to Build & Run

### In Android Studio:
1. Open Android Studio -> **File -> Open**.
2. Select `/home/rajatmishra/.gemini/antigravity/scratch/smart-launcher-compose`.
3. Wait for Gradle Sync to complete.
4. Connect an Android phone (or launch an Emulator) and click **Run (Shift + F10)**.

### Set as Default Launcher:
1. On your phone, go to **Settings -> Apps -> Default Apps -> Home App**.
2. Select **Smart Launcher**.
