
x1 = 1
x0 = 0
index = 1
while True:
    tmp = x1
    x1 += x0
    x0 = tmp
    index += 1
    if len(str(x1)) >= 1000:
        print "result is", x1, "with index", index
        break
