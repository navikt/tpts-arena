package no.nav.tpts.util;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class SimpleCircularQueue<K> {

    private final int maxSize;
    private final LinkedList<K> queue = new LinkedList<>();

    @Synchronized
    public void put(K content) {
        while (queue.size() >= maxSize) {
            queue.remove();
        }
        queue.add(content);
    }

    @Synchronized
    public K get() {
        K first = queue.getFirst();
        queue.removeFirst();
        return first;
    }

    @Synchronized
    public List<K> getAll() {
        List<K> all = List.copyOf(queue);
        queue.clear();
        return all;
    }

    @Synchronized
    public void clear() {
        queue.clear();
    }

    @Synchronized
    public int size() {
        return queue.size();
    }

}
