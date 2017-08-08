# はじめに

本ソースコードは Unity で altbeacon を使うためのサンプルコード
（iBeaconの信号のみ受信するようにフォーマット指定を行っています）

# android ディレクトリ

Android Studio 2.3.3 で altbeacon-debug/release.aar を作るためのプロジェクト

# unity-altbeacon-demo ディレクトリ

Unity 5.6.3f1 で AltBeacon を使ったサンプルを動かすためのサンプル

---

# ライブラリ構成

unity-altbeacon-demo : 本プロジェクトで作成

altbeacon-release.arr : 本プロジェクトで作成

android-beacon-library-2.11.aar : AltBeacon本体

---

# ポイント

- 動作確認不十分です。
- AndroidManifest.xml で起動時の Activity を AltBeacon 用に差し替えています。
　com.example.grape.altbeacon.AltBeaconActivity
- 使い方は AltBeaconTestGUI.cs を参考にしてください。
