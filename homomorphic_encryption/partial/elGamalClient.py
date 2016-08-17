from Crypto.PublicKey import ElGamal
from client import EncryptedRequest


"""
Implementation of the EncryptedRequest class that uses El Gamal homomorphic encryption.
"""
class ElGamalEncryption(EncryptedRequest):

	def makeURLQuery(self, ctxts):
		return "/elgamal/?" + "ar=" + str(ctxts[0]) + "&am=" + str(ctxts[1]) + "&br=" + str(ctxts[2]) + "&bm=" + str(ctxts[3])

	def initialize(self, keyLen):
		self.KeyLen = keyLen
		self.KeyObj = ElGamal.generate(self.KeyLen, self.getSecureRand())


	def encrypt(self, message):
		K = self.getSecureRand()(self.KeyLen)
		return self.KeyObj.encrypt(message,K)

	def decrypt(self, ctxt):
		(r,m) = ctxt.split(" ")
		return self.KeyObj.decrypt((long(r),long(m)))







EG = ElGamalEncryption()
print ('Welcome to the super secure multiplier thing')
print("")

#query the user for key size and initialize EG based on that 
size = input('Enter the number of bits you want to use as a key: ')
EG.initialize(size)

print("")
print("Finished generating a key")
print("")

#get the ip addr and query values from the user
ipAddr = input('Enter the IP to send requests to: ')
print("")
a = input('Type the first number you want multiplied: ')
b = input('Type the second number you want multiplied: ')
print("")

#generate the two encryptions
c1 = EG.encrypt(a)
c2 = EG.encrypt(b)

#form the cipher text array from the encryption outputs
ctxts = [None] * 4
ctxts[0] = c1[0]
ctxts[1] = c1[1]
ctxts[2] = c2[0]
ctxts[3] = c2[1]

#send the http request
result = EG.sendHTTPRequest(ipAddr, ctxts)

#decrypt and print the decryption result
print("")
print("The decrypted result is: " + str(EG.decrypt(result)))
print("")
