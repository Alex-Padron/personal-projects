/*
Other hashtbl ideas:
-never rewrite old arrays, just allocate
new ones and store pointers in smaller hash table
 */
#include "hash.c"
#include "list.c"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>


typedef struct hashtbl {
  int size;
  struct node **table;
} hashtbl_t;;

hashtbl_t empty (int size) {
}

void unit_test () {
}
int main () {
  unit_test ();
  return 0;
}
