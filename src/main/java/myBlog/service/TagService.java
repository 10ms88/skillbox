package myBlog.service;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import myBlog.api.response.TagResponse;
import myBlog.dto.TagDto;
import myBlog.model.Tag;
import myBlog.repository.PostRepository;
import myBlog.repository.TagRepository;

@Service
public class TagService {

  private final TagResponse tagResponse = new TagResponse();

  @Autowired
  TagRepository tagRepository;

  @Autowired
  PostRepository postRepository;


  public TagResponse getTagList(String name) {
    List<Tag> tagList = tagRepository.findAllByStartWith(name + "%");
    tagResponse.setTags(new ArrayList<>());
    List<Integer> listPostCount = new ArrayList<>();
    for (Tag tag : tagList) {
      listPostCount.add(tag.getPostList().size());
    }
    //     находим общее количество постов count

    int count = Lists.newArrayList(postRepository.findAll()).size();

    //     находим самый популярный тег mostPolarTagCount
    int mostPolarTagCount = Collections.max(listPostCount);

    //    Находим ненормированный вес самого популярного тега dWeightMax
    double dWeightMax = (double) mostPolarTagCount / count;

    //      Находим коэффициент k для нормализации
    double k = 0;
    if (dWeightMax != 0) {
      k = 1 / dWeightMax;
    }
    //      Нормируем каждый вес weight
    double weight;
    for (Tag tag : tagList) {
      weight = (double) tag.getPostList().size() / count * k;
      tagResponse.getTags().add(TagDto.of(tag.getName(), weight));
    }
    return tagResponse;
  }
}
