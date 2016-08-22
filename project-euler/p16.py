
def sum_of_digits(x):
    l = [int(i) for i in str(x)]
    s = 0
    for i in l:
        s += i
    return s

if __name__ == "__main__":
    print sum_of_digits(2**1000)
