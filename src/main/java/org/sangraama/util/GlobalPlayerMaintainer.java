package org.sangraama.util;

import com.hazelcast.core.HazelcastInstance;
import org.sangraama.coordination.staticPartition.TileCoordinator;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public enum GlobalPlayerMaintainer {
    INSTANCE;
    private HazelcastInstance hzctInstance;
    private Map<Long, String> globalPlayerMap;
    private String host;

    private GlobalPlayerMaintainer() {
        hzctInstance = TileCoordinator.INSTANCE.getHazelcastInstance();
        this.globalPlayerMap = hzctInstance.getMap("SangraamaPlayers");

        Properties prop = new Properties();

        try {
            prop.load(getClass().getResourceAsStream("/conf/sangraamaserver.properties"));
        } catch (IOException e) {
            System.out.println("sangraamaserver.properties file not found.");
            e.printStackTrace();
        }
        host = prop.getProperty("host") + ":" + prop.getProperty("port");
    }

    public boolean registerPlayerGlobally(long userID) {
        boolean registered;
        if (globalPlayerMap.containsKey(userID)) {
            registered = false;
        } else {
            globalPlayerMap.put(userID, host);
            registered = true;
        }
        printEntries();
        return registered;
    }

    public void changePlayerHost(long userID) {
        if (globalPlayerMap.containsKey(userID)) {
            globalPlayerMap.put(userID, host);
        }
    }

    public boolean checkPlayerExistance(long userID) {
        boolean exist = false;
        if (globalPlayerMap.containsKey(userID)) {
            exist = true;
        }
        return exist;
    }

    private void printEntries() {
        if (!globalPlayerMap.isEmpty()) {
            Object[] a = globalPlayerMap.keySet().toArray();
            Object[] b = globalPlayerMap.values().toArray();
            for (int i = 0; i < globalPlayerMap.size(); i++) {
                System.out.println(a[i] + " ++ " + b[i]);
            }
        }
    }
}
