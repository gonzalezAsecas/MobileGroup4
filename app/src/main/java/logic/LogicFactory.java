/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 * The factory of the logic layer
 * @author Jon Gonzalez
 */
public class LogicFactory {
    public static Logic getLogic(){
        return new LogicImplementation();
        //return new LogicDataTestImplementation();
    }
}
