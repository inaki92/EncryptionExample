package com.inaki.encryptionexample.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class EncryptionProcess {

    /**
     * This variable is to have access to the initialization vector from cipher
     */
    var iv: ByteArray? = null

    /**
     * This variable will contain our text already encrypted
     */
    var encryption: ByteArray? = null

    /**
     * This is the variable for cipher to be retrieved every time
     */
    private val cipher: Cipher by lazy {
        Cipher.getInstance(TRANSFORMATION)
    }

    /**
     * This method will perform the encryption of your key
     */
    fun encryptText(textToEncrypt: String, aliasKey: String): ByteArray? {
        cipher.init(Cipher.ENCRYPT_MODE, keyGenerator(aliasKey))

        iv = cipher.iv

        encryption = cipher.doFinal(textToEncrypt.toByteArray())

        return encryption
    }

    /**
     * This method will generate a secret key for the encryption of your text
     * and it needs an alias key to create your keyStore
     */
    private fun keyGenerator(aliasKey: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, MY_KEY_GEN).apply {
            init(
                KeyGenParameterSpec.Builder(
                    aliasKey,
                    KeyProperties.PURPOSE_ENCRYPT.or(KeyProperties.PURPOSE_DECRYPT)
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        }

        return keyGenerator.generateKey()
    }

    companion object {
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val MY_KEY_GEN = "AndroidKeyStore"
    }
}