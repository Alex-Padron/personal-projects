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
    return l


l = generate_primes(100*1000*1000)
print "generated l"

def is_pandigital(l, n):
    seen = Set()
    for digit in l:
        if digit in seen:
            return False
        seen.add(digit)
    for digit in seen:
        if int(digit) > n: 
            return False
    return len(seen) == n and (not "0" in seen)

assert not is_pandigital("456", 3)
assert not is_pandigital("123", 9)
assert is_pandigital("123", 3)
assert is_pandigital("123456789", 9)
assert not is_pandigital("123456789", 7)

maxv = 0
for n in range(1, 10):
    print "checking n =", n
    for i in range((10 ** (n-1)), (10 ** n) - 1):
        if l[i] and is_pandigital(str(i),n):
            if i > maxv:
                print "found largest pandigital prime", i, "with n =", n
                maxv = i
