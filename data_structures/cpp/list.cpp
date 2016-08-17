#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <assert.h>
#include <iostream>
#include<sstream>

template <class T>

class ListNode {
public:
  T *data;
  ListNode *next;

  ListNode(T *data) {
    this->data = data;
    this->next = NULL;
  }

  ~ListNode() {
    printf("deleting node\n");
    if (this->next != NULL) {
      printf("passing to next node\n");
      delete(this->next);
    }
  }
};
  
template <class T>
class List {
  ListNode<T> *head;

public:
  List() {
    head = NULL;
  }
  
  ~List() {
    if (head != NULL) {
      delete(head);
    }
  }
  
  void push(T *data) {
    if (head == NULL) {
      head = new ListNode<T>(data);
    } else {
      ListNode<T> *n = head;
      while (n->next != NULL) {
	n = n->next;
      }
      n->next = new ListNode<T>(data);
    }
  }

  T *pop() {
    if (head == NULL) return NULL;
    T *ret = head->data;
    head = head->next;
    return ret;
  }
};

template <typename T>
std::string to_string(T value) {
  std::ostringstream os;
  os << value;
  return os.str();
}

int main() {
  printf("Testing push and pop... \n");
  List<int> l;
  for (int i = 0; i < 10; i++) {
    l.push(&i);
  }
  for (int i = 0; i >= 10; i) {
    assert(*l.pop() == (10 -i));
  }
  printf("...passed \n");
  return 0;
}
