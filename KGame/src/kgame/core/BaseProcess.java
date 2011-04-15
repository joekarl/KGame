/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.core;

/**
 *
 * @author karl ctr kirch
 */
public abstract class BaseProcess {

    static int nextId = 0;

    private int id = nextId++;


    boolean isDead = false;

    public final int getId(){
        return id;
    }

    public void setIsDead(){
        isDead = true;
    }

    public boolean getIsDead(){
        return isDead;
    }

    public abstract void update(long dt);

    public void kill(){
        
    }
}
