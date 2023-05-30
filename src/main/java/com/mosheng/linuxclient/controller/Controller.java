package com.mosheng.linuxclient.controller;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.mosheng.linuxclient.utils.SSHConnection;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String hello() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String host, String username, String password, Model model, HttpSession httpSession) {
        if(host==null||host==""){
            model.addAttribute("msg", "信息填写不完整!");
            return "login";
        }
        if(username==null||username==""){
            model.addAttribute("msg", "信息填写不完整!");
            return "login";
        }
        if(password==null||password==""){
            model.addAttribute("msg", "信息填写不完整!");
            return "login";
        }
        Connection connection = SSHConnection.getConnection(host, username, password);
        if (connection == null) {
            model.addAttribute("msg", "用户名或密码错误!");
            return "login";
        } else {
            httpSession.setAttribute("connection", connection);
            model.addAttribute("host", host);
            model.addAttribute("user", username);
            return "main";
        }
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @ResponseBody
    @PostMapping("/cmd")
    public String commend(@RequestParam("cmd") String cmd, HttpSession httpSession) {
        Connection connection = (Connection) httpSession.getAttribute("connection");
        System.out.println(cmd);
        Session session = SSHConnection.getSession(connection);
        String result = SSHConnection.commend(session, cmd);
        SSHConnection.close(null, session);
        System.out.println(result);
        return result;
    }

    @GetMapping("/exit")
    public String exit(HttpSession httpSession) {
        Connection connection = (Connection) httpSession.getAttribute("connection");
        SSHConnection.close(connection, null);
        return "login";
    }
}
