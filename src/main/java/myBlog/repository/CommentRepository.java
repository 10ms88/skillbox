package myBlog.repository;

import org.springframework.data.repository.CrudRepository;

import myBlog.model.PostComment;

public interface CommentRepository extends CrudRepository<PostComment, Integer> {

}
