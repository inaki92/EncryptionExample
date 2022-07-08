package com.inaki.encryptionexample.encryption

import com.inaki.encryptionexample.encryption.SecuredObjects.getSecretKey
import java.nio.charset.Charset
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.spec.GCMParameterSpec

class EncryptionProcess {

    var encryptedData: ByteArray? = null

    fun encryptData(data: String, aliasKey: String): ByteArray {
        val key = SecuredObjects.generateKey(aliasKey)
        SecuredObjects.cipher.init(ENCRYPT_MODE, key)

        return SecuredObjects.cipher.doFinal(data.toByteArray()).also {
            encryptedData = it
        }
    }

    fun decryptData(aliasKey: String): String? {
        val gcmParams = GCMParameterSpec(128, SecuredObjects.iv)
        SecuredObjects.cipher.init(DECRYPT_MODE, getSecretKey(aliasKey), gcmParams)

        return encryptedData?.let {
            String(SecuredObjects.cipher.doFinal(it), Charset.defaultCharset())
        }
    }
}