# SOFTPOS-ANDROID-ENTEGRASYON
SOFTPOS ANDROID ENTEGRASYON

# Implementation

```kotlin
implementation(“com.provisionpay:android-deeplink-sdk:1.0.22")
```
# Manifest Ayarları

İşlem yapılan activity için manifest dosya düzeni aşağıdaki gibi olmalıdır. 
"Activity" tag içerisine 'launchMode=singleTask' eklenmelidir.
Deeplink konfigürasyonu için "data" kısmı eklenmelidir.

```kotlin
        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:screenOrientation="portrait"
            android:launchMode="singleTask"
            android:windowSoftInputMode="adjustPan" >

            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

            <intent-filter android:autoVerify="true">
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data  
                    android:scheme="https"
                    android:host="yourDomain.com"
                    android:pathPrefix="/receipt" />
            </intent-filter>
        </activity>
```