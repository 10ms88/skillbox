package myBlog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class DefaultController {

  @RequestMapping("/")
  public ModelAndView index() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("index");
    return modelAndView;
  }
}
