import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KonyaginPomerance {
    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите число которое хотите проверить: ");
            long n = scanner.nextLong();
            System.out.print("Сколько вы знаете простых сомножителей " + (n - 1) + "? ");
            int m = scanner.nextInt();
            List<Long> factorization = new ArrayList<>();
            System.out.println("Введите простые множители " + (n - 1) + ": ");
            for (int i = 0; i < m; i++) {
                factorization.add(scanner.nextLong());
            }
            boolean isPrime = isPrimeKonyaginPomerance(n, factorization);
            System.out.println("Число " + n + (isPrime ? " простое" : " составное"));
        }
    }

    public static boolean isPrimeKonyaginPomerance(long n, List<Long> factorization) {
        if (n <= 1 || n % 2 == 0) return false;
        long log2n = (long) (Math.log(n) / Math.log(2));
        long sqrtN = (long) Math.sqrt(n);
        long F = 1;
        List<Long> smallPrimes = sievePrimes((int) (log2n + 1));
        for (long a : smallPrimes) {
            if (a > log2n) break;
            if (!isProbablePrime(a) || modPow(a, F, n) == 1) continue;
            long E = findOrder(a, n, factorization);
            boolean condition = true;
            for (long q : factorization) {
                if (gcd(modPow(a, E / q, n) - 1, n) != 1) {
                    condition = false;
                    break;
                }
            }
            if (!condition) return false;
            F = lcm(F, E);
            if (F >= sqrtN) return true;
        }
        return false;
    }


    private static List<Long> sievePrimes(int limit) {
        boolean[] isPrime = new boolean[limit + 1];
        List<Long> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) isPrime[i] = true;

        for (int p = 2; p * p <= limit; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= limit; i += p) isPrime[i] = false;
            }
        }
        for (int i = 2; i <= limit; i++) {
            if (isPrime[i]) primes.add((long) i);
        }
        return primes;
    }


    private static long findOrder(long a, long n, List<Long> factorization) {
        long order = n - 1;
        for (long q : factorization) {
            long tempOrder = order / q;
            if (modPow(a, tempOrder, n) == 1) {
                order = tempOrder;
            }
        }
        return order;
    }


    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }


    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            exp >>= 1;
            base = (base * base) % mod;
        }
        return result;
    }

    private static boolean isProbablePrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }


    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
