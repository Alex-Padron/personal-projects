# Homomorphic Encryption

This folder contains some work my partner and I did for MIT class 6.857, Computer and Network Security. Our topic was homomorphic encryption, we started by exploring some partially homomorphic encryption schemes. 

### Partially Homomorphic Encryption

We implemented ElGamal and Pailler partially homomorphic encryption, in the [partial/] folder. Since the mathematics of the server code is multiplication for each of these schemes, we use the same server code for each scheme. 

### Fully Homomorphic Encryption

We explored the fully homomorphic encryption scheme in https://crypto.stanford.edu/craig/craig-thesis.pdf. We did not implement this, rather we have demo code using a preexisting implementation in the [fhe/] folder. The library required is https://github.com/shaih/HElib.

### Multiparty Homomorphic Encryption

Our main work was to implement multiparty homomorphic encryption, a system we came up with for providing FHE using multiple servers. The paper and basic implementation can be found in the folder [multiparty_encryption/]. The implementation requires the Sympy library.