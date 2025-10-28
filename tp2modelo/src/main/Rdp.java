package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rdp {
    public int transitions = 12;
    public int places = 15;

    private final double[][] incidenceMatrix = {
        // t0  t1  t2   t3  t4  t5  t6  t7  t8  t9  t10 t11
           {-1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1 }, // p0
           {-1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // p1
           { 1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 }, // p2
           { 0,  1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0 }, // p3
           {-1,  0,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0 }, // p4
           { 0,  0,  1,  0,  0, -1,  0,  0,  0,  0,  0,  0 }, // p5
           { 0,  0, -1,  0,  0,  1,  0,  0,  0,  0,  0,  0 }, // p6
           { 0,  0,  0, -1,  1,  0,  0,  0,  0,  0,  0,  0 }, // p7
           { 0,  0,  0,  1, -1,  0,  0,  0,  0,  0,  0,  0 }, // p8
           { 0,  0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0 }, // p9
           { 0,  0,  0,  0,  0,  0, -1, -1,  1,  0,  1,  0 }, // p10
           { 0,  0,  0,  0,  0,  0,  1,  0,  0, -1,  0,  0 }, // p11
           { 0,  0,  0,  0,  0,  0,  0,  1, -1,  0,  0,  0 }, // p12
           { 0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1,  0 }, // p13
           { 0,  0,  0,  0,  0,  0,  0,  0,  1,  0,  1, -1 }  // p14
    };

    private final double[] initialMarking = {5, 1, 0, 0, 5, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0};
    private final List<Integer> transitionSleepTime = Collections.unmodifiableList(
        Arrays.asList(0 , 0 , 0 , 0 , 0, 0, 0, 0, 0, 0, 0, 0)
    );

    private final long[] transitionTime = new long[transitions];
    private final int[] firedCount = new int[transitions];
    private final double[] vectorMarking = new double[places];
    private String sequence = "";
    private int maxInvariant;
    private int lastFired;

    public Rdp(int max) {
        System.arraycopy(initialMarking, 0, vectorMarking, 0, initialMarking.length);
        Arrays.fill(transitionTime, -1);
        transitionTime[0] = System.currentTimeMillis();
        maxInvariant = max;
    }

    public long isEnabled(int t) {
        if (t == 0 && firedCount[0] >= maxInvariant) return -1;

        for (int p = 0; p < vectorMarking.length; p++) {
            if (vectorMarking[p] + incidenceMatrix[p][t] < 0) return -1;
        }

        if (transitionTime[t] == -1) transitionTime[t] = System.currentTimeMillis();

        return Math.max(transitionTime[t] + transitionSleepTime.get(t) - System.currentTimeMillis(), 0);
    }

    public void fire(int t) {
        testPlaceInvariant();
        if (isEnabled(t) == 0) {
            for (int p = 0; p < vectorMarking.length; p++) {
                vectorMarking[p] += incidenceMatrix[p][t];
            }
            transitionTime[t] = -1;
            sequence += (t < 10 ? "T0" : "T") + t;
            firedCount[t]++;
            lastFired = t;
        }
    }

    public List<Integer> whichEnabled() {
        List<Integer> result = new ArrayList<>();
        for (int t = 0; t < transitions; t++) {
            if (isEnabled(t) == 0) result.add(t);
        }
        return result;
    }

    public String getSequence() {
        return sequence;
    }

    public int[] getFiredCounter() {
        return firedCount;
    }

    public int getMarking(int p) {
        return (int) vectorMarking[p];
    }

    public boolean completedInvariants() {
        return firedCount[11] >= maxInvariant;
    }

    private void testPlaceInvariant() {
        boolean p1 = (getMarking(1) + getMarking(2)) == 1;
        boolean p2 = (getMarking(2) + getMarking(3) + getMarking(4)) == 5;
        boolean p3 = (getMarking(5) + getMarking(6)) == 1;
        boolean p4 = (getMarking(7) + getMarking(8)) == 1;
        boolean p5 = (getMarking(10) + getMarking(11) + getMarking(12) + getMarking(13)) == 1;
        boolean p6 = (getMarking(0) + getMarking(2) + getMarking(3) + 
                    getMarking(5) + getMarking(8) + getMarking(9) + 
                    getMarking(11) + getMarking(12) + getMarking(13) + getMarking(14)) == 5;

        if (!(p1 && p2 && p3 && p4 && p5 && p6)) {
            System.out.println("ERROR EN INVARIANTE DE TRANSICION, CERRANDO EJECUCION.");
            System.exit(0);
        }
    }

    public List<Integer> whichEnabledAfterLastFired() {
        List<Integer> lista = new ArrayList<>();

        switch (lastFired) {
            case 0:
                if (isEnabled(1)==0){
                    lista.add(1);
                }
                break;
            case 1:
                if (isEnabled(2)==0){
                    lista.add(2);
                }
                if (isEnabled(3)==0){
                    lista.add(3);
                }
                break;
            case 2:
                if (isEnabled(5)==0){
                    lista.add(5);
                }
                break;
            case 3:
                if (isEnabled(4)==0){
                    lista.add(4);
                }
                break;
            case 4, 5:
                if (isEnabled(6)==0){
                    lista.add(6);
                }
                if (isEnabled(7)==0){
                    lista.add(7);
                }
                break;
            case 6:
                if (isEnabled(9)==0){
                    lista.add(9);
                }
                break;
            case 7:
                if (isEnabled(8)==0){
                    lista.add(8);
                }
                break;
            case 8, 10:
                if (isEnabled(11)==0){
                    lista.add(11);
                }
                break;
            case 9:
                if (isEnabled(10)==0){
                    lista.add(10);
                }
                break;
            case 11:
                if (isEnabled(0)==0){
                    lista.add(0);
                }
                break;
            default:
                lista.add(0);
                break;
        }
        return lista;
    }

}
