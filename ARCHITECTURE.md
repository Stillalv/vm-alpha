# Architecture & Reverse Engineering Triage Report (`VM-v3.2.86-official.apk`)

> **Methodology:** REA (`morluto/rea` — `rea-agents@6.7.0`) Evidence-Backed Static Analysis & Clean-Room Reconstruction
> **Target Artifact:** `C:\Users\uni\Downloads\VM-v3.2.86-official.apk`
> **Target SHA-256:** `a8f6302f14554b14d0e2cf81cc0f61d4f496e18393985715a0a60aa2d8813c81`
> **Artifact Size:** `153,078,500` bytes (~146 MB)

---

## 1. REA Evidence Ledger

| Operation | REA Tool / Provider | Evidence ID | Summary |
| :--- | :--- | :--- | :--- |
| Archive Inventory | `rea:inventory_artifact` | `ev_a5ab7eec0da5f141d3574db85571d41010ccc7bd412121d39b1b1ad17002b4b3` | 1,910 archive entries, 1,778 unique content-addressed nodes, 0 integrity contradictions |
| Artifact Graph Inspection | `rea:inspect_artifact` | `ev_ea76a441a6ede20256a21d9f9f33f72b353cf1ee066a677f010fa82404ba8a4f` | Complete container graph (`agm_fde9eb1b2f824bfaf5833ee6e9e32c3b43a344475aebb80961fc708a093a50b5`) |
| Resource & Manifest Decode | `rea:decode_android_resources` (`apktool 2.11.1`) | `ev_9922e1bb679c7b78653483ffbe3a7fd01314229b6924b7fb7f8a64895082bbc8` | 2,024 decoded files, `AndroidManifest.xml`, `res/values/public.xml`, localized string tables |
| Package & DEX Inspection | `rea:inspect_android_package` (`jadx 0.7.1`) | `ev_63420fc4d91138b83fba237fdaf0d4298078013352c51dfa0e13a8123202f53f` | 14,649 total classes across 3 DEX files (`classes.dex`, `classes2.dex`, `classes3.dex`), 1,909 resources, 34 permissions |

---

## 2. Manifest & Package Triage

### 2.1 Package & Build Identity
- **Application ID (`package`):** `com.clone.android.dual.space` (Virtual Master / Android Virtual Machine Engine)
- **Version Code / Name:** `3286` / `3.2.86`
- **SDK Levels:** `minSdk = 22`, `targetSdk = 36`, `compileSdk = 36`
- **Original Build System (from `kotlin-tooling-metadata.json` & `app-metadata.properties`):**
  - Gradle `8.14.5`
  - Android Gradle Plugin (AGP) `8.10.1`
  - Kotlin Android Plugin `2.2.0` (`jvmTarget = 17`)
- **Application Entry Class:** `com.android.vmapp.VMApp`

### 2.2 Permissions (`uses-permission`)
- **Storage & File Management:** `MANAGE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`
- **Network & VPN:** `INTERNET`, `ACCESS_NETWORK_STATE`, `CHANGE_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE`
- **Virtual Machine Display & Overlay:** `SYSTEM_ALERT_WINDOW`, `REORDER_TASKS`, `WAKE_LOCK`, `POST_NOTIFICATIONS`, `VIBRATE`
- **Foreground Services:** `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_SPECIAL_USE`, `FOREGROUND_SERVICE_CONNECTED_DEVICE`, `FOREGROUND_SERVICE_DATA_SYNC`
- **Hardware Passthrough (Camera/Audio/Phone/Location/Biometric):** `READ_PHONE_STATE`, `RECORD_AUDIO`, `CAMERA`, `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION`, `USE_BIOMETRIC`, `USE_FINGERPRINT`
- **Package & Shizuku Integration:** `QUERY_ALL_PACKAGES`, `com.android.permission.GET_INSTALLED_APPS`, `moe.shizuku.manager.permission.API_V23`
- **Monetization & Play Services:** `com.android.vending.BILLING`, `com.google.android.gms.permission.AD_ID`, `ACCESS_ADSERVICES_AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_TOPICS`, `com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE`

