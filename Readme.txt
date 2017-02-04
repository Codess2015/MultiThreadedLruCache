LRU Multi-Threaded Caching

Cache implementation using "Least Recently Used" Cache replacement policy. This algorithm solves the concurrent access
problem for cache operations - get() and put() - while maintaining nearly constant runtime for the both operations.   

Interface:  
ICache defines the caching interface and LRUCacheMultiThreaded is the thread safe implementation of ICache.
LRUCacheMultiThreaded takes following configurable parameters - "cache size" and "pending queue size". Cache size is self
explanatory.  "Pending queue size" defines the threshold for number of pending queue task. When pending queue tasks for
"Cache Purge" (Cache Purge explained below) reaches this limit, cache access will be blocked until pending task count
drops again.

  Detail Design
Cache access operations are implemented to be quick retrieval or update. This is achieved by decoupling LRU functionality
from cache access. LRU functionality is implemented by “cache purge” and is explained below. LRUCacheMultiThreaded
maintains ConcurrentHashMap for the quick access of cache entities. On a get() request, requested entity is quickly
returned from the map and a non-blocking cache purge task is queued. Similarly, on a put() request, entity is added to
the map and a non-blocking cache purge task is queued. Cache purge takes care of removing oldest entity when cache size
reached the provided limit. If multiple threads attempts to add the same entity to the cache, only one will succeed.

Cache Purge:
 In traditional - non thread safe - LRU cache implementation, DoublyLinkedList is preferred way to maintain access ordering
 of the cache entities. However, in multi-threaded environment, it is not safe to update LinkedList on the cache access
threads.  One option is to synchronize LinkedList access but that will result in high lock contention for each get() and
set() operation. To avoid  lock contention among these operations and still be able to use LinkedList (or other data
structure) for LRU policy, LinkedList updates are thread confined  to a service thread. Service thread is maintained by
the cache, instance of LRUCacheMultiThreaded. On each cache access operation, accessed cache entity is queued using
BlockingQueue, service thread processes these entries one by one and maintains  LRU ordering using linkedList.
Service thread runs PurgeCacheNotThreadSafe runnable, as mentioned in its name, Purge cache is not thread safe  and is
not meant for the concurrent access.  

Performance: 
Cache operations - get() and put() - are expected to be constant time depending upon "Pending queue size" threshold. If
this value is  too small, cache purge will become bottleneck.  

Testing: 
TBD
