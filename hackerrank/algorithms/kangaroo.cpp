#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h> 

void print_if_meet(int x1, int v1, int x2, int v2) {
  if (v1 - v2 == 0) {
    if (x1 == x2) {
      printf("YES");
    } else {
      printf("NO");
    }
    return;
  }
  float x = (float)(x2 - x1) / (float)(v1 - v2);
  if (((int)(x)) == x && x > 0) {
    printf("YES");
  } else {
    printf("NO");
  }
}

int main() {
  int s1 = 0;
  int j1 = 2;
  int s2 = 5;
  int j2 = 3;
  print_if_meet(s1, j1, s2, j2);
  return 0;
}
