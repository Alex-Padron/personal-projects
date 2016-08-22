from sets import Set
import itertools

def l_to_num(l):
    s = 0
    m = 1
    i = len(l) - 1
    while i >= 0:
        s += m*int(l[i])
        m *= 10
        i -= 1
    return s

first_primes = [2,3,5,7,11,13,17,19,23,29]
def check_property(p):
    p = str(p)
    i = 1
    while i + 2 < len(p):
        n = l_to_num([p[i], p[i+1], p[i+2]])
        if n % first_primes[i - 1] != 0 :
            return False
        i += 1
    return True

assert check_property(1406357289)
assert check_property(113)
assert not check_property(3423235)

s = 0
for n in range(9, 10):
    l = [i for i in range(n+1)]
    print "checking permutations of", l
    l = map(lambda x: list(x), list(itertools.permutations(l)))
    for p in l:
        p = l_to_num(p)
        if p > 10 ** 9 and check_property(p):
            print "found valid solution", p
            s += p
print "result is", s
