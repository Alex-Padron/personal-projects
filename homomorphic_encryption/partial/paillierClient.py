from client import EncryptedRequest
from paillier import *

class paillierEncryption(EncryptedRequest):

	def makeURLQuery(self, ctxts):
		return "/paillier/?" + "x1=" + str(ctxts[0]) + "&x2=" + str(ctxts[1])

	def initialize(self, keySize):
		priv, pub = generate_keypair(keySize)
		self.KeyObj = (priv, pub)
		self.KeyLen = keySize

	def encrypt(self, message):
		return encrypt(self.KeyObj[1], message)

	def decrypt(self, message):
		return decrypt(self.KeyObj[0], self.KeyObj[1], int(message))



PR = paillierEncryption()

print('Welcome to the super secure adder thing')
print("")

#query the user for key size and initialize EG based on that 
size = input('Enter the number of bits you want to use as a key: ')
PR.initialize(size)

print("")
print("Finished generating a key")
print("")

#get the ip addr and query values from the user
ipAddr = input('Enter the IP to send requests to: ')
print("")
a = input('Type the first number you want added: ')
b = input('Type the second number you want added: ')
print("")

#generate the two encryptions
c1 = PR.encrypt(a)
c2 = PR.encrypt(b)

#form the cipher text array from the encryption outputs
ctxts = [None] * 2
ctxts[0] = c1
ctxts[1] = c2

#send the http request
result = PR.sendHTTPRequest(ipAddr, ctxts)

#decrypt and print the decryption result
print("")
print("The decrypted result is: " + str(PR.decrypt(result)))
print("")