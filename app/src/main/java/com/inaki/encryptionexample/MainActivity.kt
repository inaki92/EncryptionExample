package com.inaki.encryptionexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.inaki.encryptionexample.databinding.ActivityMainBinding
import com.inaki.encryptionexample.security.DecryptionProcess
import com.inaki.encryptionexample.security.EncryptionProcess
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val encryptor: EncryptionProcess = EncryptionProcess()


    private val decryptor: DecryptionProcess by lazy {
        DecryptionProcess()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.decryptBtn.setOnClickListener { decryptText() }

        binding.encryptBtn.setOnClickListener { encryptText() }
    }

    private fun decryptText() {
        val decryptedText = encryptor.encryption?.let {
            encryptor.iv?.let { iv ->
                decryptor.decryptData(ALIAS_KEY, it, iv)
            }
        } ?: "No data to decrypt"

        binding.myText.text = decryptedText
    }

    private fun encryptText() {
        val valueEncrypted = encryptor.encryptText(binding.txtToEncrypt.text.toString(), ALIAS_KEY)

        valueEncrypted?.let {
            binding.myText.text = Base64.encodeToString(valueEncrypted, Base64.DEFAULT)
        }
    }

    companion object {
        private const val ALIAS_KEY = "ANDROID_KEY"
    }
}