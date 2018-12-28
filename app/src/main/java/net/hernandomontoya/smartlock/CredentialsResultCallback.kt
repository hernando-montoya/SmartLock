package net.hernandomontoya.smartlock

import com.google.android.gms.auth.api.credentials.CredentialRequestResult
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResultCallback


class CredentialsResultCallback(private val mainActivity: MainActivity) : ResultCallback<CredentialRequestResult> {

    override fun onResult(requestResult: CredentialRequestResult) {

        if (requestResult.status.statusCode == CommonStatusCodes.SIGN_IN_REQUIRED) {
            mainActivity.showDialog()
        }

    }


}