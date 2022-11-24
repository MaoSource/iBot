package com.source.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/14/16:48
 */
public class EncDecUtils {

    private static String RSA_ALGORITHM_MODEL = "RSA/ECB/PKCS1Padding";

    public static byte[] aesEncrypt(String paramString1, String paramString2) throws Exception {
        Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        localCipher.init(1, new SecretKeySpec(paramString2.getBytes(StandardCharsets.UTF_8), "AES"));
        return localCipher.doFinal(paramString1.getBytes(StandardCharsets.UTF_8));
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (int index = 0, len = bytes.length; index <= len - 1; index += 1) {
            String invalue1 = Integer.toHexString((bytes[index] >> 4) & 0xF);
            String intValue2 = Integer.toHexString(bytes[index] & 0xF);
            result.append(invalue1);
            result.append(intValue2);
        }
        return result.toString();
    }

    public static PublicKey getRSAPublicKey() throws Exception {
//        InputStream inputStream = Resources.class.getResourceAsStream("/crt/public_key.der");
//        byte[] keyBytes = Files.readAllBytes(Paths.get("D:\\source\\recreation\\iBot\\src\\main\\resources\\crt\\public_key.der"));
        byte[] keyBytes = Files.readAllBytes(Paths.get("public_key.der"));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static byte[] rsaEncrypt(byte[] paramArrayOfByte) throws Exception {
        Cipher localCipher = Cipher.getInstance(RSA_ALGORITHM_MODEL);
        localCipher.init(1, getRSAPublicKey());
        return localCipher.doFinal(paramArrayOfByte);
    }

}
