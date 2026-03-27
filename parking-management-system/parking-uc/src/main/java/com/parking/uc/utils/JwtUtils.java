package com.parking.uc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret:parking-system-secret-key}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        long now = System.currentTimeMillis();
        long exp = now + expiration;
        
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"userId\":" + userId + ",\"username\":\"" + username + "\",\"iat\":" + (now/1000) + ",\"exp\":" + (exp/1000) + "}";
        
        String content = base64UrlEncode(header) + "." + base64UrlEncode(payload);
        String signature = sign(content, secret);
        return content + "." + signature;
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String signature = sign(parts[0] + "." + parts[1], secret);
            return signature.equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int userIdIndex = payload.indexOf("\"userId\":");
            if (userIdIndex == -1) return null;
            String userIdStr = payload.substring(userIdIndex + 8);
            int end = userIdStr.indexOf(",");
            if (end == -1) end = userIdStr.indexOf("}");
            return Long.parseLong(userIdStr.substring(0, end).trim());
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int usernameIndex = payload.indexOf("\"username\":\"");
            if (usernameIndex == -1) return null;
            String usernameStr = payload.substring(usernameIndex + 12);
            int end = usernameStr.indexOf("\"");
            return usernameStr.substring(0, end);
        } catch (Exception e) {
            return null;
        }
    }

    private String base64UrlEncode(String text) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String data, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);
            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(new String(hash));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
