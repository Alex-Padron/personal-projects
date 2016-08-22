from sets import Set
from decimal import *
getcontext().prec = 10000

def get_cycle_length(x):
    s = str(Decimal(1)/Decimal(x))[2:]
    maxlen = 0
    for i in range(1, len(s)/2):
        start = s[:i]
        all_valid = True
        for j in range(len(start)):
            if not (i + j < len(s) and start[j] == s[i + j]):
                all_valid = False
        if all_valid:
            maxlen = i
            break
                
    return maxlen

print(get_cycle_length(3))
print(get_cycle_length(7))
maxl = 0
maxi = 0
for i in range(1, 1000):
    if get_cycle_length(i) > maxl:
        print "found new max:", i, "with cycle length", get_cycle_length(i)
        maxl = get_cycle_length(i)
        maxi = i
print "result is", maxi, "max cycle length is", maxl

