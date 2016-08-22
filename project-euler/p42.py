import csv
import string

f = open("p042_words.txt")
r = csv.reader(f)
words = list(r)[0]

def score(word):
    s = 0
    for char in word:
        s += string.lowercase.index(char.lower()) + 1
    return s

assert score("SKY") == 55

rng = 1*1000*1000
l = [False] * rng

for i in range(len(l)):
    x = (i*(i+1))/2
    if x < len(l):
        l[x] = True

print "generated l"
assert l[score("SKY")]

c = 0
for word in words:
    if l[score(word)]:
        c += 1
print "result is", c
