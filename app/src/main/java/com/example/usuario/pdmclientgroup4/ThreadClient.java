package com.example.usuario.pdmclientgroup4;

import logic.Logic;
import message.User;

public class ThreadClient extends Thread{

    private User user;
    private Integer msg;
    private Logic logic;

    public User getUser(){
        return user;
    }

    public ThreadClient(User user, int msg, Logic logic){
        this.user=user;
        this.msg=msg;
        this.logic=logic;
    }

    @Override
    public void run() {
        if(msg==1){
            try {
                user=logic.loginUser(user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(msg==2){
            try{
                logic.signUpUser(user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
