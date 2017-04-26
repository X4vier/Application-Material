"""
This question asks: find the difference between the sum of the squares of the first
one hundred natural numbers and the square of the sum.
"""

def triangle(n):
    """Returns the sum of the first n natural numbers"""
    return int(n*(n+1)/2)

def sumOfSquares(n):
    """Returns the sum of the squares n natural numbers"""
    return int(n*(n+1)*(2*n+1)/6)

print(triangle(100)**2 - sumOfSquares(100))