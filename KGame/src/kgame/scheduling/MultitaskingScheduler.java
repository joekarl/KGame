/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kgame.scheduling;

import cern.colt.list.ObjectArrayList;
import cern.colt.map.OpenIntObjectHashMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author karl ctr kirch
 */
public class MultitaskingScheduler {

    private static MultitaskingScheduler ms;

    private OpenIntObjectHashMap activeProcesses;
    private List<BaseProcess> processesToDelete,pausedProcesses;

    public static MultitaskingScheduler defaultScheduler(){
        if(ms == null){
            ms = new MultitaskingScheduler();
        }
        return ms;
    }

    private MultitaskingScheduler(){
        activeProcesses = new OpenIntObjectHashMap();
        pausedProcesses = new ArrayList<BaseProcess>();
        processesToDelete = new ArrayList<BaseProcess>();
    }

    public void tick(long dt){
        for(Object obj : activeProcesses.values().toList()){
            BaseProcess process = (BaseProcess)obj;
            process.update(dt);
            if(process.getIsDead()){
                removeProcess(process);
            }
        }
        for(BaseProcess process : processesToDelete){
            activeProcesses.removeKey(process.getId());
        }
        processesToDelete.clear();
    }

    public void pauseProcess(BaseProcess process){
        pausedProcesses.add(process);
        activeProcesses.removeKey(process.getId());
    }

    public void resumeProcess(BaseProcess process){
        pausedProcesses.remove(process);
        activeProcesses.put(process.getId(), process);
    }

    public void addProcess(BaseProcess process) {
        activeProcesses.put(process.getId(), process);
    }

    public void removeProcess(BaseProcess process){
        process.kill();
        processesToDelete.add(process);
    }

    public void killAllProcesses(){
        ObjectArrayList values = activeProcesses.values();
        for(int i = 0; i < values.size(); ++i){
            BaseProcess process = (BaseProcess)values.get(i);
            process.kill();
            removeProcess(process);
        }
    }

    public List<Process> getAllActiveProcesses(){
        return (List<Process>) activeProcesses.values().toList();
    }
}
