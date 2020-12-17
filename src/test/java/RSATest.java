import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class RSATest {
    private RSA rsa =new RSA();

    @Test
    void millerRabinT() {
        assertTrue(rsa.MillerRabinT(new BigInteger("17"), 1000));
        assertTrue(rsa.MillerRabinT(new BigInteger("1021"), 1000));
        assertTrue(rsa.MillerRabinT(new BigInteger("9973"), 1000));

        assertFalse(rsa.MillerRabinT(new BigInteger("20"), 1000));
        assertFalse(rsa.MillerRabinT(new BigInteger("1020"), 1000));
        assertFalse(rsa.MillerRabinT(new BigInteger("9977"), 1000));
    }

    @Test
    void modInverse() {
        BigInteger expected = rsa.modInverse(new BigInteger("9977"), new BigInteger("123"));
        System.out.println(expected);
        BigInteger actual = new BigInteger("9977").modInverse(new BigInteger("123"));
        System.out.println(actual);

        assertEquals(expected, actual);
    }

    @Test
    void encDec () {
        BigInteger[] keys = rsa.keygen(1024);
        BigInteger N = keys[0], e = keys[1], d = keys[2];
        System.out.printf("public:\nN = %s \ne = %s\n", N.toString(16), e.toString(16));
        System.out.printf("secret:\nN = %s \nd = %s\n", N.toString(16), d.toString(16));

        String message = "Hello world!";
        System.out.printf("Message: %s\n", message);

        BigInteger encrypted = rsa.encrypt(new BigInteger(message.getBytes()), N, e);
        System.out.printf("Encrypted: %s\n", encrypted.toString(16));

        BigInteger decrypted = rsa.decrypt(encrypted, N, d);
        System.out.printf("Decrypted: %s\n", new String(decrypted.toByteArray()));
    }
}