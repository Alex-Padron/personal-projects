
def gen_distinct_prime_factors(size):
    l = [0] * size
    p = 2
    while p < size:
        while l[p] != 0:
            p += 1
            if p >= size:
                return l
        l[p] = 1
        j = 2*p
        while j < size:
            l[j] = l[j] + 1
            j += p
        p += 1
    return l

l = gen_distinct_prime_factors(1*1000*1000)
assert l[14] == 2 and l[15] == 2
assert l[644] == 3 and l[645] == 3 and l[646] == 3

for i in range(len(l)):
    j = i
    found = False
    while l[j] == 4:
        if j - i >= 3:
            found = True
            break
        j += 1
    if found:
        print "found solution, i =", i
        break
