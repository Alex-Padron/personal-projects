
class Actors:
    def __init__(self, actors):
        self.actors = actors

    #get a weighted score for how unique the set is 
    def score(self):
        counts = {}
        for actor in self.actors:
            inc (counts, actor.classification)
            delta = 0
            total = 0 
        if len (counts) == 1:
            return 1
        for x in counts:
            for y in counts: 
                if x != y:
                    delta += (counts[x] - counts[y]) ** 2
                    total += counts[x]
                    total += counts[y]
        return 1.0*delta/total

    #split the actors into subsets based on the field
    def split (self, field):
        groups = {}
        for actor in self.actors:
            attr = actor.attrs[field]
            set_or_append (groups, attr, actor)
        return groups
    
    def arity (actors):
        assert len(actors) > 0
        arity = len(actors[0].attrs)
        for actor in actors:
            assert arity == len(actor.attrs)
        return arity 

def best_split (actors):
    assert len(actors) > 0 
    best_score = -1
    best_split = None
    best_field = None 
    for field in range(arity(actors)):
        groups = split(actors,field)
        group_score = 0 
        for attr in groups:
            group_score += score (groups[attr])
        if group_score > best_score:
            best_score = group_score 
            best_split = groups
            best_field = field
    return best_split, best_field

    def is_homogenous (self):
        if len(actors) == 0:
            return False, None 
        classification = actors[0].classification
        for actor in actors:
            if actor.classification != classification:
                return False, None
        return True, classification

def generate (actors):
    h, classification = is_homogenous(actors)
    #if the group is homogenous, return the end node 
    if h:
        return Node(0, classification, True)
    groups, field = best_split(actors)
    possibilities = {}
    for attr in groups:
        possibilities[attr] = generate(groups[attr])
    return Node(field, possibilities, False)




        
def field_name (field, field_names):
    if field in field_names:
        return field_names[field]
    else:
        return str(field)

def inc (values, val):
    if val in values:
        values[val] = values[val] + 1
    else:
        values[val] = 1
    
def set_or_append (values, key, val):
    if key in values:
        values[key].append(val)
    else:
        values[key] = [val]

    

class Node:
    #field defines which field within the actors to search over.
    #possibilities is a map from possible field values to either other node
    #is_terminal: if the node has no children
    def __init__(self, field, possibilities, is_terminal):
        self.field = field
        self.possibilities = possibilities
        self.is_terminal = is_terminal
        
    def to_string (self, field_names):
        return self.to_string_indent(0, field_names)

    def to_string_indent (self, indent, field_names):
        if self.is_terminal:
            return ("NODE: terminal, classification: %s" % self.possibilities)
        else:
            s = "NODE: field: %s" % field_name(self.field, field_names)
            for possibility in self.possibilities:
                s = s + "\n" + (indent * " ") + "case %s: %s" \
                    % (possibility, self.possibilities[possibility].to_string_indent(indent + 4, field_names))
            return s

    def query (self, actor):
        if self.is_terminal:
            return self.possibilities
        field_val = actor.attrs[self.field]
        if not field_val in self.possibilities:
            return "unknown attr %s - cannot parse in tree" % field_val
        return self.possibilities[field_val].query(actor)
class Actor:
    #attsrs is a list of strings representing an attribute, for example hair color
    #could be ["red"] or ["blue"].
    def __init__ (self, attrs, classification):
        self.attrs = attrs
        self.classification = classification

""" TESTS """
print "testing field_name..."
field_names = {1:"foo", 2:"bar"}
assert field_name(1, field_names) == "foo"
assert field_name(2, field_names) == "bar"
assert field_name(1, {}) == "1"
assert field_name(3, field_names) == "3"
print "...passed"

print "testing inc..."
values = {}

inc (values, 5)
assert values[5] == 1
inc (values, 5)
assert values[5] == 2

inc (values, 7)
assert values[7] == 1
assert values[5] == 2

assert len(values) == 2
print "...passed"

print "testing set_or_append..."
values = {}
set_or_append (values, "key", 5)
set_or_append (values, "key", 7)
assert values["key"] == [5,7]

set_or_append (values, "key", 9)
assert values["key"] == [5,7,9]
print "...passed"

print "testing score..."
actors = [
    Actor (["brown", "blue", "tall"], "mit"),
    Actor (["brown", "blue", "tall"], "mit"),
]
assert score(actors) == 1
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit")
]
assert score(actors) == 0
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "brown", "tall"], "mit")

]
s = score(actors)
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "brown", "tall"], "mit")
]
assert 0 < s < score(actors) <= 1
print "...passed"

print "testing split..."
actors = [
    Actor (["brown", "blue", "tall"], "mit"),
    Actor (["grey", "blue", "tall"], "mit"),
]
splits = split(actors, 0)
assert len(splits["brown"]) == 1
assert len(splits["grey"]) == 1
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "brown", "tall"], "mit")
]
splits = split(actors, 0)
assert len(splits["grey"]) == 4
assert len(splits) == 1
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "tall"], "mit")
]
splits = split(actors, 1)
assert len(splits) == 2
assert len(splits["blue"]) == 1
assert len(splits["brown"]) == 3
print "...passed"

