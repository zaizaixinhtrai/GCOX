package com.data

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by thanhbc on 2/28/18.
 */
class AESUtils(password: String, ivParam: String) {

    private val secretKey: SecretKeySpec?
    private val ivParameterSpecs: IvParameterSpec?

    init {
        secretKey = SecretKeySpec(password.toByteArray(Charsets.UTF_8), "AES")
        ivParameterSpecs = IvParameterSpec(ivParam.toByteArray(Charsets.UTF_8))
    }

    fun encrypt(strToEncrypt: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpecs)

            return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)

        } catch (e: Exception) {

            println("Error while encrypting: " + e.toString())
        }

        return null
    }

    fun decrypt(strToDecrypt: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpecs)
            return String(cipher.doFinal(Base64.decode(strToDecrypt.toByteArray(Charsets.UTF_8), Base64.DEFAULT)))

        } catch (e: Exception) {

            println("Error while decrypting: " + e.toString())
        }

        return null
    }
}