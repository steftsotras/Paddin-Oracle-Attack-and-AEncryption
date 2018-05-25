/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package padding_attack;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;



class AuthenticatedEncryption {
    
    private final byte[] k1;
    private final byte[] k2;
    
    public AuthenticatedEncryption(String encryption_key, String auth_key) throws Exception {
        
        //DECODE TA KLEIDIA SE BYTE
        this.k1 = Base64.getDecoder().decode(encryption_key);
        this.k2 = Base64.getDecoder().decode((auth_key));
    }


    public String encrypt(String plaintext) throws Exception {
        
        byte[] in = plaintext.getBytes("UTF-8");
        
        //PARAGWGH TUXAIWN BYTE
        //INITIALIZATION VECTOR (IV)
        byte[] iv = new byte[16];
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        randomSecureRandom.nextBytes(iv);
        
        //KRYPTOGRAFHSH TOU MYNHMATOS ME TO PRWTO KLEIDI
        //AES ENCRYPTION
        SecretKeySpec key = new SecretKeySpec(k1, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        AlgorithmParameterSpec param = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, param);
        byte[] ciphertext = cipher.doFinal(in);
        
        //ENWSH TOU KRYPTOGRAFHMENOU TEXT ME TA TYXAIA BYTES
        byte[] ivcipher = concat_byteArrays(iv, ciphertext);
        
        //PARAGWGH TOU MAC ME TO DEYTERO KLEIDI
        SecretKeySpec secret_key = new SecretKeySpec(k2, "HmacSHA256");
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] mac_data = sha256_HMAC.doFinal(ivcipher);
        
        //ENWSH TOU MAC ME TO KRYPTOGRAFHMENO KEIMENO KAI IV
        //METATROPH SE STRING KAI EPISTROFH
        return Base64.getEncoder().encodeToString(concat_byteArrays(ivcipher, mac_data));
    }

    //ENWSH DYO PINAKWN BYTE
    private byte[] concat_byteArrays(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}