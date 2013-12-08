package org.sangraama.gameLogic.aoi.subtile;

import org.sangraama.jsonprotocols.send.PlayerDelta;

/**
 * Interface for procedures with one int parameter.
 *
 * Created: Mon Nov  5 21:45:49 2013
 *
 * @author Gihan Karunarathne
 */
public interface TraceBackNode {
    /**
     * Executes this procedure. A false return value indicates that
     * the application executing this procedure should not invoke this
     * procedure again.
     *
     * @param value a value of type <code>int</code>
     * @return true if additional invocations of the procedure are
     * allowed.
     */
    boolean executePlayer(long id);
    
    boolean executeBullet(long id);

    boolean execute(PlayerDelta playerDelta);
}
