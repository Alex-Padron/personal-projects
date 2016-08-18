#include <stdio.h>
#include <assert.h>
#include <memory>


template <class T>
class ListNode {
public:
  T *data;
  std::shared_ptr<ListNode<T>> next;

  ListNode(T *data) {
    this->data = data;
    this->next = NULL;
  }
};
  
template <class T>
class List {
public:
  typedef std::shared_ptr<ListNode<T>> node;
  node head;
  
  List() {
    head = NULL;
  }
  
  void push(T *new_data) {
    node tmp = head;
    head = node(new ListNode<T>(new_data));
    head->next = tmp;
  }

  T *pop() {
    if (head == NULL) return NULL;
    T *ret = head->data;
    head = head->next;
    return ret;
  }
  
  int length() {
    int count = 0;
    node n = head;
    while (n != NULL) {
      count += 1;
      n = n->next;
    }
    return count;
  }

  void filter(bool (*f)(T)) {
    if (head == NULL) return;
    //filter head repeatedly
    while (f(*head->data)) {
      head = head->next;
      if (head == NULL) return;
    }
    //filter child nodes
    node n = head;
    while (n != NULL && n->next != NULL) {
      if (f(*n->next->data)) {
	n->next = n->next->next;
      }
      n = n->next;
    }
  }

  void filter_data(bool (*f)(T, T), T data) {
    if (head == NULL) return;
    //filter head repeatedly
    while (f(*head->data, data)) {
      head = head->next;
      if (head == NULL) return;
    }
    //filter child nodes
    node n = head;
    while (n != NULL && n->next != NULL) {
      if (f(*n->next->data, data)) {
	n->next = n->next->next;
      }
      n = n->next;
    }  
  }

  T *find_data(bool (*f)(T, T), T data) {
    if (head == NULL) {
      return NULL;
    }
    node n = head;
    while (n != NULL) {
      if (f(*n->data, data)) {
	return n->data; 
      }
      n = n->next;
    }
    return NULL;
  }
};

bool is_even(int x) {
  return x % 2 == 0;
}

void list_unit_test() {
  printf("testing push, pop, and length... \n");
  int x[10];
  for (int i = 0; i < 10; i++) {
    x[i] = i;
  }
  List<int> l;
  for (int i = 0; i < 10; i++) {
    l.push(&x[i]);
    assert(l.length() == i + 1);
  }
  for (int i = 0; i < 10; i++) {
    assert(*l.pop() == 9 - i);
    assert(l.length() == 9 - i);
  }
  printf("...passed \n");

  printf("testing filter... \n");
  assert(l.length() == 0);
  int y = 1;
  int z = 2;
  l.push(&y);
  l.push(&z);
  l.filter(is_even);
  assert(l.head->next == NULL);
  assert(*l.head->data == 1);
  l.pop();
  assert(l.length() == 0);
  l.push(&z);
  l.push(&y);
  l.filter(is_even);
  assert(l.head->next == NULL);
  assert(*l.head->data == 1);
  l.pop();
  assert(l.length() == 0);

  int a = 3;
  int b = 4;
  l.push(&b);
  l.push(&a);
  l.push(&z);
  l.push(&y);
  l.filter(is_even);
  assert(*l.head->data == 1);
  assert(*l.head->next->data == 3);
  l.pop();
  l.pop();
  assert(l.length() == 0);
  
  for (int i = 0; i < 10; i++) {
    l.push(&x[i]);
  }
  l.filter(is_even);
  for (int i = 9; i > 0; i = i - 2) {
    assert(*l.pop() == i);
  }
  printf("...passed \n");

  printf("testing filter_data... \n");
  assert(l.length() == 0);
  for (int i = 0; i < 10; i++) {
    l.push(&x[i]);
  }
  l.filter_data([](int x, int y) ->bool { return x == y; }, 3);
  for (int i = 9; i >= 0; i--) {
    if (i != 3) {
      assert(*l.pop() == i);
    }
  }
  printf("...passed \n");

  printf("testing find_data... \n");
  assert(l.length() == 0);
  for (int i = 0; i < 10; i++) {
    l.push(&x[i]);
  }
  assert(*l.find_data([](int x, int y) -> bool { return x == 2*y; }, 4) == 8);
  printf("...passed \n");
}
    
