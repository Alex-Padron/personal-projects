from sets import Set

def is_pandigital(a, b):
    c = a*b
    tup = (a, b, c)
    seen = Set()
    for num in tup:
        l = [int(i) for i in str(num)]
        for digit in l:
            if digit in seen:
                return False
            seen.add(digit)
    return len(seen) == 9 and (not 0 in seen)

assert not is_pandigital(1,2)
assert is_pandigital(39, 186)

pandigital = Set()
for i in range(10000):
    if i % 1000 == 0:
        print "i =", i
    for j in range(100):
        if is_pandigital(i,j):
            print "found solution: i =", i, ", j =", j, ", i*j =", i*j
            pandigital.add(i*j)

s = 0
for num in pandigital:
    s += num
print "result is", s
