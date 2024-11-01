# SOFTPOS-ANDROID-ENTEGRASYON
SOFTPOS ANDROID ENTEGRASYON

# Implementation

```groovy
implementation(“com.provisionpay:android-deeplink-sdk:1.0.22")
```
# Manifest Ayarları

İşlem yapılan activity için manifest dosya düzeni aşağıdaki gibi olmalıdır. 
"Activity" tag içerisine 'launchMode=singleTask' eklenmelidir.
Deeplink konfigürasyonu için "data" kısmı eklenmelidir.

```xml
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

# Activity Düzenlemeleri

İşlem yapılan activity içerisinde “onNewIntent” metodu override edilmeli ve 
aşağıdaki gibi deeplink aktivasyon fonksiyonu çağırılmalı.

```kotlin
override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        SoftposDeeplinkSdk.handleDeeplinkTransaction()
}
```
# Softpos Initialize ve Callback Kontrolü


```kotlin
        SoftposDeeplinkSdk.initialize(
            InitializeConfig(
                privateKey = "privateKey",
                activity = activity,
                deeplinkUrl = "softPosUrl"
            )
        )
        SoftposDeeplinkSdk.setDebugMode(true)

        SoftposDeeplinkSdk.subscribe(object : SoftposDeeplinkSdkListener {
            override fun onCancel() {
                Log.d("SOFTPOS", "onCancel)")

            }

            override fun onError(e: Throwable) {
                Log.d("SOFTPOS", "onError")

            }

            override fun onIntentData(dataFlow: IntentDataFlow, data: String?) {
                Log.d("SOFTPOS", "onIntentData")

            }

            override fun onOfflineDecline(paymentFailedResult: PaymentFailedResult?) {
                Log.d("SOFTPOS", "onOfflineDecline")

            }

            override fun onPaymentDone(transaction: Transaction, isApproved: Boolean) {
                Log.d("SOFTPOS", "onPaymentDone")

            }

            override fun onSoftposError(errorType: SoftposErrorType, description: String?) {
                Log.d("SOFTPOS", "onSoftposError")

            }

            override fun onTimeOut() {
                Log.d("SOFTPOS", "onTimeOut")

            }
        })

        SoftposDeeplinkSdk.registerBroadcastReceiver("com.provisionpay.softpos.esnekpos",
            object : BroadcastReceiverListener {
                override fun onSoftposBroadcastReceived(
                    eventType: Int,
                    eventTypeMessage: String,
                    paymentSessionToken: String
                ) {
                    Log.d("SOFTPOS", "onSoftposBroadcastReceived")
                }
            }
        )

        override fun onPause() {
                super.onPause()
                SoftposDeeplinkSdk.unregisterBroadcastReceiver()
        }
```
# Softpos Ödeme Başlatma

```kotlin
        SoftposDeeplinkSdk.startPayment(
                "paymentSessionId",
                "softPosUrl"
        )
```

# Esnekpos Ödeme Başlatma ve Bitirme Metodları

Bu dokümantasyon, `https://posservice.esnekpos.com/api/mobile/` base URL'ini kullanan iki adet API çağrısını içermektedir.

---

## 1. SoftPosStartPayment

### Endpoint
`POST https://posservice.esnekpos.com/api/mobile/SoftPosStartPayment`

### Request Body

| Parameter     | Type   | Description        |
|---------------|--------|--------------------|
| `dealerId`    | String | Bayi ID           |
| `userId`      | String | Kullanıcı ID      |
| `amount`      | String | Ödeme Tutarı      |
| `installment` | Int    | Taksit Sayısı     |
| `mobileToken` | String | Mobil Token       |
| `merchant`    | String | Mağaza ID         |
| `merchantKey` | String | Mağaza Anahtarı   |

#### JSON Format

```json
{
  "dealerId": "string",
  "userId": "string",
  "amount": "string",
  "installment": 0,
  "mobileToken": "string",
  "merchant": "string",
  "merchantKey": "string"
}
```
### Response Model

| Parameter     | Type   | Description        |
|---------------|--------|--------------------|
| `status`    | String | İşlem Durumu           |
| `message`      | String | Durum Mesajı      |
| `paymentSessionId`      | String | İşlem ID |

```json
{
  "status": boolean,
  "message": "string",
  "paymentSessionId": "string"
}
```



















