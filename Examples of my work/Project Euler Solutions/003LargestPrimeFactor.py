"""This question asks for the largest prime factor of the number 600851475143"""

def largestPrimeFactor(n):
    """Returns the larges prime factor of an integer n >= 2"""
    if n == 1 or n == 0: return 0
    for i in range(2, n+1):
        if n%i == 0:
            return max(i, largestPrimeFactor(n//i))

print(largestPrimeFactor(600851475143))