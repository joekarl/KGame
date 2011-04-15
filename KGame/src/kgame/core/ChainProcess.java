/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.core;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author karl ctr kirch
 */
public class ChainProcess extends BaseProcess {

    Queue<BaseProcess> processes = new LinkedList<BaseProcess>();

    @Override
    public void update(long dt) {
        BaseProcess peek = processes.peek();
        if(peek != null && peek.getIsDead()){
            processes.remove().kill();
            update(dt);
        } else if(peek != null) {
            peek.update(dt);
        }
    }

    public void addProcess(BaseProcess process){
        processes.add(process);
    }

}
