### Distributed Publish and Subscribe Framwork

Uses master, publisher, and subscriber servers. Abstraction is a a set of paths,
each path having a given publishing server. Subscribers connect to the master to
find the most recent publisher of a path, then get the value of the path from
the publisher. Each path only has a single value at a time, they can be thought
of as a stream.

###
keep working on consistency tests
-for master, add remove/get_paths_under, and other messages to consistency
add authentication for publishers (master signs that the given publisher is
the correct one)
add close methods for subscriber
switch message types to be final rather than methods where possible