package pt.tecnico.distledger.utils;

import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;

import java.util.ArrayList;
import java.util.List;

public class VectorClock {
    private final List<Integer> timestamps;

    public VectorClock(int id) {
        timestamps = new ArrayList<>(id+1);
        for (int i = 0; i < id+1; i++) {
            timestamps.add(0);
        }
    }

    public VectorClock(List<Integer> timestamps) {
        this.timestamps = new ArrayList<>(timestamps);
    }

    public List<Integer> getTimestamps() {
        return timestamps;
    }

    public Integer getTimestamp(Integer i) {
        if (i >= timestamps.size()) {
            return 0;
        }
        return timestamps.get(i);
    }


    public void setTimestamp(Integer i, Integer value) {
        if (i >= timestamps.size()) {
            for (int j = timestamps.size(); j <= i; j++) {
                timestamps.add(0);
            }
        }
        timestamps.set(i, value);
    }

    public DistLedgerCommonDefinitions.VectorClock toProto() {
        return DistLedgerCommonDefinitions.VectorClock.newBuilder().addAllTimestamp(timestamps).build();
    }

    public boolean greaterOrEqual(VectorClock v) {
        int size = Math.max(timestamps.size(), v.getTimestamps().size());
        for (int i = 0; i < size; i++) {
            if (this.getTimestamp(i) < v.getTimestamp(i)) {
                return false;
            }
        }
        return true;
    }

    public void merge(VectorClock v) {
        int size = Math.max(timestamps.size(), v.getTimestamps().size());
        for (int i = 0; i < size; i++) {
            if (this.getTimestamp(i) > v.getTimestamp(i)) {
                this.setTimestamp(i, this.getTimestamp(i));
            } else {
                this.setTimestamp(i, v.getTimestamp(i));
            }
        }
    }

}