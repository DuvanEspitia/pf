/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;


/**
 *  
 *  
 *
 * @author juand 
 */
public class MyStartScreen extends BaseAppState implements ScreenController
{    
    private Nifty nifty;
    private Main aplication;
    /**
     * custom methods
     * @param nifty
     */
    





    public MyStartScreen(Nifty nifty, Main aplication) {
        this.nifty = nifty;
        this.aplication = aplication;
        //It is technically safe to do all initialization and cleanup in the        
        //onEnable()/onDisable() methods. Choosing to use initialize() and        
        //cleanup() for this is a matter of performance specifics for the        
        //implementor.       
        //TODO: initialize your AppState, e.g. attach spatials to rootNode   
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    public Main getAplication() {
        return aplication;
    }

    public void setAplication(Main aplication) {
        this.aplication = aplication;
    }
    
    

    @Override
    protected void initialize(Application app) {
        //It is technically safe to do all initialization and cleanup in the        
        //onEnable()/onDisable() methods. Choosing to use initialize() and        
        //cleanup() for this is a matter of performance specifics for the        
        //implementor.       
        //TODO: initialize your AppState, e.g. attach spatials to rootNode   
    }
    
    
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);  // switch to another screen
        // start the game and do some more stuff...
    }

    public void quitGame() {
        getAplication().stop();
    }

    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    
    
    @Override
    protected void onEnable() {
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }

    @Override
    public void update(float tpf) 
    {
        //TODO: implement behavior during runtime    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
       
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }

    static class java {

        public java() {
        }
    }

}
