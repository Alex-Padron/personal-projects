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
    s = list(s)
    s.sort()
    return l, s

def put_or_max(d, k, v):
    if not k in d:
        d[k] = v
    else:
        d[k] = max(d[k], v)

def generate_prime_sums(length):
    l, s = generate_primes(length)
    print "generated primes"
    possible_sums = {}
    for i in range(len(s)):
        if i % 1000 == 0:
            print "i =", i
        val = 0
        k = i
        while k < len(s):
            val += s[k]
            k += 1
            if val < length:
                put_or_max(possible_sums, val, k - i + 1)
    print "generated possible sums"
    maxk = 0
    maxv = 0
    for k in possible_sums:
        if k < length and possible_sums[k] > maxv and l[k]:
            maxv = possible_sums[k]
            maxk = k
    return maxk

p = generate_prime_sums(1000*1000)
print p
assert False
l, s = generate_primes(1000*1000)
s = list(s)
s.sort()
print "generated l"


