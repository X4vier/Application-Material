"""
This question asks: what is the 10 001st prime number?
"""

n = 10001

primes = set()
primes.add(2)
primes.add(3)
largestPrime = 2
while len(primes) < n:
    i = largestPrime
    repeat = True
    while repeat:
        i += 2
        prime = True
        for p in primes:
            if i%p == 0:
                prime = False
                break
        if prime:
            primes.add(i)
            largestPrime = i
            repeat = False

print(largestPrime)