package kr.co.seoulit.logistics.busisvc.sales.controller.logisales.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        session.invalidate();
        ModelAndView mv = new ModelAndView("logiinfo/loginForm");
        return mv;
        //System.out.println(session.getAttribute(""));
    }

}