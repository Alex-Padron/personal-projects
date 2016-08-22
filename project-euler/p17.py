import inflect
from num2words import num2words

p = inflect.engine

def count_letters(x):
    s = num2words(x)
    count = 0
    for i in range(len(s)):
        if s[i].isalpha():
            count += 1
    return count

if __name__ == "__main__":
    print(count_letters(342))
    print(count_letters(115))
    c = 0
    for i in range(1, 1001):
        c += count_letters(i)
    print ("result is", c)
