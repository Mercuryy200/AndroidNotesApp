# AndroidNotesApp

[![Android CI](https://github.com/USERNAME/AndroidNotesApp/actions/workflows/android-ci.yml/badge.svg)](https://github.com/USERNAME/AndroidNotesApp/actions/workflows/android-ci.yml)

A production-quality task management Android application built with modern Android development best practices. Create, organise, and track tasks with priority levels, due dates, completion status, search/filter, persistent preferences, and timely notifications.

## Screenshots

|             Task List             |              Task Detail              |             Edit Task             |
| :-------------------------------: | :-----------------------------------: | :-------------------------------: |
| [Task List](/images/TaskList.png) | [Task Detail](/images/TaskDetail.png) | [Task Edit](/images/TaskEdit.png) |

## Features

- **Create & Edit Tasks** — Add tasks with a name, note, due date, and priority level
- **Priority Levels** — Categorise tasks as Low, Medium, or High priority with colour-coded chips
- **Due Dates** — Set due dates with a date picker; overdue tasks are highlighted in red
- **Mark Complete** — Check off tasks directly from the list view
- **Search & Filter** — Real-time search by name/note; filter by priority; hide completed tasks
- **Sort Order** — Toggle between ascending/descending due-date sort (persisted across restarts)
- **Reminders** — Background notification one hour before each task's due date (WorkManager)
- **Dependency Injection** — Hilt manages all object lifecycles
- **Localization** — English and French

## Tech Stack

| Layer        | Technology                                       |
| ------------ | ------------------------------------------------ |
| UI           | Jetpack Compose + Material 3                     |
| Architecture | MVVM + Repository Pattern + UiState sealed class |
| DI           | Hilt                                             |
| Database     | Room (SQLite)                                    |
| Async        | Kotlin Coroutines + Flow                         |
| Navigation   | Jetpack Navigation Compose                       |
| Background   | WorkManager + HiltWorker                         |
| Preferences  | DataStore (Preferences)                          |
| Date/Time    | ThreeTenABP                                      |
| Build        | Kotlin DSL + KSP + Version Catalog               |
| CI           | GitHub Actions                                   |

## Architecture

```
Compose UI (TaskListScreen / TaskDetailScreen / EditTaskScreen)
        ↓  collectAsState / observeAsState
TaskViewModel  (@HiltViewModel)
        ↓  combine(allTasks, searchQuery, filterPriority, showCompleted, userPrefs)
TaskRepository (interface)          UserPreferencesRepository
        ↓                                   ↓
TaskRepositoryImpl                    DataStore<Preferences>
        ↓
    TaskDao (Room)
        ↓
    SQLite  (tasks_db)

WorkManager ← TaskReminderWorker (@HiltWorker)
```

## Project Structure

```
src/main/java/com/example/notesapp/
├── NotesApplication.kt          — @HiltAndroidApp, WorkManager config, notification channel
├── MainActivity.kt              — @AndroidEntryPoint, Compose host
├── di/
│   ├── DatabaseModule.kt        — provides TaskDatabase, TaskDao, TaskRepository
│   └── PreferencesModule.kt     — provides DataStore<Preferences>
├── data/
│   ├── database/
│   │   ├── TaskDatabase.kt
│   │   ├── TaskDao.kt
│   │   ├── TaskRepository.kt    — interface
│   │   └── TaskRepositoryImpl.kt
│   ├── model/
│   │   └── Task.kt
│   └── preferences/
│       └── UserPreferencesRepository.kt  — SortOrder, UserPreferences, DataStore wrapper
├── notifications/
│   └── NotificationHelper.kt   — CHANNEL_ID constant, createNotificationChannel()
├── workers/
│   └── TaskReminderWorker.kt   — @HiltWorker, posts reminder notification
├── ui/
│   ├── screens/
│   │   ├── TaskListScreen.kt   — search bar, filter chips, sort toggle, UiState handling
│   │   ├── TaskDetailScreen.kt
│   │   └── TaskEditScreen.kt
│   ├── components/
│   │   ├── TaskItem.kt
│   │   ├── PriorityChip.kt
│   │   ├── PriorityDropdown.kt
│   │   └── DueDatePicker.kt
│   ├── viewmodel/
│   │   ├── TaskViewModel.kt    — @HiltViewModel, filteredUiState, WorkManager scheduling
│   │   └── TaskUiState.kt      — sealed class: Loading | Success | Error
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── navigation/
    └── NavGraph.kt             — hiltViewModel(), Screen sealed class
```

## Requirements

- Android Studio Ladybug or newer
- Android SDK 35
- Kotlin 2.0.21 + KSP 2.0.21-1.0.28

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/USERNAME/AndroidNotesApp.git
   ```
2. Open the project in Android Studio.
3. Let Gradle sync and download dependencies.
4. Run on an emulator or physical device running Android 7.0 (API 24) or higher.

## Contributing

```bash
# Run all unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Run instrumented tests (requires connected device / emulator)
./gradlew connectedAndroidTest
```

## Compatibility

| Setting     | Value            |
| ----------- | ---------------- |
| Minimum SDK | 24 (Android 7.0) |
| Target SDK  | 35 (Android 15)  |
| Language    | Kotlin 2.0.21    |
| JVM Target  | 11               |

## License

This project is for educational and portfolio purposes.
