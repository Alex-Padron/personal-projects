import sys

def range_divisible(x, y):
    for i in range(1, y+1):
        if x % i != 0:
            return False
    return True

if __name__ == "__main__":
    n = int(sys.argv[1])
    i = 2520
    while not range_divisible(i,n):
        i += 2520
    print "Result is:", i