### 2.3 Declared Application Components

#### Activities (App-Owned)
1. `com.android.vmapp.ui.MainActivity` (`MAIN` / `LAUNCHER`, `Theme.App.Starting`) — Bottom-navigation host (`VMFragment`, `YouFragment`)
2. `com.android.vmapp.ui.eula.EulaActivity` — Terms of Service / Privacy Policy acceptance screen
3. `com.android.vmapp.ui.billing.BillingActivity` — VIP subscription & one-time purchase screen
4. `com.android.vmapp.ui.settings.SettingsActivity` — Global app settings (theme, language, Shizuku phantom process fix)
5. `com.android.vmapp.ui.about.AboutActivity` — Version info, social links, OSS licenses
6. `com.android.vmapp.ui.vm.add.ImportsActivity` — Import installed host apps or APK files into a VM
7. `com.android.vmapp.ui.vm.add.RecordsActivity` — App installation/import progress & history records
8. `com.android.vmapp.ui.vm.settings.VMSettingsActivity` — Per-VM configuration dashboard
9. `com.android.vmapp.ui.vm.settings.VMSensorActivity` — Per-VM sensor passthrough settings
10. `com.android.vmapp.ui.vm.settings.VMLocationActivity` — Per-VM GPS/location spoofing & passthrough
11. `com.android.vmapp.ui.vm.settings.VMNetworkActivity` — Per-VM network/SOCKS5 proxy settings
12. `com.android.vmapp.ui.vm.settings.VMPhoneActivity` — Per-VM IMEI/IMSI/ICCID/SIM/Carrier settings
13. `com.android.vmapp.ui.vm.settings.VMHWInfoActivity` — Per-VM device model/brand/GPU spoofing
14. `com.android.vmapp.ui.vm.settings.VMSecurityActivity` — Per-VM Root, Magisk, Xposed, Google Play Services toggles
15. `com.android.vmapp.ui.vm.settings.VMResolutionActivity` — Per-VM display width, height, DPI, refresh rate
16. `com.android.vmapp.ui.vm.settings.VMNotificationActivity` — Per-VM notification forwarding settings
17. `com.android.vmapp.ui.vm.create.VMCreateActivity` — Wizard for selecting a ROM and naming a new VM
18. `com.android.vmapp.ui.vm.rom.VMRomActivity` — ROM management & download screen
19. `com.android.vmapp.ui.vm.advanced.SettingsActivity` — Floating window / picture-in-picture / mini-ball settings
20. `com.android.vmapp.vm.VMStopActivity` — Transparent helper activity to stop a running VM instance
21. `com.android.vmapp.vm.VMStartActivity0` .. `VMStartActivity3` (`singleTask`, affinities `.vm0`..`.vm3`) — Multi-task VM display containers
22. `com.android.vmapp.vm.VMDisplayActivity` — Direct VM OpenGL/SurfaceView display activity
23. `com.android.vmapp.vm.VMReportActivity` — Crash report & diagnostic log uploader
24. `com.android.vmapp.ui.vm.backup.VMBackupActivity` — Export/backup VM snapshot
25. `com.android.vmapp.ui.vm.backup.VMBackupRestoreActivity` — Restore VM snapshot from storage
26. `com.android.vmapp.ui.vm.privacy.PrivacyMainActivity` — Privacy lock / calculator / PIN vault screen
27. `com.android.vmapp.ui.vm.privacy.PrivacyAddActivity` — Add VMs to privacy lock
28. `com.android.vmapp.ui.vm.privacy.PrivacySettingActivity` — Configure PIN/biometric & timeout policy
29. `com.android.vmapp.ui.points.FreeVipActivity` — Rewarded missions & coins redemption for VIP time
30. `com.android.vmapp.ui.points.WatchAdActivity` — Transparent activity for rewarded ad playback
31. `com.android.vmapp.ui.vm.carrier.CarrierListActivity` — Country & mobile carrier selector
32. `com.phantom.killer.disabler.ui.MainActivity` — Android 12+ Phantom Process Killer disabler tool (Wireless ADB / Shizuku)
33. `com.phantom.killer.disabler.ui.shizuku.ShizukuActivity` — Shizuku permission & execution screen
34. `com.android.ui.auth.*` & `com.android.auth.internal.*` — Embedded AuthUI sign-in flows (Email, Phone, Google, Facebook, IdP)

