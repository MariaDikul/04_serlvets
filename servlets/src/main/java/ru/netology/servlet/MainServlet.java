package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String methodGet = "GET";
  private static final String methodPost = "POST";
  private static final String methodDelete = "DELETE";
  private static final String pathApiPosts = "/api/posts";
  private static final String pathApiPostsWithId = "/api/posts";



  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
      // primitive routing
      if (method.equals(methodGet) && path.equals(pathApiPosts)) {
        controller.all(resp);
        return;
      }
      if (method.equals(methodGet) && path.matches(pathApiPostsWithId)) {
        // easy way
        controller.getById(id, resp);
        return;
      }
      if (method.equals(methodPost) && path.equals(pathApiPosts)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(methodDelete) && path.matches(pathApiPostsWithId)) {
        // easy way
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

