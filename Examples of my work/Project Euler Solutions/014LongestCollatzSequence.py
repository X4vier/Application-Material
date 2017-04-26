collatzLengths = {}
collatzLengths[1] = 1
def lengthOfCollatz(n):
    if n in collatzLengths:
        return collatzLengths[n]
    elif n%2 == 0:
        rtn = 1 + lengthOfCollatz(n//2)
        collatzLengths[n] = rtn
        return rtn
    else:
        rtn = 1 + lengthOfCollatz(3*n + 1)
        collatzLengths[n] = rtn
        return rtn


startingNumber = 0
longestSequence = 0
for n in range(1, 1000000):
    if lengthOfCollatz(n) > longestSequence:
        startingNumber = n
        longestSequence = lengthOfCollatz(n)

print(startingNumber)