package main;
import java.util.Random;
import java.util.List;

public class Policy {

    private final boolean policyTypeEquitative;
    private final Random rand = new Random();
    private static Integer pick2 = 0;
    private static Integer pick3 = 0;

    public Policy(boolean a){
        policyTypeEquitative = a;
    }

    
    public Integer decide(List<Integer> transitions){

        if (transitions == null || transitions.isEmpty()) return null;
        if (transitions.size() == 1) return transitions.get(0);

        boolean has23 = transitions.contains(2) && transitions.contains(3);
        boolean has67 = transitions.contains(6) && transitions.contains(7);

        // probability ahora es para decidir entre el par 2,3 o 6,7
        double probability = rand.nextDouble();

        //elimine la logica del switch

        // analzamos primero si ambos pares de transiciones estan habilitados
        if (has23 && has67){
            boolean pick23 = probability < 0.5;
            if (pick23){
                return pickPair23();
            } else {
                return pickPair67();
            }
        }

        // solo tenemos el par 2-3
        if(has23) return pickPair23();
        // solo tenemos el par 6-7
        if(has67) return pickPair67();

        // si llegamos aca, no tenemos ni el par 2-3 ni el par 6-7
        return transitions.get(rand.nextInt(transitions.size()));
    }

    private Integer pickPair23(){
        double probability = rand.nextDouble();
        if (policyTypeEquitative){
            if(probability>=0.5){
                pick2++;
                return 2;
            } else {
                pick3++;
                return 3;
            }
        }
        else {
            if(probability<= 0.75){
                pick2++;
                return 2;
            } else {
                pick3++;
                return 3;
            }
        }
    }

    private Integer pickPair67(){
        double probability = rand.nextDouble();
        if (policyTypeEquitative){
            if(probability>=0.5){
                return 6;
            } else {
                return 7;
            }
        }
        else {
            if(probability<= 0.8){
                return 6;
            } else {
                return 7;
            }
        }
    }
    public Integer getPicked2(){
        return pick2;
    }
    public Integer getPicked3(){
        return pick3;
    }

}
