package br.unitins.topicos1.service;

import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HashServiceImpl implements HashService {

    private final String salt = "#b_l-ah+xyz-22%";
    private final Integer iterationCount = 400;
    private final Integer keyLength = 512;

    @Override
    public String getHashSenha(String senha) {

        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
            .generateSecret(new PBEKeySpec(senha.toCharArray(), salt.getBytes(), iterationCount, keyLength))
            .getEncoded();

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar um hash");
        }
    }
}
