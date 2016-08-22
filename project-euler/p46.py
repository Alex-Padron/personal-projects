import math
from sets import Set

def generate_primes(length):
    l = [True] * length
    l[0] = False
    l[1] = False
    p = 2
    while p < (length/2) + 1:
        while not l[p]:
            p += 1
        j = 2*p
        while j < length:
            l[j] = False
            j += p
        p += 1
    s = Set()
    for i in range(len(l)):
        if l[i]:
            s.add(i)
    return l, s


l, s = generate_primes(100*1000)
s = list(s)
s.sort()
print "generated l"
c = [False] * (1*1000*1000)
count = 0
for p in s:
    count += 1
    if count % 1000 == 0:
        print "prime is", p
    for i in range(1000):
        ind = p + (2*i*i)
        if ind < len(c):
            c[ind] = True
print "generated c with length", len(c), "5777 is", c[5777]
for i in range(len(c)):
    if not c[i] and i % 2 == 1:
        print "found solution", i
        if i > 10:
            break
assert False

def is_prime_and_two_squares(x):
    i = 1
    while i < math.sqrt(x) + 2:
        if l[x - 2*(i ** 2)]:
            return True
    return False

for i in range(1*1000*1000):
    if (not l[i]) and is_prime_and_two_squares(i):
        print "found solution:", i
        break
    
