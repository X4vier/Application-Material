"""
This question asks to find the product a*b*c, where (a, b, c) is the
the Pythagorean triplet for which a + b + c = 1000.
 Pythagorean triplet means a*a + b*b == c*c)
"""
def findTriplet(n):
    """Finds a pythagorean triplet which sums to n, returns None if none exist"""
    for x in range(1,n//2):
        leftover = n - x
        for y in range(1, leftover//2):
            if x + y + (x*x + y*y)**0.5 == n:
                    return x, y, int((x*x + y*y)**0.5)
    return None

print(findTriplet(1000))