#include <stdlib.h>
#include <stdio.h>

// ~10.3 sec for in place quicksort unoptimized, 500000 elements
// about same for coarsening, a bit faster
// with coarsening ~0.112 seconds for 50000 elements

void print_array(int* A, int size);

void inline swap(int* A, int i, int j) {
  int tmp = A[i];
  A[i] = A[j];
  A[j] = tmp;
}

inline int partition(int* A, int i, int j) {
  int pivot = A[j];
  int h = i;
  while (i < j) {
    if (A[i] < pivot) {
      if (i > h) {
	swap(A, i, h);
      }
      ++h;
    }
    ++i;
  }
  swap(A, h, j);
  return h;
}


void insertion_sort(int* A, int i, int j) {
  for (int s = i; s <= j; s++) {
    int t = s;
    while (t > i && A[t] < A[t-1]) {
      swap(A, t, t - 1);
      t -= 1;
    }
  }
}

void quicksort(int* A, int i, int j) {
  if (i >= j - 10) {
    insertion_sort(A, i, j);
    return;
  }
  int h = partition(A, i, j);
  quicksort(A, i, h - 1);
  quicksort(A, h + 1, j);
}


void print_array(int* A, int size) {
  for (int i = 0; i < size; i++) {
    printf("%d ", A[i]);
  }
  printf("\n");
}

int main() {
  int SIZE = 250000;
  int* a = new int[SIZE];
  for (int i = 0; i < SIZE; i++) {
    a[i] = (7*i*i + 6*i + 34*i*i*i) % 15;
  }
  // print_array(a, SIZE);
  printf("started sorting\n");
  quicksort(a, 0, SIZE - 1);
  //insertion_sort(a, 0, SIZE - 1);
  printf("finished sorting\n");
  // print_array(a, SIZE);
  return 0;
}
