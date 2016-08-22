
def run_collatz(x):
    count = 1
    while x != 1:
        count += 1
        if x % 2 == 0:
            x = x/2
        else:
            x = 3*x + 1
    return count

if __name__ == "__main__":
    maxi = None
    maxlen = -1
    print(run_collatz(13))
    assert(run_collatz(13) == 10)
    for i in range(1, 1*1000*1000):
        if i % 1000 == 0:
            print "longest so far is", maxi, "with chain length", maxlen
        tmp = run_collatz(i)
        if tmp > maxlen:
            maxlen = tmp
            maxi = i
    print "result is", i, "with chain length", maxlen
