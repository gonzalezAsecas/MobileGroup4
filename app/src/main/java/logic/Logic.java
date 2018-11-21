/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;


import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import message.User;

/**
 *
 * @author Jon Gonzalez
 */
public interface Logic {
    User loginUser(User user) throws LoginNotExistingException,
            WrongPasswordException, Exception;
    void signUpUser(User user)throws LoginExistingException,
            EmailNotUniqueException, Exception;
}