#### Services (App-Owned)
1. `com.android.vmapp.vm.VMCoreService` (`foregroundServiceType="specialUse"`) — Foreground service keeping the virtual Android OS engine alive
2. `com.android.vmapp.vm.VMVpnService` (`android.permission.BIND_VPN_SERVICE`) — Host VPN service for VM network tunneling
3. `com.android.vmcore.service.BinderService` — IPC Binder bridge connecting the host process with guest VM rootfs processes (`libvm.so`)
4. `com.phantom.killer.disabler.core.overlay.FloatingPairingOverlayService` — Wireless ADB pairing code overlay

#### Receivers & Providers (App-Owned)
1. `com.android.vmapp.receivers.ShareSheetReceiver` — Receives share sheet intents for APK import
2. `com.phantom.killer.disabler.core.notification.PairingNotificationReceiver` — Handles wireless debugging pairing code submission
3. `androidx.core.content.FileProvider` (`com.clone.android.dual.space.fileprovider`)
4. `rikka.shizuku.ShizukuProvider` (`com.clone.android.dual.space.shizuku`)
5. `com.android.ui.auth.data.client.AuthUiInitProvider` (`com.clone.android.dual.space.authuiinitprovider`)
6. `com.lzf.easyfloat.EasyFloatInitializer` (`com.clone.android.dual.space.EasyFloatInitializer`)

---

## 3. Architecture & Module Deobfuscation Map

The APK was compiled from **4 internal modules** with R8 + StringFog (`e6.WWWWWWWW.m14522WWWWWWWW`, XOR UTF-8 byte decryption):

### 3.1 Module 1: `:vmcore` (`com.android.vmcore.*` & obfuscated `q4`..`b5`)
Provides the Java/Kotlin host layer over the native Linux/Android virtualization engine (`libvm.so`, `libkr32.so`, `libkr64.so`, `libkrloader32.so`, `libkrloader64.so`, `libun7z.so`):
- `com.android.vmcore.NativeHelper` — JNI entry points (`startOS`, `getProcessList`, `clearZombieProcess`, `chmodRecursively`, `deleteRecursively`, `printXAttr`, `buildVMProcessReport`)
- `com.android.vmcore.KLog` — Engine logger
- `com.android.vmcore.VMNetworkConfig` & `VMProcessInfo` — Data models for VM networking & running guest processes
- `com.android.vmcore.hal.*` (`AudioService`, `BatteryService`, `DisplayService`, `HALManager`, `InputService`, `NetlinkManager`, `phone.AdnRecord`, `phone.EncodeException`) + `u4.*` (Location/GPS/Sensor HAL) + `v4.*` (Telephony/SIM/ICC RIL emulation)
- `com.android.vmcore.service.BinderService` & `x4.*` (`IBinderService`) — Native/Java Binder setup (`setupBinder`, `getBinderVersion`)
- `com.android.vmcore.ui.VMSurfaceView` & `a5.*` — SurfaceView rendering guest display frames via JNI
- `com.android.vmcore.utils.OsExt` & `b5.*` — Guest filesystem path translation (`/data`, `/storage/emulated/0`)
- `com.android.vmcore.vpn.*` (`VpnConfig`, `VpnManager`, `VpnTunService`) — Virtual TUN/SOCKS5 network stack
- `q4.*` — Core `VMManager` (`C4290WWWWWWWW`), `VMInstance` (`C4293WWoWWo`), `VMConfig` (`WWoWWo`), `RomConfig`
- `r4.*`, `s4.*`, `y4.*`, `z4.*` — `VMEventManager` (`/dev/event`), filesystem & build.prop patch tasks (`FixFsTask`, `BuildExecPathTask`, `Bug3FixTask`, `Bug6FixTask`, `Bug7FixTask`), plugin installers (`magisk.zip`, `play.zip`, `superuser.zip`, `xposed.zip`)

