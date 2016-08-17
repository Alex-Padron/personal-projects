import binascii

def hex_to_base64(x):
    hex = x.decode("hex")
    return binascii.b2a_base64(hex)

if __name__ == "__main__":
    print hex_to_base64("49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
    
