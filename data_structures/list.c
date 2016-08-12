#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

/*
TODO: add a replace_key function
 */

typedef struct node {
  char *key;
  char *value;
  struct node *next;
} node_t;

inline node_t *node (char *key, char *value) {
  node_t *n = malloc( sizeof( node_t ));
  n->key = key;
  n->value = value;
  n->next = NULL;
}

node_t *append (node_t *list, char *key, char *value) {
  if (list == NULL) return node( key, value );
  node_t *target = list;
  while (target->next != NULL) {
    target = target->next;
  }
  target->next = node( key, value );
  return list;
}

int list_length (node_t *list) {
  if (list == NULL) return 0;
  int count = 0;
  while (list != NULL) {
    count += 1;
    list = list->next;
  }
  return count;
}

node_t *remove_last (node_t *list) {
  if (list == NULL) assert(0);
  if (list->next == NULL) {
    free( list );
    return NULL;
  }
  node_t *second_to_last_node = list;
  while (second_to_last_node->next->next != NULL) {
    second_to_last_node = second_to_last_node->next;
  }
  free( second_to_last_node->next );
  second_to_last_node->next = NULL;
  return list;
}

node_t *push (node_t *list, char *key, char *value) {
  node_t *new_head = node( key, value );
  new_head->next = list;
  return new_head;
}

node_t *pop (node_t *list) {
  node_t *new_head = list->next;
  free( list );
  return new_head;
}	      

node_t *find_key (node_t *list, char *key) {
  while (list != NULL) {
    if (list->key == key) {
      return list;
    }
    list = list->next;
  }
  return NULL;
}

node_t *remove_key (node_t *list, char *key) {
  if (list == NULL) assert(0);
  if (list->key == key) {
    node_t *new_head = list->next;
    free( list );
    return new_head;
  }
  node_t *to_remove_parent = list;
  while (to_remove_parent->next != NULL && to_remove_parent->next->key != key) {
    to_remove_parent = to_remove_parent->next;
  }
  if (to_remove_parent == NULL) assert(0);
  node_t *tmp = to_remove_parent->next->next;
  free( to_remove_parent->next );
  to_remove_parent->next = tmp;
  return list;
}

int contains (node_t *list, char *key) {
  while (list != NULL) {
    if (list->key == key) return 1;
    list = list->next;
  }
  return 0;
}

void list_unit_test () {
  node_t *head = NULL;

  printf( "testing append... \n" );
  assert( head == NULL );
  head = append( head, "a", "b" );
  assert( head->key == "a" );
  assert( head->value == "b" );
  head = append( head, "c", "d" );
  assert( head->next->key == "c" );
  assert( head->next->value == "d" );
  free( head->next );
  head->next = NULL;
  free( head );
  head = NULL;
  printf( "...passed \n" );

  printf( "testing remove_last... \n" );
  assert( head == NULL );
  head = append( head, "a", "b" );
  head = remove_last( head );
  assert( head == NULL );
  head = append( head, "a", "b" );
  head = append( head, "c", "d" );
  head = remove_last( head );
  assert( head->next ==  NULL );
  assert( head->key == "a" );
  assert( head->value == "b" );
  head = append( head, "c", "d" );
  head = append( head, "e", "f" );
  head = remove_last( head );
  assert( head->next->next == NULL );
  assert( head->next->key == "c" );
  assert( head->next->value == "d" );
  free( head->next );
  head->next = NULL;
  free( head );
  head = NULL;
  printf( "...passed \n" );

  printf( "testing push... \n" );
  assert( head == NULL );
  head = push( head, "a", "b" );
  assert( head->key == "a" );
  assert( head->value == "b" );
  assert( head->next == NULL );
  head = push( head, "c", "d" );
  assert( head->key == "c" );
  assert( head->value == "d" );
  assert( head->next->key == "a" );
  head = append( head, "e", "f" );
  assert( head->key == "c" );
  assert( head->value == "d" );
  assert( head->next->key == "a" );
  assert( head->next->next->key == "e" );
  assert( head->next->next->value == "f" );
  free( head->next->next );
  head->next->next = NULL;
  free( head->next );
  head->next = NULL;
  free( head );
  head = NULL;
  printf( "...passed \n" );

  printf( "testing pop... \n" );
  assert( head == NULL );
  head = append( head, "a", "b" );
  head = pop( head );
  assert( head == NULL );
  head = append( head, "a", "b" );
  head = push( head, "c", "d" );
  head = pop( head );
  assert( head->key == "a" );
  assert( head->value == "b" );
  assert( head->next == NULL );
  free( head );
  head = NULL;
  printf( "...passed \n" );

  printf( "testing remove_key... \n" );
  head = append( head, "a", "b" );
  head = remove_key( head, "a" );
  assert( head == NULL );
  head = append( head, "a", "b" );
  head = append( head, "c", "d" );
  head = remove_key( head, "c" );
  assert( head->next == NULL );
  assert( head->key == "a" );
  head = append( head, "c", "d" );
  head = append( head, "e", "f" );
  head = remove_key( head, "c" );
  assert( head->next->key == "e" );
  free( head->next );
  head->next = NULL;
  free( head );
  head = NULL;
  printf( "...passed \n" );
  
  printf( "testing contains... \n" );
  assert( head == NULL );
  head = append( head, "a", "b" );
  assert( contains( head, "a" ) );
  head = append( head, "c", "d" );
  assert( contains( head, "c" ));
  assert( !contains( head, "e" ));
  printf( "...passed \n" );
  
  printf( "testing find_key... \n" );
  assert( find_key( head, "c" )->value == "d" );
  assert( find_key( head, "e" ) == NULL );
  printf( "...passed \n" );
  
  printf( "testing length... \n" );
  assert( list_length( head ) == 2 );
  printf( "...passed \n" );
}
