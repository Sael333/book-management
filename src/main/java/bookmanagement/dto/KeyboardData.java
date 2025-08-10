package bookmanagement.dto;

import lombok.Data;

@Data  // Lombok generará automáticamente los getters, setters, toString, equals, hashCode
public class KeyboardData {
    private String keyboardPwd;  // Al ser un valor de texto, lo representamos con String
    private int keyboardPwdId;  // Al ser un número entero, usamos int
}