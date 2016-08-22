import sys
import math

if __name__ == "__main__":
    n = int(sys.argv[1])
    l = [1] * int(sys.argv[2])
    for i in range(1, len(l)):
        j = 2*i
        while j < len(l):
            l[j] += 1
            j += i

    print "generated l"
    t = 0
    i = 0
    maxd = 0
    while t+i+1 < len(l):
        i += 1
        t += i 
        x = l[t]
        if x > maxd:
            print "highest num divisors found yet is", x, "from value", t
            maxd = x
        if x >= n:
            print "result is:", t
            break
"""  
    for i in range(1, len(l) - 1):
        nf = (l[i] - 1) * (l[i+1] - 1) - 1
        v = (i * (i + 1)) / 2
        print "v =", v, "nf =", nf, "l[i]", l[i], "l[i+1]", l[i+1]
        if nf > maxf:
            print "found value", v, "with", nf, "factors"
            maxf = nf
        if nf >= n:
            print "RESULT: value", v, "has", nf, "factors"
            break
        
"""
