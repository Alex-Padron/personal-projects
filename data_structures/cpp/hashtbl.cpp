#include "list.cpp"
#include <cmath>

template <class K, class V>
class KV_node {
public:
  K key;
  V value;
  
  KV_node(K key, V value) {
    this->key = key;
    this->value = value;
  }
  
  KV_node(K key) {
    this->key = key;
  }
};

template <class K, class V>
class Hashtbl {
  int num_buckets;
  int num_elements;
  List<KV_node<K,V>> **tbl;

public:
  Hashtbl(int size) {
    num_buckets = pow(2, size);
    num_elements = 0;
    tbl = new List<KV_node<K,V>>*[num_buckets];
  }
  
  bool matches_key(K key, KV_node<K,V> n) {
    return key == n.key;
  }

  void put(K key, V value) {
    std::size_t index = std::hash<K>()(key) % num_buckets;
    List<KV_node<K,V>>* bucket = tbl[index];
    KV_node<K,V> *new_node = new KV_node<K,V>(key, value);
    //either create a new bucket or filter duplicates
    if (bucket == NULL) {
      bucket = new List<KV_node<K,V>>;
    } else {
      int old_bucket_size = bucket->length();
      //std::function<bool (KV_node<K,V> n)> f = [=](KV_node<K,V> n) -> bool { return n.key == key; };
      bucket->filter_data([](KV_node<K,V> x, KV_node<K,V> y) -> bool { return x.key == y.key; }, *new_node);
      num_elements = num_elements - old_bucket_size + bucket->length();
    }
    bucket->push(new_node);
    tbl[index] = bucket;
    num_elements += 1;
  }

  V *get(K key) {
    std::size_t index = std::hash<K>()(key) % num_buckets;
    List<KV_node<K,V>>* bucket = tbl[index];
    if (bucket == NULL) return NULL;
    KV_node<K,V> comparison_node (key);
    return &bucket->find_data([](KV_node<K,V> x, KV_node<K,V> y) -> bool 
			     { return x.key == y.key; }, comparison_node)->value;
  }

  int elements() {
    return num_elements;
  }

  int buckets() {
    return num_buckets;
  }
};

void hashtbl_unit_test() {
  printf("testing basic put and get...\n");
  Hashtbl<int, int> ht(4);
  for (int i = 0; i < 1000; i++) {
    ht.put(i, 2*i);
  }
  for(int i = 0; i < 1000; i++) {
    assert(*ht.get(i) == 2*i);
  }
  printf("...passed\n");
}

int main () {
  printf("TESTING LIST\n");
  list_unit_test();
  printf("PASSED LIST\n");
  printf("TESTING HASHTBL\n");
  hashtbl_unit_test();
  printf("PASSED HASHTBL\n");
}
