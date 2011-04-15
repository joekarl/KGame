/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgame.core;

/**
 *
 * @author karl ctr kirch
 */
public abstract class ScheduledProcess extends BaseProcess implements Runnable {

    private long _accumulator,_requestedDT,_numberOfLoops;
    private boolean _infinite;

    public ScheduledProcess(long requestedDT,long numberOfLoops, boolean immediate){
        //for nanoseconds
        _requestedDT = requestedDT * 1000000L;
        //for milliseconds
        //_requestedDT = requestedDT;
        _numberOfLoops = numberOfLoops;
        _infinite = _numberOfLoops < 0;
        if(immediate) {
            update(requestedDT);
        }
    }

    public ScheduledProcess(long requestedDT, boolean immediate){
        this(requestedDT,-1,immediate);
    }

    public ScheduledProcess(long requestedDT, long numberOfLoops){
        this(requestedDT,numberOfLoops, false);
    }

    public ScheduledProcess(long requestedDT){
        this(requestedDT,-1,false);
    }

    @Override
    public final void update(long dt) {
        _accumulator += dt;
        if(_accumulator >= _requestedDT){
            run();
            _accumulator = 0;
            if(!_infinite && --_numberOfLoops <= 0){
                this.setIsDead();
            }
        }
    }
}
