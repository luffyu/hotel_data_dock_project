package com.rubber.project.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author luffyu
 * Created on 2021/3/19
 */
@Controller
public class IndexController {


    @RequestMapping("/")
    public String index(){
        return "index";
    }


    @RequestMapping("/home")
    public String indexHome(){
        return "index";
    }

}
