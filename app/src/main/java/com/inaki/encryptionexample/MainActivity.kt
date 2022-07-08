package com.inaki.encryptionexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.inaki.encryptionexample.databinding.ActivityMainBinding
import com.inaki.encryptionexample.encryption.EncryptionProcess

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val encryption by lazy {
        EncryptionProcess()
    }

    private val masterKey by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }

    private val sharedPrefs by lazy {
        EncryptedSharedPreferences.create(
            "my_shared_prefs",
            masterKey,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.decryptBtn.setOnClickListener { decryptText() }

        binding.encryptBtn.setOnClickListener { encryptText() }
    }

    private fun decryptText() =
        encryption.encryptedData?.let {
            encryption.decryptData(ALIAS_KEY)
        } ?: "No data to decrypt".also {
            binding.myText.text = it
        }

    private fun encryptText() =
        encryption.encryptData(binding.txtToEncrypt.text.toString(), ALIAS_KEY).also { valueEncrypted ->
            binding.myText.text = Base64.encodeToString(valueEncrypted, Base64.DEFAULT)
        }

    companion object {
        private const val ALIAS_KEY = "ANDROID_KEY_1"
    }
}