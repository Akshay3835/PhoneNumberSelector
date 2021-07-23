package com.example.phoneselectorapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.i18n.phonenumbers.PhoneNumberUtil


class MainActivity : AppCompatActivity() {

    private lateinit var phoneNumber: EditText
    private lateinit var countries: EditText
    private lateinit var imageView: ImageView
    private var phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        phoneNumber = findViewById(R.id.phoneNumber)
        countries = findViewById(R.id.countries)
        imageView = findViewById(R.id.imageView)

        phoneNumber.setOnClickListener {
            phoneSelection()

            Toast.makeText(this, "Number has Selected ", Toast.LENGTH_LONG).show()
        }
    }

    private fun phoneSelection() {

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog().build()

        val credentialsClient = Credentials.getClient(applicationContext, options)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        resultCode.launch(IntentSenderRequest.Builder(intent).build())
    }

    private val resultCode =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val credential: Credential? = result.data?.getParcelableExtra(Credential.EXTRA_KEY)
                credential?.apply {
                    val numberWithCountryCode = phoneUtil.parse(credential.id, "")
                    phoneNumber.setText(numberWithCountryCode.nationalNumber.toString())
                    countries.setText(numberWithCountryCode.countryCode.toString())

                }
            }

        }

}


