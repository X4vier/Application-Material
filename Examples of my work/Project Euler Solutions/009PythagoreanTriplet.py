
def findTriplet(n):
    for x in range(1,n//2):
        leftover = n - x
        for y in range(1, leftover//2):
            if x + y + (x*x + y*y)**0.5 == n:
                    return x, y, int((x*x + y*y)**0.5)
    return None

print(findTriplet(1000))