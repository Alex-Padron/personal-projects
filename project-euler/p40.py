
s = ""
i = 1
while len(s) < 1*1000*1000:
    s = s + str(i)
    i += 1
assert(s[12] == "1")
print "result is", int(s[0]) * int(s[9]) * int(s[99]) * int(s[999]) * int(s[9999]) * int(s[99999]) * int(s[999999])
