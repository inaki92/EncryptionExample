package com.inaki.encryptionexample.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.*
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object SecuredObjects {

    private const val TRANSFORMATION = "DES/CBC/PKCS5Padding"
    private const val KEY_STORE = "AndroidKeyStore"

    /**
     * This is the variable for cipher to be retrieved every time
     */
    val cipher: Cipher by lazy {
        Cipher.getInstance(TRANSFORMATION)
    }

    val iv: ByteArray by lazy {
        cipher.iv
    }

    val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEY_STORE).apply {
            load(null)
        }
    }

    fun generateKey(aliasKey: String): SecretKey {
        val gen = KeyGenerator.getInstance(KEY_ALGORITHM_AES, KEY_STORE).apply {
            init(getAlgorithmParams(aliasKey))
        }

        return gen.generateKey()
    }


    fun getSecretKey(aliasKey: String): SecretKey =
        (keyStore.getEntry(aliasKey, null) as KeyStore.SecretKeyEntry).secretKey

    private fun getAlgorithmParams(aliasKey: String) =
        KeyGenParameterSpec.Builder(
            aliasKey,
            PURPOSE_ENCRYPT.or(PURPOSE_DECRYPT)
        )
            .setBlockModes(BLOCK_MODE_GCM)
            .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
            .build()
}