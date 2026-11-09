# Blocker2

Offline-first Android self-control app.

## Build
GitHub Actions builds debug and release APKs with JDK 17 and Gradle 8.13. Download the `blocker2-apks` artifact from the workflow run.

## Implemented in this foundation
- Local word/phrase list stored in app-private SharedPreferences.
- Accessibility-based text scanning, excluding Blocker2 itself.
- Temporary foreground-app exit after a configured word match.
- Persian/Arabic letter and whitespace/half-space normalization.
- Android 10 (API 29) minimum.

## Current limits
Accessibility can inspect only text exposed by other apps. Website/DNS filtering, Telegram channel rules, commitment mode, and full backup/restore are not implemented yet. Release APKs from this workflow are not Play Store signed.

## Privacy
No account, remote server, or analytics service is used. Accessibility text is processed locally and is not uploaded.
