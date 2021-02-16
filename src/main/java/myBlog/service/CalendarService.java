package myBlog.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.api.response.CalendarResponse;
import myBlog.repository.PostRepository;

@Service
public class CalendarService {

  private final CalendarResponse calendarResponse = new CalendarResponse();

  @Autowired
  PostRepository postRepository;


  public CalendarResponse getPostByYear(String year) {
    calendarResponse.setYears(postRepository.calendarYears());
    calendarResponse.setPosts(new HashMap<>());
    String yearStart = year + "-01-01 00:00:00";
    String yearEnd = year + "-12-31 23:59:59";
    List<String> s = postRepository.calendarPosts(yearStart, yearEnd);
    s.forEach(p -> {
      String[] split = p.split(",");
      calendarResponse.getPosts().put(split[0], Integer.valueOf(split[1]));
    });
    return calendarResponse;
  }
}
