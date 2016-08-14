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
  int num_elements;
  node_t **table;
} hashtbl_t;;

hashtbl_t *create_hashtbl (int size) {
  hashtbl_t *hashtbl = malloc( sizeof( hashtbl_t ));
  hashtbl->table = malloc( sizeof( node_t * ) * size );
  int i;
  for (i = 0; i < size; i++) {
    hashtbl->table[i] = NULL;
  }
  hashtbl->num_elements = 0;
  hashtbl->size = size;
  return hashtbl;
}

void free_hashtbl (hashtbl_t *hashtbl) {
  free( hashtbl->table );
  free( hashtbl );
}

void insert( hashtbl_t *hashtbl, char *key, char *value );
//copy elements from old_table to hashtbl, and free old_table
void copy_elements_clean (hashtbl_t *hashtbl, node_t **old_table) {
  //insert all the old elements into the new table
  int i;
  for ( i = 0; i < sizeof( old_table ); i = i + sizeof( node_t * )) {
    node_t *el = old_table[i];
    node_t *tmp;
    //free elements as we add them to the new table
    while ( el != NULL ) {
      insert( hashtbl, el->key, el->value );
      tmp = el->next;
      free( el );
      el = tmp;
    }  
  }
  //free the old table
  free( old_table );
}

void resize (hashtbl_t *hashtbl) {
  if (hashtbl->size * sizeof( node_t * ) * 2 > sizeof( hashtbl->table )) { //double size 
    node_t ** old_table = hashtbl->table;
    hashtbl->size = hashtbl->size * 2;
    hashtbl->table = malloc( hashtbl->size * sizeof( node_t * ));
    copy_elements_clean( hashtbl, old_table );
  } else if (hashtbl->size * sizeof( node_t * ) * 4 < sizeof( hashtbl->table )) { //halve size
    node_t ** old_table = hashtbl->table;
    hashtbl->size = hashtbl->size / 2;
    hashtbl->table = malloc( hashtbl->size * sizeof( node_t * ));
    copy_elements_clean( hashtbl, old_table );
  }
}

void insert (hashtbl_t *hashtbl, char *key, char *value) {
  uint32_t index = lookup3( key, sizeof(key), 0 ) % hashtbl->size;
  printf( "inserting key %s in bucket %d \n", key, index );
  resize( hashtbl );
  if (hashtbl->table[index] == NULL) {
    hashtbl->num_elements += 1;
    hashtbl->table[index] = node( key, value );
    return;
  }
  node_t * new_value;
  if (contains( hashtbl->table[index], key )) {
    node_t * tmp = remove_key( hashtbl->table[index], key );
    new_value = push( tmp, key, value );
  } else {
    hashtbl->num_elements += 1;
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
  //make a bunch of puts and check that they are all there
  insert( ht, "a", "a" );
  insert( ht, "b", "b" );
  insert( ht, "c", "c" );
  insert( ht, "d", "d" );
  insert( ht, "e", "e" );
  insert( ht, "f", "f" );
  insert( ht, "g", "g" );
  insert( ht, "h", "h" );
  insert( ht, "i", "i" );
  insert( ht, "j", "j" );
  insert( ht, "k", "k" );
  insert( ht, "l", "l" );
  insert( ht, "m", "m" );
  insert( ht, "n", "n" );
  insert( ht, "o", "o" );
  insert( ht, "p", "p" );
  print_bucket_sizes( ht );
  fflush( stdout );

  assert( get( ht, "a" ) == "a" );
  assert( get( ht, "b" ) == "b" );
  assert( get( ht, "c" ) == "c" );
  assert( get( ht, "d" ) == "d" );
  assert( get( ht, "e" ) == "e" );
  assert( get( ht, "f" ) == "f" );
  assert( get( ht, "g" ) == "g" );
  assert( get( ht, "h" ) == "h" );
  assert( get( ht, "i" ) == "i" );
  assert( get( ht, "j" ) == "j" );
  assert( get( ht, "k" ) == "k" );
  assert( get( ht, "l" ) == "l" );
  assert( get( ht, "m" ) == "m" );
  assert( get( ht, "n" ) == "n" );
  assert( get( ht, "o" ) == "o" );
  assert( get( ht, "p" ) == "p" );
  
  printf( "check balanced... \n" );
  printf( "...failed - test not implemented \n" );
  
  printf( "test resizing... \n" );
  ht = create_hashtbl( 0 );
  assert( ht->size == 0 );
  printf( "...passed \n" );
  //check that the keys are roughly balanced across the hashtbl
  assert( largest_delta( ht ) < 2 );

  free_hashtbl( ht );
  printf( "...passed \n" );
}

int main () {
  list_unit_test ();
  hash_unit_test ();
  hashtbl_unit_test ();
  return 0;
}
