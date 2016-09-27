#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>


void rotate_and_print(int n, int* arr, int k, int q, int* m) {
  k = k % n;
  for (int i = 0; i < q; i++) {
    int index = (m[i] + k) % n;
    printf("%d\n", arr[index]);
  }
}

int main() {
  int n = 5;
  int arr[5] = {1,2,3,4,5};
  int k = 3; //num rotations
  int q = 5;
  int m[5] = {0,1,2,3,4};
  rotate_and_print(n, arr, k, q, m);
  return 0;
}
