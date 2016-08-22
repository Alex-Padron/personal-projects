
if __name__ == "__main__":
    s = 0
    s2 = 0
    for i in range(1,101):
        s += i
        s2 += i*i
    s = s*s
    print "result is:", s - s2
