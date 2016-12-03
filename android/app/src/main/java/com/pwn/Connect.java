package com.pwn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;


public class Connect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        JSch jsch = new JSch();
        String user = "root@10.55.176.112";

        String host = "127.0.0.1";
        UserInfo ui = new UserInfo() {
            @Override
            public String getPassphrase() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public boolean promptPassword(String s) {
                return false;
            }

            @Override
            public boolean promptPassphrase(String s) {
                return false;
            }

            @Override
            public boolean promptYesNo(String s) {
                return false;
            }

            @Override
            public void showMessage(String s) {

            }
        };


        try {
            Session session = jsch.getSession(host, user, 22);
            session.setUserInfo(ui);
            session.connect();
            Channel channel = session.openChannel("exec");

            channel.setInputStream(null);

            String command = "dir";
            ((ChannelExec) channel).setCommand(command);

            ((ChannelExec) channel).setErrStream(System.err);
            try {
                InputStream in = channel.getInputStream();
                channel.connect();
                byte[] tmp = new byte[1024];
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        System.out.print(new String(tmp, 0, i));
                    }
                    if (channel.isClosed()) {
                        if (in.available() > 0) continue;
                        System.out.println("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ee) {
                    }
                }

                channel.disconnect();

                session.disconnect();

            } catch (Exception e) {

                System.out.println(e);

            }


        }catch (JSchException je){


            System.out.println("Erorr " + je);
        }



    }
}
