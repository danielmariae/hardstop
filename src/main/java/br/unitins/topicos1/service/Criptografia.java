package br.unitins.topicos1.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Criptografia {
    private static final String CHAVE_AES_BASE64 = "u1vxBb27Cr7XJXx1sg1X5SRiL1791UosvqEwbcbgu3Q="; // Chave de 32 bytes para AES

    public static String criptografar(String texto) throws Exception {
        byte[] chaveBytes = Base64.getDecoder().decode(CHAVE_AES_BASE64);
        SecretKeySpec chaveSecreta = new SecretKeySpec(chaveBytes, "AES");

        Cipher cifra = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cifra.init(Cipher.ENCRYPT_MODE, chaveSecreta);

        byte[] textoBytes = texto.getBytes(StandardCharsets.UTF_8);
        byte[] textoCifrado = cifra.doFinal(textoBytes);
        return Base64.getEncoder().encodeToString(textoCifrado);
    }

    public static String descriptografar(String textoCifrado) throws Exception {
        byte[] chaveBytes = Base64.getDecoder().decode(CHAVE_AES_BASE64);
        SecretKeySpec chaveSecreta = new SecretKeySpec(chaveBytes, "AES");

        Cipher cifra = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cifra.init(Cipher.DECRYPT_MODE, chaveSecreta);

        byte[] textoBytes = Base64.getDecoder().decode(textoCifrado);
        byte[] textoDecifrado = cifra.doFinal(textoBytes);

        return new String(textoDecifrado, StandardCharsets.UTF_8);
    }
}