print "testing arity..."
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["grey", "brown", "tall"], "mit"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "tall"], "mit")
]
assert arity(actors) == 3
actors = [
    Actor (["grey", "brown"], "harvard"),
    Actor (["grey", "brown"], "mit"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
assert arity(actors) == 2
actors = [
    Actor ([], "harvard"),
]
assert arity(actors) == 0 
print "...passed"

print "testing best_split..."
actors = [
    Actor (["grey", "brown"], "harvard"),
    Actor (["grey", "blue"], "mit"),
    Actor (["grey", "blue"], "mit"),
]
s, field = best_split(actors)
assert field == 1
assert len(s["brown"]) == 1
assert len(s["blue"]) == 2
assert s["blue"][0].classification == "mit"
assert s["blue"][1].classification == "mit"
assert s["brown"][0].classification == "harvard"

actors = [
    Actor (["grey", "brown"], "harvard"),
    Actor (["brown", "brown"], "harvard"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
s, field = best_split(actors)
assert field == 1
assert len(s["blue"]) == 1
assert len(s["brown"]) == 3
actors = [
    Actor (["grey", "brown"], "harvard"),
    Actor (["grey", "brown"], "harvard"),
    Actor (["brown", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
s, field = best_split(actors)
assert field == 0 
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["brown", "brown", "tall"], "harvard"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "short"], "mit")
]
s, field = best_split(actors)
assert field == 1
print "...passed"

print "testing is_homogenous..."
actors = [
    Actor (["grey", "brown"], "harvard"),
    Actor (["brown", "brown"], "harvard"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
h, c = is_homogenous(actors)
assert not h
actors = [
    Actor (["grey", "brown"], "mit"),
    Actor (["brown", "brown"], "mit"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
h, c = is_homogenous(actors)
assert h
assert c == "mit"
print "...passed"

print "testing generate..."
actors = [
    Actor (["grey", "brown"], "mit"),
    Actor (["brown", "brown"], "mit"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
n = generate(actors)
assert n.is_terminal
assert n.possibilities == "mit"
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["brown", "brown", "tall"], "harvard"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "short"], "mit")
]
n = generate(actors)
assert n.field == 1
assert len(n.possibilities) == 2
n2 = n.possibilities["brown"]
assert n2.field == 0 
n3 = n.possibilities["blue"]
assert n3.is_terminal
assert n3.possibilities == "mit"
assert len(n2.possibilities) == 2
n4 = n2.possibilities["brown"]
n5 = n2.possibilities["grey"]
assert n5.is_terminal
assert n5.possibilities == "harvard"
assert len(n4.possibilities) == 2
n6 = n4.possibilities["short"] 
n7 = n4.possibilities["tall"]
assert n6.is_terminal
assert n6.possibilities == "mit"
assert n7.is_terminal
assert n7.possibilities == "harvard"
print "...passed"
 
print "testing node to_string..."
actors = [
    Actor (["grey", "brown"], "mit"),
    Actor (["brown", "brown"], "mit"),
    Actor (["grey", "blue"], "mit"),
    Actor (["brown", "brown"], "mit")
]
n = generate(actors)
assert n.to_string({}) == "\
NODE: terminal, classification: mit"

actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["brown", "brown", "tall"], "harvard"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "short"], "mit")
]
n = generate(actors)
assert n.to_string({}) == """NODE: field: 1
case blue: NODE: terminal, classification: mit
case brown: NODE: field: 0
    case brown: NODE: field: 2
        case tall: NODE: terminal, classification: harvard
        case short: NODE: terminal, classification: mit
    case grey: NODE: terminal, classification: harvard"""
print "...passed"

print "testing query..."
actors = [
    Actor (["grey", "brown", "short"], "harvard"),
    Actor (["brown", "brown", "tall"], "harvard"),
    Actor (["grey", "blue", "tall"], "mit"),
    Actor (["brown", "brown", "short"], "mit")
]
n = generate(actors)
assert n.query(Actor (["grey", "brown", "short"], "unknown")) == "harvard"
assert n.query(Actor (["grey", "blue", "tall"], "unknown")) == "mit"
assert n.query(Actor (["brown", "blue", "tall"], "unknown")) == "mit"
assert n.query(Actor (["grey", "brown", "tall"], "unknown")) == "harvard"
print "...passed"

print "testing vampires..."
field_names = {0:"shadow", 1:"garlic", 2:"complextion", 3:"accent"}
actors = [
    Actor (["unknown", "yes", "pale"   , "none" ], "no"     ),
    Actor (["yes"    , "yes", "ruddy"  , "none" ], "no"     ),
    Actor (["unknown", "no" , "ruddy"  , "none" ], "vampire"),
    Actor (["no"     , "no" , "average", "heavy"], "vampire"),
    Actor (["unknown", "no" , "average", "odd"  ], "vampire"),
    Actor (["yes"    , "no" , "pale"   , "heavy"], "no"     ),
    Actor (["yes"    , "no" , "average", "heavy"], "no"     ),
    Actor (["unknown", "yes", "ruddy"  , "odd"  ], "no"     )
]
n = generate(actors)
assert n.to_string(field_names) == """NODE: field: shadow
case unknown: NODE: field: garlic
    case yes: NODE: terminal, classification: no
    case no: NODE: terminal, classification: vampire
case yes: NODE: terminal, classification: no
case no: NODE: terminal, classification: vampire"""

assert n.query(Actor (["unknown", "no", "pale", "odd"], "unknown")) == "vampire"
assert n.query(Actor (["yes", "no", "pale", "odd"], "unknown")) == "no"
assert n.query(Actor (["no", "yes", "pale", "odd"], "unknown")) == "vampire"
print(n.to_string(field_names))
print "...passed"


