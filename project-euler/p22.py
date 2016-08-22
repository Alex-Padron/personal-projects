import csv
import string
string.lowercase.index('b')
f = open("p022_names.txt")
r = csv.reader(f)
l = list(r)[0]

def score(word):
    s = 0
    for char in word:
        s += string.lowercase.index(char.lower()) + 1
    return s

print "score is", score("COLIN")
assert score("COLIN") == 53

l.sort()
count = 0
for i in range(len(l)):
    count += (i+1) * score(l[i])
    if l[i] == "COLIN":
        assert (i+1)*score(l[i]) == 49714
print "result is", count
