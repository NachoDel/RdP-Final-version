package main;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // MAXIMUM NUMBER OF SHOTS (T0)
        int maxFires = 186;

        // THREADS THAT EXECUTE EACH TASK
        int thread1 = 1;
        int thread2 = 1;
        int thread3 = 1;
        int thread4 = 1;
        int thread5 = 1;
        int thread6 = 1;

        // TRANSITIONS THAT EACH THREAD TRIGGERS
        /// SA (T0, T1)
        List<Integer> segmentA = new ArrayList<>();
        segmentA.add(0);
        segmentA.add(1);

        /// SB (T2, T5)
        List<Integer> segmentB = new ArrayList<>();
        segmentB.add(2);
        segmentB.add(5);

        /// SC (T3, T4)
        List<Integer> segmentC = new ArrayList<>();
        segmentC.add(3);
        segmentC.add(4);

        /// SD (T6, T9, T10)
        List<Integer> segmentD = new ArrayList<>();
        segmentD.add(6);
        segmentD.add(9);
        segmentD.add(10);

        /// SE (T7, T8)
        List<Integer> segmentE = new ArrayList<>();
        segmentE.add(7);
        segmentE.add(8);

        /// SF (T11)
        List<Integer> segmentF = new ArrayList<>();
        segmentF.add(11);

        // INITIALIZING THE MONITOR WITH POLICY AND RDP
        Rdp rdp = new Rdp(maxFires);
        Policy policy = new Policy(false);   // true es equitativo, false es priorizada
        Monitor monitor = new Monitor(rdp, policy);

        // ARRANGEMENTS TO SET TRANSITIONS TO EACH THREADS
        Threads[] SA = new Threads[thread1];
        Threads[] SB = new Threads[thread2];
        Threads[] SC = new Threads[thread3];
        Threads[] SD = new Threads[thread4];
        Threads[] SE = new Threads[thread5];
        Threads[] SF = new Threads[thread6];

        // ASSIGNING TRANSITIONS TO THREADS
        /// SA
        for (int i = 0; i < thread1; i++){
            SA[i] = new Threads(segmentA, monitor);
            SA[i].setName("SA " + i);
        }

        /// SB
        for (int i = 0; i < thread2; i++){
            SB[i] = new Threads(segmentB, monitor);
            SB[i].setName("SB " + i);
        }

        /// SC
        for (int i = 0; i < thread3; i++){
            SC[i] = new Threads(segmentC, monitor);
            SC[i].setName("SC " + i);
        }

        /// SD
        for (int i = 0; i < thread4; i++){
            SD[i] = new Threads(segmentD, monitor);
            SD[i].setName("SD " + i);
        }

        /// SE
        for (int i = 0; i < thread5; i++){
            SE[i] = new Threads(segmentE, monitor);
            SE[i].setName("SE " + i);
        }

        /// SF
        for (int i = 0; i < thread6; i++){
            SF[i] = new Threads(segmentF, monitor);
            SF[i].setName("SF " + i);
        }

        // INITIALIZING A THREAD FOR LOGGING
        Log logger = new Log(SA, SB, SC, SD, SE, SF, monitor);
        Thread logThread = new Thread(logger);
        logThread.start();

        // START TASKS FOR EACH THREAD
        /// SA
        for (int i = 0; i < thread1; i++){
            SA[i].start();
        }

        /// SB
        for (int i = 0; i < thread2; i++){
            SB[i].start();
        }

        /// SC
        for (int i = 0; i < thread3; i++){
            SC[i].start();
        }

        /// SD
        for (int i = 0; i < thread4; i++){
            SD[i].start();
        }

        /// SE
        for (int i = 0; i < thread5; i++){
            SE[i].start();
        }

        /// SF
        for (int i = 0; i < thread6; i++){
            SF[i].start();
        }
    // Esperar a que terminen todos los hilos de SA..SF
        try {
            for (Threads t : SA) if (t != null) t.join();
            for (Threads t : SB) if (t != null) t.join();
            for (Threads t : SC) if (t != null) t.join();
            for (Threads t : SD) if (t != null) t.join();
            for (Threads t : SE) if (t != null) t.join();
            for (Threads t : SF) if (t != null) t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // manejar interrupción si es necesario
        }
        // Esperar también al hilo del logger
        try {
            logThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Escribir tiempo total en el log y en consola
        Log.getInstance().writeTotalTimeAndPrint();

        // Mostrar métricas de políticas
        System.out.println("Picked 2: " + policy.getPicked2());
        System.out.println("Picked 3: " + policy.getPicked3());
        System.out.println("Picked 6: " + policy.getPicked6());
        System.out.println("Picked 7: " + policy.getPicked7());
    }
    
}
