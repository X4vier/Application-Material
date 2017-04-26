## THIS IS UNFINISHED


singleDigitToString = {
    0: 0,
    1: "one",
    2: "two",
    3: "three",
    4: "four",
    5: "five",
    6: "six",
    7: "seven",
    8: "eight",
    9: "nine"
}

doubleDigitToString = {
    10: "ten",
    11: "eleven",
    12: "twelve",
    13: "thirteen",
    15: "fifteen",
    18: "eighteen",
    20: "twenty",
    30: "thirty",
    40: "forty",
    50: "fifty",
    60: "sixty",
    70: "seventy",
    80: "eighty",
    90: "ninety"
}


def numberToString(n):
    if n < 10:
        return singleDigitToString[n]
    elif 10 <= n < 14 or n == 15 or n == 18:
        return doubleDigitToString[n]
    elif 14 <= n < 20:
        return singleDigitToString[n % 10] + "teen"
    elif(20 <= n < 100):
        return doubleDigitToString[(n // 10) * 10] + \
               singleDigitToString[n % 10]
    elif 100 <= n < 1000:
        return numberToString(n // 100) + "hundred" + " " "and" + numberToString(n % 100)
    elif n == 1000:
        return "one" + " " + "thousand"
    else:
        raise ValueError("Number must be in range 1:1000 in order to count the letters")

sum = 0
for n in range(1,1001):
    print(numberToString(n))

print(numberToString(19))

# singleDigitToString = {
#     0: 0,
#     1: len("one"),
#     2: len("two"),
#     3: len("three"),
#     4: len("four"),
#     5: len("five"),
#     6: len("six"),
#     7: len("seven"),
#     8: len("eight"),
#     9: len("nine")
# }
#
# doubleDigitToString = {
#     10: len("ten"),
#     11: len("eleven"),
#     12: len("twelve"),
#     13: len("thirteen"),
#     15: len("fifteen"),
#     18: len("eighteen"),
#     20: len("twenty"),
#     30: len("thirty"),
#     40: len("forty"),
#     50: len("fifty"),
#     60: len("sixty"),
#     70: len("seventy"),
#     80: len("eighty"),
#     90: len("ninety")
# }
#
#
# def letterCount(n):
#     if n < 10:
#         return singleDigitToString[n]
#     elif 10 <= n < 14 or n == 15 or n == 18:
#         return doubleDigitToString[n]
#     elif 14 <= n < 20:
#         return singleDigitToString[n % 10] + len("teen")
#     elif(20 <= n < 100):
#         return doubleDigitToString[(n // 10) * 10] + \
#                singleDigitToString[n % 10]
#     elif 100 <= n < 1000:
#         return letterCount(n//100) + len("hundred") + len("and") + letterCount(n%100)
#     elif n == 1000:
#         return len("one") + len("thousand")
#     else:
#         raise ValueError("Number must be in range 1:1000 in order to count the letters")
