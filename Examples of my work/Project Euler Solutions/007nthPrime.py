"""
This question asks: what is the 10 001st prime number?
My implementation takes a few seconds to run...
"""

n = 10001

primes = set()
primes.add(2)
primes.add(3)
largestPrime = 3
while len(primes) < n:
    i = largestPrime + 2
    while not i in primes: # Check if i is prime, if it isn't add two and check again
        isPrime = True
        for p in primes:
            if i%p == 0:
                isPrime = False
                break
        if isPrime:
            primes.add(i)
            largestPrime = i
        else:
            i += 2

print(largestPrime)