### 3.2 Module 2: `:app` (`com.android.vmapp.*` & obfuscated `f3`..`p4`)
- `com.android.vmapp.VMApp` & `f3.*` — Application singleton, FreeReflection hidden-API unsealing (`me.weishu.reflection.BootstrapClass`), `PrivacyProtector`, `GoogleAgeSignalsHelper`, `VMExtension` registry
- `com.android.vmapp.billing.*` & `u3.*` — Google Play Billing v8.0.0 repository, VIP membership state, backend purchase verification
- `com.android.vmapp.data.*` & `h3.*` — `AppsRepository`, `BackupRepository`, `VMRepository`
- `com.android.vmapp.points.*` & `l3.*` — Room database (`PointsDatabase`), Proto DataStore (`my_coins.pb`), `GoodsDataSource`, `MissionDataSource`, `GoodsItemModel`, `MissionModel`
- `com.android.vmapp.rom.*` & `n3.*` — `RomModel`, `Asset`, `RomFirebaseDataSource`, Play Asset Delivery integration
- `com.android.vmapp.ui.*` & `p3..m4` — All Activities, Fragments, ViewModels, Custom Views, Preference screens, and RecyclerView adapters
- `com.android.vmapp.vm.*` & `n4..o4` — `VMCoreService`, `VMDisplayActivity`, `VMStartActivity0..3`, `VMExtension`, `VMHealthMonitor`, notification mirroring
- `com.android.vmapp.window.*` & `p4.*` — Floating window (`EasyFloat`), `MiniBallView`, `VMMiniView`, `VMFullView`, `VMDashboardView`, `VMMenuView`, `VMTitleBarView`

### 3.3 Module 3: `:phantom-disabler` (`com.phantom.killer.disabler.*`)
- Self-contained Kotlin feature module for disabling Android 12+ Phantom Process Killer via Shizuku or Wireless Debugging (ADB pairing over mDNS/TLS).

### 3.4 Module 4: `:auth-ui` (`com.android.auth.*`, `com.android.ui.auth.*`, `j2`..`d3`)
- Embedded authentication & FirebaseUI-style sign-in library (`AuthUI`, `KickoffActivity`, Email/Phone/Google/Facebook IdP handlers).

---

## 4. Third-Party Dependencies Identified (from `META-INF/*.version` & `*.properties`)

