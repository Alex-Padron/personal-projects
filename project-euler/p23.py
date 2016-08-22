
def sum_proper_divisors(x):
    s = 0
    for i in range(1, x):
        if x % i == 0:
            s += i
    return s

rng = 28123
#generate abundant numbers
l = [False] * rng

for i in range(rng):
    l[i] = sum_proper_divisors(i) > i
print "determined l"

def has_sum(x):
    for i in range(x):
        j = x - i
        if l[i] and l[j]:
            return True
    return False

c = 0
for i in range(rng):
    if not has_sum(i):
        c += i
print "result is", c
