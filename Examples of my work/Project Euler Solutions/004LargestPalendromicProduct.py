"""
This question asks for the largest palindrome number made from the product of two 3-digit numbers.
(A palindrome number is a number which reads the same backwards and forwards, like 9009)
"""


def isPalendrome(x):
    """Returns true if x is a palindrome"""
    digits = str(x)
    if len(digits) <= 1:  return True
    if digits[0] == digits[-1]:
        return isPalendrome(digits[1:-1])
    else:
        return False

largestPalindrome = 0

for i in range(101,1000): # Check all the digit numbers
    for j in range(101,1000):
        if isPalendrome(i*j) and i*j > largestPalindrome:
            largestPalindrome = i*j


print(largestPalindrome)
