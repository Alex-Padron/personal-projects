import sys

if __name__ == "__main__":
    n = int(sys.argv[1])
    l = [True] * n
    p = 2
    while p < len(l):
        j = 2*p
        while j < len(l):
            l[j] = False
            j += p
        p += 1
        if p >= len(l):
            break
        while not l[p]:
            p += 1
            if p >= len(l):
                break
    print "calculated l"
    #print "l is", l

    i = 2
    s = 0
    while i < len(l):
        if l[i]:
            s += i
        i += 1
    print "result is:", s
