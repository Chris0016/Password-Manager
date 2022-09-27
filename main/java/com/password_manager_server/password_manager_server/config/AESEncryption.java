package com.password_manager_server.password_manager_server.config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import lombok.SneakyThrows;

@Configuration
public class AESEncryption implements AttributeConverter<Object, String> {

    @Value("${aes.encryption.key}")
    private String encryptionKey;
    private final String encryptionCipher = "AES";
    private final String initVector = "encryptionIntVec";

    private Key key;
    private Cipher cipher;
    private IvParameterSpec iv;

    private IvParameterSpec getIv() {
        if (iv == null)
            iv = new IvParameterSpec(initVector.getBytes());
        return iv;
    }

    private Key getKey() {
        if (key == null)
            key = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);

        return key;
    }

    private Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (cipher == null)
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        return cipher;
    }

    private void initCipher(int encryptMode)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException {

        getCipher().init(encryptMode, getKey(), getIv());
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null)
            return null;
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = SerializationUtils.serialize(attribute);
        return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));
    }

    @SneakyThrows
    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        initCipher(Cipher.DECRYPT_MODE);
        byte[] bytes = getCipher().doFinal(Base64.getDecoder().decode(dbData));
        return SerializationUtils.deserialize(bytes);
    }

}
