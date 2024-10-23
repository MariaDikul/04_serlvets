package ru.netology.repository;
import org.springframework.stereotype.Repository;
import ru.netology.model.Post;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
    private static final ConcurrentHashMap<Long, Post> repository = new ConcurrentHashMap<>();
    private static final AtomicLong counter = new AtomicLong(1);
    public List<Post> all() {
        List<Post> list = new ArrayList<>();
        for (Map.Entry entry : repository.entrySet()) {
            list.add((Post) entry.getValue());
        }
        return list;
    }

    public Optional<Post> getById(long id) {
        if(repository.get(id) != null) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long i = counter.getAndIncrement();
            while (repository.containsKey(i)) {
                i = counter.getAndIncrement();
            }
            post.setId(i);
            repository.put(i, post);
        } else {
            repository.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        repository.remove(id);
    }
}
