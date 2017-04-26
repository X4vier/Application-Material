"""
    This question asks for the sum of all even fibonacci numbers less than
    four million. To make the implementation as fast and memory-efficient
    as possible, only two fibonacci numbers are ever stored in memory at
    once and we add them up as we generate them.
    """

i = 1
j = 1

sum = 0

while True:
        i = i+j # Next fibonacci number
        if i > 4000000:
            break
        if i%2 == 0: # Adding up all the even fibonacci numbers
            sum += i

        j = i - j # j becomes the fibonacci number which comes before i
print(sum)