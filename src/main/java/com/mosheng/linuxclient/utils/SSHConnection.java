package com.mosheng.linuxclient.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHConnection {
    public static Connection getConnection(String ip, String user, String password) {
        Connection conn = null;
        try {
            //先创建一个连接，传入IP地址和端口
            conn = new Connection(ip, 22);
            conn.connect();
            //然后传入用户名密码
            boolean b = conn.authenticateWithPassword(user, password);
            if (!b) {
                throw new IOException();
            }
        } catch (IOException e) {
            return null;
        }
        return conn;
    }

    public static Session getSession(Connection conn){
        Session session=null;
        try {
            session= conn.openSession();
        } catch (IOException e) {
            return null;
        }
        return session;
    }

    public static String commend(Session session, String cmd) {
        StringBuilder result=new StringBuilder();
        try {
            //输入需要执行的命令
            session.execCommand(cmd);
            //然后将返回的结果转化为输入流对象
            InputStream stdout = new StreamGobbler(session.getStdout());
            //然后将流对象读取出来
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                result.append(line).append("\n");
            }
        } catch (IOException e) {
                return null;
        }
        return result.toString();
    }

    public static void close(Connection conn,Session session){
        if(conn!=null){
            conn.close();
        }
        if(session!=null){
            session.close();
        }
    }
}
