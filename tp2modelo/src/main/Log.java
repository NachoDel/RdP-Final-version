package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Runnable {
    int count;
    Threads[] SA;
    Threads[] SB;
    Threads[] SC;
    Threads[] SD;
    Threads[] SE;
    Threads[] SF;
    final Monitor monitor;
    File file;
    File file1;
    final Long INITIAL_TIME = System.currentTimeMillis();
    static Log instance;

    public Log(Threads[] SA, Threads[] SB, Threads[] SC, Threads[] SD, Threads[] SE, Threads[] SF, Monitor monitor){
        this.SA = SA;
        this.SB = SB;
        this.SC = SC;
        this.SD = SD;
        this.SE = SE;
        this.SF = SF;
        this.monitor = monitor;
        count = 0;

        File directory = new File("./Logs");
        if(!directory.exists()){
            if (!directory.mkdirs()) {
                System.out.println("Error al crear directorio");
            }
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String fileName = "log-" + dateFormat.format(new Date()) + ".txt";
            file = new File(directory, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            System.out.println("Problema al crear el archivo de LOG.");
        }

        File directory1 = new File("./Secuencia");
        if(!directory1.exists()){
            if (!directory1.mkdirs()) {
                System.out.println("Error al crear directorio");
            } //if it aint broken dont fix it
        }
        try {
            String fileName1 = ("Secuencia") + ".txt";
            file1 = new File(directory1, fileName1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
        }
        catch (IOException e) {
            System.out.println("Problema al crear el archivo de LOG.");
        }
        instance = this;
    }

    static Log getInstance(){
        return instance;
    }

    @Override
    public void run() {
        while (!monitor.areInvariantsCompleted()){
            try {
                writeFile();
                this.count++;
                synchronized (monitor){
                    monitor.wait(500);
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.count++;
        writeFile();
        logWriteSequence();
        writeSequenceOnly();
    }


    private void writeFile(){
        try {
            FileWriter writer = new FileWriter(file, true);
            try {
                Long currentTime = System.currentTimeMillis();
                //QUE ES LO QUE SE PONE EN LUGAR DE "monitor.getRdp().getFiredCounter()[3] + monitor.getRdp().getFiredCounter()[4]"?
                writer.write("Iteración: " + count + " tiempo: " + (currentTime - INITIAL_TIME) + "ms\n");
                writer.write("Clientes ingresados y en sala de espera: "+ monitor.getRdp().getFiredCounter()[1] +"\n");
                writer.write("Clientes atendidos: "+ (monitor.getRdp().getFiredCounter()[5] + monitor.getRdp().getFiredCounter()[4]) + "\n");
                writer.write("Atendidos por la Mesa 1: "+ (monitor.getRdp().getFiredCounter()[5] + " , Atendidos por la Mesa 2: " + monitor.getRdp().getFiredCounter()[4]) +"\n");
                writer.write("Clientes con la reserva confirmada y pagada: "+ (monitor.getRdp().getFiredCounter()[10]) +"\n");
                writer.write("Clientes con la reserva cancelado: "+ (monitor.getRdp().getFiredCounter()[8]) +"\n");
                writer.write("Clientes retirados: "+ monitor.getRdp().getFiredCounter()[11] +"\n");
                //writer.write(monitor.getBalanceCount() +"\n");

                for (Threads thread: SA){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: SB){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: SC){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: SD){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: SE){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: SF){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                writer.write("\n\n");
            }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logWriteSequence(){
        try {
            FileWriter writer = new FileWriter(file, true);
            try {
                writer.write("Secuencia: "+ monitor.getRdp().getSequence() +"\n");
                
                writer.write("\n\n");
            }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSequenceOnly(){
        try {
            FileWriter writer = new FileWriter(file1, false);
            try {
                writer.write(monitor.getRdp().getSequence()+"\n");
                }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeTotalTimeAndPrint(){
        long totalMs = System.currentTimeMillis() - INITIAL_TIME;
        // Escribir en el archivo de LOG (append)
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write("Tiempo total de ejecución: " + totalMs + " ms\n");
            writer.write("\n\n");
        } catch (IOException e) {
            System.out.println("Problema al escribir el tiempo total en el archivo de LOG.");
        }

        // Mostrar por terminal
        System.out.println("Tiempo total de ejecución: " + totalMs + " ms");
        }

}
