package com.greenfoxacademy.springwebapp.webSockets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping("/ws-test")
  public String index() {
    return "index.html";
  }

}
