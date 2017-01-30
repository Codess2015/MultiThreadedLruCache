LRU multi-threaded Caching

linked list and map

lock contention

1) Perform all the get requests and store node access in the queue/ sorted map
	sorted concurrent hash map will cost log(n) for each get(). So, just use concurrent queue.

2) in the background thread, replay all the get access from the queue on to the LL.
	Take snapshot of the queue and make a copy.

3) What to do with get(), when LL is being updated??
	get() still keep working normally.
	put() will block until LL is updated. but block only when current LL is locked.

4) update LL
	1) use a map to remove duplicates.
	2) create a new LL using the queue.

	Do a full lock on current LL and append:
	1) append to the new LL from current LL, until limit is reached.
	2) remove any left over elements from LL and map.

	Dead element: element removed from the map but present in the queue. Remove it from the new LL.

5) put()

fetch.
add to the map and queue.
return.
// Nope: too complicated and will cause contention.
use a lock on the last item on the LL and do following:
 	1. update map
	2. remove and update last item in the current LL.
update map and append to queue

5. trade off:
number of items in the map or LL might be more than the limit at any given state. Reserve.
As a extended goal: implement  immediate LL update when reserve grows after certain limit.

6. things to considered
on put(), update LL. But don’t do this because lock contention between put() and LL update(). keep put() simple.


7. Just use Blocking queue and purge will consume items in the queue and will clean up. Cache just maintains the map and
purge maintains the LL.
Cache purge will take map. concurrent hash map will be used by purge and cache. however, purge will only use map when an items
is being removed from LL (on put which needs to remove old item)