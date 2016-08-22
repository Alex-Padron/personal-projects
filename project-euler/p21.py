from sets import Set

def sum_proper_divisors(x):
    s = 0
    for i in range(1, x):
        if x % i == 0:
            s += i
    return s

assert(sum_proper_divisors(220) == 284)
assert(sum_proper_divisors(284) == 220)

s = [None]*10000
for i in range(10000):
    s[i] = sum_proper_divisors(i)

c = 0
for i in range(10000):
    for j in range(10000):
        if s[i] == j and s[j] == i and i != j:
            print "found amicable pair", i, j
            c += (i+j)
c = c/2
print "result is", c
