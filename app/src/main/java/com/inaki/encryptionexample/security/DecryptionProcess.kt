package com.inaki.encryptionexample.security

import android.database.Cursor
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class DecryptionProcess {

    /**
     * This is the variable that will hold the keyStore object
     */
    private lateinit var keyStore: KeyStore

    /**
     * This init block will run just after you instantiate the class
     */
    init {
        keyStore = KeyStore.getInstance(EncryptionProcess.MY_KEY_GEN).apply {
            load(null)
        }
    }

    /**
     * This is the variable for cipher to be retrieved every time
     */
    private val cipher: Cipher by lazy {
        Cipher.getInstance(EncryptionProcess.TRANSFORMATION)
    }

    /**
     * This method will decrypt your data using the encrypted data and the aliasKey
     */
    fun decryptData(aliasKey: String, encryptedData: ByteArray, encryptionIv: ByteArray): String {
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(aliasKey), spec)

        return String(cipher.doFinal(encryptedData), Charset.defaultCharset())
    }

    private fun getSecretKey(aliasKey: String): SecretKey {
        return (keyStore.getEntry(aliasKey, null) as KeyStore.SecretKeyEntry).secretKey
    }
}