/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.entity;

import cern.colt.map.OpenIntObjectHashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author karl ctr kirch
 */
public class EntityManager {

    private OpenIntObjectHashMap entities;
    private static EntityManager defaultManager;

    public static EntityManager defaultManager() {
        if (defaultManager == null) {
            defaultManager = new EntityManager();
        }
        return defaultManager;
    }

    private EntityManager() {
        entities = new OpenIntObjectHashMap();
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    public Entity getEntity(int id) {
        return (Entity) entities.get(id);
    }

    public Entity removeEntity(int id){
        Entity e = (Entity) entities.get(id);
        if(e != null && entities.removeKey(id)){
            return e;
        }
        return null;
    }

    public List<Entity> getEntities(){
        return entities.values().toList();
    }

    public void cleanDeadEntities(){
        List<Entity> entityList = getEntities();
        Iterator<Entity> entityIterator = entityList.iterator();
        while(entityIterator.hasNext()){
            Entity entity = entityIterator.next();
            if(entity.isDead()){
                entityIterator.remove();
                removeEntity(entity.getId());
            }
        }
    }

}
