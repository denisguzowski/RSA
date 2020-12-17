import java.math.BigInteger;
import java.util.Random;

public class RSA {
    public boolean MillerRabinT (BigInteger n, int k) {
        //if n == 2 or n == 3
        if (n.equals(BigInteger.TWO) || n.equals(new BigInteger("3")))
            return true;

        //if n < 2 or n even
        if (n.compareTo(BigInteger.TWO) < 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO))
            return false;

        //n-1 in form (2^s)·t, where t is odd. Division of n-1 by 2
        BigInteger t = n.subtract(BigInteger.ONE);
        int s = 0;
        while (t.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            t = t.divide(BigInteger.TWO);
            s += 1;
        }

        for (int i = 0; i < k; i++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), new Random());
            }
            while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) >= 0); //[2, n − 2]

            // x ← a^t mod n
            BigInteger x = a.modPow(t, n);

            //if x == 1 or x == n − 1
            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
                continue;

            for (int r = 1; r < s; r++) {
                // x ← x^2 mod n
                x = x.modPow(BigInteger.TWO, n);

                //if x == 1 return composite
                if (x.equals(BigInteger.ONE))
                    return false;

                if (x.equals(n.subtract(BigInteger.ONE)))
                    break;
            }

            if (!x.equals(n.subtract(BigInteger.ONE)))
                return false;
        }

        //probable prime
        return true;
    }

    //adapted Extended Euclidean algorithm
    public BigInteger modInverse (BigInteger a, BigInteger n) {
        BigInteger t = BigInteger.ZERO;
        BigInteger newt = BigInteger.ONE;
        BigInteger r = n;
        BigInteger newr = a;

        while (!newr.equals(BigInteger.ZERO)) {
            BigInteger quotient = r.divide(newr);

            BigInteger auxval = newt;
            newt = t.subtract(quotient.multiply(newt)); //t − quotient × newt
            t = auxval;

            auxval = newr;
            newr = r.subtract(quotient.multiply(newr)); //r − quotient × newr
            r = auxval;
        }

        if (r.compareTo(BigInteger.ONE) > 0) throw new IllegalArgumentException("a is not invertible");

        if (t.compareTo(BigInteger.ZERO) < 0) {
            t = t.add(n);
        }

        return t;
    }

    public BigInteger[] keygen (int bitLength) {
        Random random =  new Random();
        BigInteger p = new BigInteger(bitLength, 1000, random);
        assert MillerRabinT(p, 1000);

        BigInteger q = new BigInteger(bitLength, 1000, random);
        assert MillerRabinT(q, 1000);

        assert !p.equals(q);

        BigInteger N = p.multiply(q);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = new BigInteger(bitLength, 1000, random);
        }
        while (e.compareTo(BigInteger.ONE) < 0 || e.compareTo(phi) >= 0); //1<e<phi

        BigInteger d = modInverse(e, phi);
        assert d.equals(e.modInverse(phi));

        // pk - (N,e)
        // sk - (N,d)
        BigInteger[] keys = new BigInteger[]{N, e, d};
        return keys;
    }

    public BigInteger encrypt (BigInteger m, BigInteger N, BigInteger e) {
        BigInteger c = m.modPow(e, N);
        return c;
    }

    public BigInteger decrypt (BigInteger c, BigInteger N, BigInteger d) {
        BigInteger m = c.modPow(d, N);
        return m;
    }
}
