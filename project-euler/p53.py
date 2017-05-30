import math

def ncr(n, r):
    return math.factorial(n)/(math.factorial(r)*math.factorial(n-r))

assert(ncr(5, 3) == 10);
count = 0
for n in range(1,101):
    for r in range(1,n):
        if ncr(n, r) > 1000000:
            count += 1
            print "n = ", n, "r = ", r
print count
