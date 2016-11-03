### Distributed Publish and Subscribe Framwork

Uses master, publisher, and subscriber servers. Abstraction is a a set of paths,
each path having a given publishing server. Subscribers connect to the master to
find the most recent publisher of a path, then get the value of the path from
the publisher. Each path only has a single value at a time, they can be thought
of as a stream.

### TODO

implement client/test
Switch data to a generic type
add better error handling for all servers
refactor message types to be clearer
add permissioning on master server
add authentication for publishers (master signs that the given publisher is
the correct one)