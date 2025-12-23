public interface MonitorInterface {

    //Por ser interfaz, debe incluir solo los metodos publicos de Monitor.java
    
    /**
     * Intenta disparar una transición específica de la red de Petri
     * @param transition el número de la transición a disparar
     * @return true si la transición se disparó exitosamente, false si los invariantes ya se completaron
     */
    Boolean fireTransition(Integer transition);
    
    /**
     * Verifica si todos los invariantes han sido completados
     * @return true si los invariantes están completos, false en caso contrario
     */
    boolean areInvariantsCompleted();
    
    /**
     * Obtiene la red de Petri asociada al monitor
     * @return la instancia de Rdp
     */
    Rdp getRdp();
}
