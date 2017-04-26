def triangle(n):
    return n*(n+1)//2

def primeFactors(n):
    factors = {}
    while n > 1:
        for i in range(2, n+1):
            if n%i == 0:
                if i in factors.keys():
                    factors[i] += 1
                else:
                    factors[i] = 1
                n = n//i
                break
    return factors

def factorial(n):
    if n == 1:
        return 1
    return n*factorial(n-1)

def binomial(n,k):
    return factorial(n)/(factorial(k)*factorial(n-k))

def numberDivisors(n):
    factors = primeFactors(n)
    totalFactors = 1
    for factor in factors:
        totalFactors *= (factors[factor] + 1)
    return totalFactors

n = 76576500

factors = primeFactors(n)

print("%d has %d divisors in total" % (n, numberDivisors(n)))
for factor in factors:
    print("{%d goes into %d %d time(s)}" % (factor, n, factors[factor]))

n = 1

while True:
    if numberDivisors(triangle(n)) >= 501:
        print(triangle(n))
        break
    n = n+1