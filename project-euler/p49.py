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
def put_or_one(d, c):
    if c in d:
        d[c] = d[c] + 1
    else:
        d[c] = 1

def is_permutation(n1, n2):
    n1 = str(n1)
    n2 = str(n2)
    d1 = {}
    d2 = {}
    for c in n1:
        put_or_one(d1, c)
    for c in n2:
        put_or_one(d2, c)
    for k in d1:
        if (not k in d2) or d1[k] != d2[k]:
            return False
    return True

assert is_permutation(12333, 32133)
assert not is_permutation(342, 546)

for i in range(1000, 10000):
    if i % 100 == 0:
        print "i =", i
    for j in range(i+1, 10000):
        c = 2*j - i
        if is_permutation(i,j) and is_permutation(j,c) and l[i] and l[j] and l[c] and i < 10000 and j < 10000 and c < 10000:
            print "found solution", i, j, c
