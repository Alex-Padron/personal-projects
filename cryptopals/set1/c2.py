
#xor two hex strings
def xor(s1, s2):
    #convert two strings to unicode
    b1 = s1.decode("hex")
    b2 = s2.decode("hex")
    #xor the bytes and return the encoded value
    return "".join(chr(ord(x) ^ ord(y)) for x, y in zip(b1, b2)).encode("hex")

if __name__ == "__main__":
     r = xor("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965").decode("hex")
     print(r)
