# Event Management App

Kotlin + XML + MVVM + Repository + Firebase Authentication + Firestore + FCM + MPAndroidChart.

## Included
- Email/password sign-up, login, password reset, persistent Firebase session
- Firestore events under `users/{uid}/events/{eventId}`
- Add/edit/delete events
- Reverse chronological real-time list
- Title/date validation
- Dashboard total/upcoming/past statistics
- MPAndroidChart monthly event graph
- Material DayNight theme (dark mode)
- FCM notification receiver
- Unit-test setup

## Firebase setup
Create a Firebase project, add Android package `com.example.eventmanagement`, enable Email/Password Auth and Firestore, download `google-services.json`, and put it at `app/google-services.json`. The real file is intentionally not included.

## Offline persistence
Firestore Android SDK uses local caching/offline behavior. Configure explicit Firestore settings if your Firebase SDK/project requires custom persistence settings.

## FCM reminders
The service handles incoming FCM notifications. Scheduling a reminder at an arbitrary future event time requires a trusted backend/Cloud Function or another server-side scheduler to send FCM.

## Production security
Use authenticated Firestore security rules so users can read/write only their own `users/{uid}/events` documents.
