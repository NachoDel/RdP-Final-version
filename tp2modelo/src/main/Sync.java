package main;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeoutException;

public final class Sync {
    // Barreras para pares en conflicto
    public static final CyclicBarrier B23 = new CyclicBarrier(2); // T2 vs T3
    public static final CyclicBarrier B67 = new CyclicBarrier(2); // T6 vs T7

    private Sync() {}

    // Pequeños helpers de rendezvous con timeout corto (evita deadlocks si el “rival” no llega)
    public static void rendezvous23() {
        await(B23);
    }
    public static void rendezvous67() {
        await(B67);
    }

    private static void await(CyclicBarrier b) {
        try {
            // Espera breve para “alinear” a los dos hilos; si no llegan ambos, seguimos sin bloquear el sistema
            b.await(50, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Nadie rompió la barrera; simplemente no se alinearon a tiempo. Continuamos sin más.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            // Si se rompió por una cancelación/interrupción concurrente, la reiniciamos para próximos usos
            b.reset();
        }
    }
}
