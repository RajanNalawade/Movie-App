package com.example.filmyapp.utility

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

object KeystoreHelper {
    private const val KEY_SIZE = 128
    private const val PROVIDER = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    private val NULL = null
    private const val EMPTY = ""
    private const val NEW_LINE = "\n"

    fun encrypt(encryptionSecret: EncryptionSecret): DecryptionSecret {
        val kg =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)

        val kps = KeyGenParameterSpec.Builder(
            encryptionSecret.alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()

        kg.init(kps)
        val sk = kg.generateKey()

        val c = Cipher.getInstance(TRANSFORMATION)
        c.init(Cipher.ENCRYPT_MODE, sk)

        val eb = c.doFinal(encryptionSecret.data.toByteArray())
        val ctB64 = Base64.encodeToString(eb, Base64.NO_PADDING)
        val ivB64 = Base64.encodeToString(c.iv, Base64.NO_PADDING)

        return DecryptionSecret(
            data = ctB64.replace(NEW_LINE, EMPTY),
            iv = ivB64.replace(NEW_LINE, EMPTY),
            alias = encryptionSecret.alias
        )
    }

    fun decrypt(decryptionSecret: DecryptionSecret): EncryptionSecret {

        val iv = Base64.decode(decryptionSecret.iv, Base64.NO_PADDING)
        val ct = Base64.decode(decryptionSecret.data, Base64.NO_PADDING)

        val ks = KeyStore.getInstance(PROVIDER)
        ks.load(NULL)

        val ske: KeyStore.SecretKeyEntry =
            ks.getEntry(decryptionSecret.alias, NULL) as KeyStore.SecretKeyEntry

        val c = Cipher.getInstance(TRANSFORMATION)
        val gps: AlgorithmParameterSpec = GCMParameterSpec(KEY_SIZE, iv)
        c.init(Cipher.DECRYPT_MODE, ske.secretKey, gps)

        val decryptedBytes = String(c.doFinal(ct))

        return EncryptionSecret(
            alias = decryptionSecret.alias,
            data = decryptedBytes
        )
    }
}