#include "hash.c"
#include "list.c"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <float.h>

/*
TODO: not finished with first iteration
 */

typedef struct hashtbl {
  int size;
  node_t **table;
} hashtbl_t;;

hashtbl_t *create_hashtbl (int size) {
  hashtbl_t *hashtbl = malloc( sizeof( hashtbl_t ));
  hashtbl->table = malloc( sizeof( node_t * ) * size );
  int i;
  for (i = 0; i < size; i++) {
    hashtbl->table[i] = NULL;
  }
  hashtbl->size = size;
  return hashtbl;
}

void free_hashtbl (hashtbl_t *hashtbl) {
  free( hashtbl->table );
  free( hashtbl );
}

void insert (hashtbl_t *hashtbl, char *key, char *value) {
  uint32_t index = lookup3( key, sizeof(key), 0 ) % hashtbl->size;
  if (hashtbl->table[index] == NULL) {
    hashtbl->table[index] = node( key, value );
    return;
  }
  node_t * new_value;
  if (contains( hashtbl->table[index], key )) {
    node_t * tmp = remove_key( hashtbl->table[index], key );
    new_value = push( tmp, key, value );
  } else {
    new_value = push( hashtbl->table[index], key, value);
  }
  hashtbl->table[index] = new_value;
}

char *get (hashtbl_t *hashtbl, char *key) {
  uint32_t index = lookup3( key, sizeof(key), 0 ) % hashtbl->size;
  node_t *n = find_key( hashtbl->table[index], key );
  if (n == NULL) return NULL;
  return n->value;
}

void print_bucket_sizes (hashtbl_t *hashtbl) {
  printf( "Bucket Sizes:\n" );
  int i;
  for ( i = 0; i < hashtbl->size; i++ ) {
    printf( "%d: %d\n", i, list_length( hashtbl->table[i] ));
  }
}

float largest_delta (hashtbl_t *hashtbl) {
  float min_size = FLT_MAX;
  float max_size = -1;
  int i;
  for ( i = 0; i < hashtbl->size; i++ ) {
    int l = list_length ( hashtbl->table[i] );
    printf( "length is %d\n", l );
    float len = (float) list_length ( hashtbl->table[i] );
    if (len < min_size) {
      min_size = len;
    } else if (len > max_size) {
      max_size = len;
    }
  }
  printf( "max size = %f, min size = %f", max_size, min_size);
  return max_size/min_size;
}

void hashtbl_unit_test () {
  hashtbl_t *ht;

  printf( "testing basic insert and get... \n" );
  ht = create_hashtbl( 4 );
  insert( ht, "1", "b" );
  print_bucket_sizes( ht );
  insert( ht, "2", "b" );
  print_bucket_sizes( ht );
  insert( ht, "3", "b" );
  print_bucket_sizes( ht );
  insert( ht, "4", "b" );
  print_bucket_sizes( ht );
  insert( ht, "5", "b" );
  print_bucket_sizes( ht );
  insert( ht, "6", "b" );
  print_bucket_sizes( ht );
  insert( ht, "7", "b" );
  print_bucket_sizes( ht );
  /*
  int i;
  for ( i = 0; i < 1000; i++ ) {
    char val[6];
    sprintf( val, "%d", i );
    insert ( ht, val, val );
    assert ( get( ht, val ) == val );
  }
  assert( largest_delta( ht ) < 2 );
  printf( "largest delta is %f\n", largest_delta( ht ));
  */
  free_hashtbl( ht );
  printf( "...passed \n" );
}

int main () {
  list_unit_test ();
  hash_unit_test ();
  hashtbl_unit_test ();
  return 0;
}
