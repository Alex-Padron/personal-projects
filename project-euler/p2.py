

if __name__ == "__main__":
    n0 = 0
    n1 = 1
    s = 0
    while n1 < 4*1000*1000:
        print "n0 is", n0, "n1 is", n1
        if n1 % 2 == 0:
            s += n1
        tmp = n1
        n1 += n0
        n0 = tmp
    print "Sum is:", s
