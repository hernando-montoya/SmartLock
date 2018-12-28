package net.hernandomontoya.smartlock

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    companion object {
        private const val REQUEST_CODE: Int = 666
    }

    private var googleApiClient: GoogleApiClient? = null
    private var credentialsRequest: CredentialRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = GoogleApiClientListener(this)

        googleApiClient = GoogleApiClient
            .Builder(this)
            .enableAutoManage(this, listener)
            .addApi(Auth.CREDENTIALS_API)
            .build()


        credentialsRequest = CredentialRequest.Builder()
            .setPasswordLoginSupported(true)
            .build()


        button.setOnClickListener {
            val credentialsCallback = CredentialsResultCallback(this)

            Auth.CredentialsApi
                .request(googleApiClient, credentialsRequest)
                .setResultCallback(credentialsCallback)

        }
    }


    fun showDialog() {
        val hintRequest = createHintRequest()

        val pendingIntent = Auth.CredentialsApi
            .getHintPickerIntent(googleApiClient, hintRequest)

        startIntentSenderForResult(pendingIntent.intentSender, REQUEST_CODE, null, 0, 0, 0)
    }

    private fun createHintRequest(): HintRequest {
        val pickerConfig = CredentialPickerConfig.Builder()
            .setShowCancelButton(true)
            .setPrompt(CredentialPickerConfig.Prompt.SIGN_UP)
            .build()
        return HintRequest.Builder()
            .setEmailAddressIdentifierSupported(true)
            .setHintPickerConfig(pickerConfig)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val credentials = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
            editText.setText(credentials?.id)
        }
    }
}
