n = 2000000

sum = 5
i = 3
while i < n-1:
        i += 2
        prime = True
        for j in range(2, int(i**0.5)+1):
            if i%j == 0:
                prime = False
                break
        if prime:
            sum += i

print(sum)
