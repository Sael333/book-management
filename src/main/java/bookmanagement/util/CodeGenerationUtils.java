package bookmanagement.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class CodeGenerationUtils {

    public int generateBookingCodeId() {
        Random rand = new Random();
        return rand.nextInt(900000) + 100000;
    }

    public int generatePasscode() {
        Random rand = new Random();
        return rand.nextInt(9000) + 1000;
    }
}
