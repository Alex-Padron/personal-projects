from c2 import xor
from c1 import hex_to_base64
import string

letter_frequencies = {"e": 12.7, "t": 9.1, "a": 8.2, "o": 7.5, "i": 7.0, "n": 6.7, "s": 6.3, "h": 6.1, 
                      "r": 6.0,  "d": 4.3, "l": 4.0, "u": 2.8, "c": 2.8, "m": 2.4, "w": 2.4, "f": 2.2, 
                      "y": 2.0,  "g": 2.0, "p": 1.9, "b": 1.5, "v": 1.0, "k": 0.8, "x": 0.2, "j": 0.2,
                      "q": 0.1,  "z": 0.1}

def frequency_delta(counts, total):
    delta = 0
    for c in counts:
        delta += abs((100.0 * counts[c])/total - letter_frequencies[c])
    return delta

def score(sentence):
    l = map(lambda x: x.lower(), filter(lambda x: x.isalpha(), list(sentence)))
    counts = {}
    for c in letter_frequencies:
        counts[c] = 0
    for c in l:
        counts[c] = counts[c] + 1    
    return frequency_delta(counts, len(l))
    
def single_char_xor(string, char):
    s2 = char*len(string)
    print xor(string, s2)
    print hex_to_base64(xor(string, s2))
    return xor(string, s2).decode("hex")

if __name__ == "__main__":
    print(score("Hello World"))
    print(score("dasdsagfshghgbdfhdjfjgjhgfh"))
    print(score("The other day I was walking down the street"))
    
    s = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
    print(hex_to_base64(s))
    
    print("looping through chars")
    for i in range(128):
        c = chr(i)
        try:
            single_char_xor(s, c)
        except:
            pass

