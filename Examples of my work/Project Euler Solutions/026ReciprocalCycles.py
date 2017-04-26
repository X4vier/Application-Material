def lengthOfReciprocal(n):
    remainders = {}
    digits = {}
    counter = 0
    r = 1

    while True:
        exp = 0

        while r*10**exp//n == 0 and r != 0:
            #print("r is %d" % r)
            #print("exp is %d" % exp)
            #print("r*10**exp//n is %d ", r*10**exp//n)
            exp += 1
            digits[counter] = 0
            counter += 1
            #print("Counter = %d" % counter)

        r = r*10**exp%n
        #print("r = %d" % r)
        #print(r in remainders.keys())
        if r in remainders.keys():
            return counter - remainders[r]
        digits[counter] = 10**exp//n
        remainders[r] = counter
        #print(remainders.keys())


max_length = 0
answer = 0
for i in range(1,1000):
    if lengthOfReciprocal(i) >= max_length:
        max_length = lengthOfReciprocal(i)
        answer = i

print(answer)
print(lengthOfReciprocal(answer))