| Category | Maven Coordinate | Observed Version |
| :--- | :--- | :--- |
| Kotlin | `org.jetbrains.kotlin:kotlin-stdlib` | `2.2.0` (or `2.1.0`) |
| Coroutines | `org.jetbrains.kotlinx:kotlinx-coroutines-android` / `core` / `play-services` | `1.9.0` |
| Serialization | `org.jetbrains.kotlinx:kotlinx-serialization-json` | `1.7.3` |
| AndroidX Core | `androidx.core:core-ktx`, `androidx.core:core-splashscreen` | `1.15.0`, `1.0.1` |
| AndroidX UI | `androidx.appcompat:appcompat`, `com.google.android.material:material`, `androidx.constraintlayout:constraintlayout` | `1.7.0`, `1.12.0`, `2.2.0` |
| Lifecycle & Nav | `androidx.lifecycle:lifecycle-viewmodel-ktx` / `runtime-ktx` / `livedata-ktx` / `process` / `service`, `androidx.navigation:navigation-fragment-ktx` / `ui-ktx` | `2.8.7`, `2.8.5` |
| Data & Storage | `androidx.room:room-runtime` / `room-ktx`, `androidx.datastore:datastore-preferences` / `datastore`, `androidx.preference:preference-ktx` | `2.6.1`, `1.1.1`, `1.2.1` |
| Work & Startup | `androidx.work:work-runtime-ktx`, `androidx.startup:startup-runtime` | `2.9.1`, `1.2.0` |
| Credentials & Biometric | `androidx.credentials:credentials` / `credentials-play-services-auth`, `androidx.biometric:biometric` | `1.3.0`, `1.2.0-alpha05` |
| Play Services & Billing | `com.android.billingclient:billing-ktx` (`8.0.0`), `com.google.android.gms:play-services-ads` (`23.6.0`), `play-services-auth` (`21.3.0`), `play-services-location` (`21.3.0`), `play-services-oss-licenses` (`17.1.0`), `com.google.android.ump:user-messaging-platform` (`3.1.0`), `com.google.android.play:asset-delivery-ktx` (`2.2.2`), `app-update-ktx` (`2.1.0`), `review-ktx` (`2.0.2`) | Official Google Maven |
| Firebase | `firebase-analytics`, `firebase-crashlytics-ndk`, `firebase-config` (Remote Config) | Firebase BoM `33.7.0` |
| UI & Utility Libraries | `com.airbnb.android:lottie` (`6.6.0`), `com.github.bumptech.glide:glide` (`4.16.0`), `io.coil-kt:coil` (`2.7.0`), `com.blankj:utilcodex` (`1.31.1`), `org.greenrobot:eventbus` (`3.3.1`), `com.github.princekin-f:EasyFloat` (`2.0.4`), `dev.rikka.shizuku:api` & `provider` (`13.1.5`), `org.apache.commons:commons-compress` (`1.26.2`), `org.tukaani:xz` (`1.9`), `com.squareup.okhttp3:okhttp` (`4.12.0`), `com.google.code.gson:gson` (`2.11.0`) | Maven Central / JitPack |

---

## 5. Resource & Native Asset Mapping
- **Decoded Resources (`res/`):** 2,024 XML and drawable files across `anim` (53), `animator` (44), `color` (269), `drawable` (943), `interpolator` (18), `layout` (297), `menu` (7), `mipmap` (22), `navigation` (3), `raw` (9), `values` (114), and `xml` (23).
- **Resource ID Resolution (`res/values/public.xml`):** Maps all compiled `0x7f......` integer constants back to symbolic `@layout/*`, `@id/*`, `@string/*`, `@drawable/*`, `@style/*`, and `@xml/*` names. Note: Resource file names shortened by resource shrinking (e.g., `res/-n.png` vs `res/-N.png` on case-insensitive Windows NTFS) are normalized using their symbolic names in `public.xml` so that AAPT2 compiles cleanly on Windows.
- **Assets (`assets/`):**
  - `assets/carrier_list.pb` (`61,118` bytes)
  - `assets/PublicSuffixDatabase.list` (`132,737` bytes)
  - `assets/plugins/magisk.zip`, `play.zip`, `superuser.zip`, `xposed.zip`, `EMPTY`
- **JNI Libraries (`jniLibs/`):**
  - `arm64-v8a`: `libvm.so`, `libun7z.so`, `libkr32.so`, `libkr32.11.so`, `libkr32.12.so`, `libkr64.so`, `libkr64.11.so`, `libkr64.12.so`, `libkrloader32.so`, `libkrloader64.so`
  - `armeabi-v7a`: `libvm.so`, `libun7z.so`, `libkr32.so`, `libkr32.11.so`, `libkr32.12.so`, `libkrloader32.so`
