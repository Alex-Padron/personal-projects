
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


l = generate_primes(10*1000*1000)
print "generated l"

maxn = 0
maxa = 0
maxb = 0
for a in range(-999, 1000):
    for b in range(-999, 1001):
        n = 0
        while l[(n*n) + a*n + b]:
            n += 1
        if n > maxn:
            maxn = n
            maxa = a
            maxb = b
            print "found optimal values n =", n, ", a =", a, ", b =", b, ", a*b =", a*b
            
