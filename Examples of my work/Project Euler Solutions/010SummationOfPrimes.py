"""
This question asks for the sum of all primes below two million. My implementation takes around
fifteen seconds on my laptop so please be patient :( An alternative implementation would be to
sum the primes up as we generate them and not keep the list in memory. This is slightly slower
but would save on space.
"""

n = 2000000

import time
start_time = time.time()
def build_list_of_primes(n):
    """Returns a list containing all the primes less than or equal to n"""
    primes = []
    for i in range(2,n+1):
        prime = True
        for p in primes:
            if i%p == 0:
                prime = False
                break
            if p >= i**0.5: # No need to look for prime factors beyond the square root
                break
        if prime:
            primes.append(i)

    return primes

print(sum(build_list_of_primes(n)))
print("--- %s seconds ---" % (time.time() - start_time))
