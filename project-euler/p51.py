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

l, s = generate_primes(1000000)

def replace_digits(x, locations, r):
    x = list(str(x))
    r = str(r)
    for i in range(len(locations)):
        if locations[i]:
            x[i] = r
    return int("".join(x))

assert replace_digits(34, [False,True], 7) == 37
assert replace_digits(3456, [False, True, False, True], 8) == 3858

def bitfield(n):
    return [True if digit=='1' else False for digit in bin(n)[2:]]

def generate_locations(p):
    r = []
    length = len(str(p))
    i = 0
    while True:
        i += 1
        b = bitfield(i)
        if len(b) > length:
            break
        r.append(([False]*(length - len(b))) + b)
    return r

print generate_locations(10)
solutions = Set()
for p in s:
    locations = generate_locations(p)
    for location in locations:
        c = 0
        primes = Set()
        for i in range(0,10):
            x = replace_digits(p, location, i)
            if l[x] and (not p in solutions) and len(str(x)) == len(str(p)):
                c += 1
                primes.add(replace_digits(p, location, i))
        if c >= 8:
            print "found solution", p, "with location", location, "and primes", primes
            solutions.add(p)
