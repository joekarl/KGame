/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.entity;

/**
 *
 * @author karl ctr kirch
 */
public abstract class Entity {

    static int nextId = 0;

    private int id = nextId++;

    boolean dead;

    public int getId(){
        return id;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public abstract void render(long delta);
    public abstract void update(long delta);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }
    
    
}
