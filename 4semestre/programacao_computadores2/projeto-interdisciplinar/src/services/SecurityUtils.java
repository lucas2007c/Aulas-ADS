package services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String hashSenha(String senhaPura) {
        if (senhaPura == null || senhaPura.isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(senhaPura.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de criptografia SHA-256 não disponível no ambiente Java.", e);
        }
    }

    public static boolean verificarSenha(String senhaPura, String hashArmazenado) {
        if (senhaPura == null || hashArmazenado == null) {
            return false;
        }
        String novoHash = hashSenha(senhaPura);
        return novoHash.equalsIgnoreCase(hashArmazenado);
    }
}
