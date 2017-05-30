class TrieNode:
    def __init__(self, letter):
        self.letter = letter
        self.children = set()
        self.terminal = False

    def insert(self, word):
        if len(word) == 0:
            self.terminal = True
            return
        first_letter = word[0]
        for child in self.children:
            if child.get_letter() == first_letter:
                child.insert(word[1:])
                return
        new_child = TrieNode(first_letter)
        new_child.insert(word[1:])
        self.children.add(new_child)

    def get_letter(self):
        return self.letter

    def next(self, letter):
        for child in self.children:
            if child.get_letter() == letter:
                return child
        return None

    def is_terminal(self):
        return self.terminal

class Trie:
    def __init__(self):
        self.head = TrieNode(None)

    def load_array(self, array):
        for word in array:
            self.insert(word)

    def insert(self, word):
        self.head.insert(word)

#f = open("./google-10000-english-usa.txt", "r")
f = open("/usr/share/dict/words");
arr = []
while True:
    s = f.readline().strip().lower()
    if len(s) == 0:
        break
    arr.append(s)

t = Trie()
t.load_array(arr)

def on_board(pos):
    x, y = pos
    return x >= 0 and x < 5 and y >= 0 and y < 5

def next(x, y, path):
    r = []
    for i in range(3):
        for j in range(3):
            new_pos = (x + i - 1, y + j - 1)
            if on_board(new_pos) and (not (i == 1 and j == 1)) and (not new_pos in path):
                r.append(new_pos)
    return r

class Path:
    def __init__(self):
        self.path = []
        self.node = t.head

    def add_step(self, x, y):
        self.path.append((x, y))
        self.node = self.node.next(board[x][y])

    def next(self, old_path, new_x, new_y):
        self.path = old_path.path[:]
        self.path.append((new_x, new_y))
        self.node = old_path.node.next(board[new_x][new_y])

    def get_end(self):
        return self.path[-1]

def path_to_string(path):
    s = ""
    for pos in path:
        x, y = pos
        s += board[x][y]
    return s

def rec_search(board, path):
    x, y = path.get_end()
    if path.node.is_terminal() and len(path.path) > 2:
        result = path_to_string(path.path)
        if not result in found:
            print("found result", path_to_string(path.path), "with path", path.path)
            found[result] = True
    next_moves = next(x, y, path.path)
    for move in next_moves:
        new_x, new_y = move
        p = Path()
        p.next(path, new_x, new_y)
        if p.node:
            rec_search(board, p)

board = [["t","i","t","j","r"],
         ["n","w","r","e","n"],
         ["n","e","s","l","w"],
         ["u","f","r","n","t"],
         ["r","a","d","i","i"]]

found = {}
for i in range(5):
    for j in range(5):
        p = Path()
        p.add_step(i, j)
        rec_search(board, p)
print(len(found))
