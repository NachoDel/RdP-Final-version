package main;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Threads extends Thread {
    private final List<Integer> transitions;
    private final Monitor monitor;

    public Threads(List<Integer> transitions, Monitor monitor) {
        this.transitions = transitions;
        this.monitor = monitor;
    }
    // Jitter pequeño para aumentar la probabilidad de choques “reales” sin bloquear
    private static void jitter(int minMs, int maxMs) {
        int d = ThreadLocalRandom.current().nextInt(minMs, maxMs + 1);
        try { Thread.sleep(d); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }

    // Si la transición pertenece a un par en conflicto, la alineamos con su “rival”
    private void rendezvousIfNeeded(int t) {
        // Si querés ser más estricto, podés chequear que esté habilitada:
        // if (monitor.getRdp().isEnabled(t) != 1) return;
        if (t == 2 || t == 3) {
            Sync.rendezvous23();
        } else if (t == 6 || t == 7) {
            Sync.rendezvous67();
        }
    }


    @Override
    public void run() {
        while (true) {
            for (int transition : transitions) {
            //    while (!monitor.fireTransition(transition))
                // Pequeño jitter para romper sincronías “malas” y provocar más solapes
                jitter(3, 12);
                // Alinear si es un par conflictivo (T2–T3, T6–T7)
                rendezvousIfNeeded(transition);
                while (!monitor.fireTransition(transition))
                {
                    if (monitor.areInvariantsCompleted()) {
                        System.out.println("Hilo manejando las transiciones " + transitions + " con invariantes completadas.");
                        return;
                    }
                }
            }

        }
    }
}
