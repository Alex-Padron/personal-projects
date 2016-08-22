import sys

if __name__ == "__main__":
    n = int(sys.argv[1])
    s = 0
    for i in range(n):
        if i % 3 == 0 or i % 5 == 0:
            s += i
    print "Sum is:", s
