"""
This question asks: What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?
"""

def build_set_of_primes(n):
    """Returns a set containing all the primes less than or equal to n"""
    primes = set()
    for i in range(2,n+1):
        prime = True
        for p in primes:
            if i%p == 0:
                prime = False
                break
        if prime: primes.add(i)

    return primes

n = 20
answer = 1

"""
Strategy: find all the primes up to n, then for each prime ask what is the highest power
that prime can be raised to while still being less than n. The smallest number which is
evenly divisible by all numbers less than or equal to n will be the product of each prime
raised to it's corresponding highest power.
"""

primes = build_set_of_primes(n)
print("The primes less than {} are: {}".format(n, primes))
print()
for p in primes:
    power = 1
    while p**(power+1) < n:
        power += 1
    print("The highest power %d can take is %d" % (p, power) )
    answer *= p**power

print()
print("The answer is %d" % answer)