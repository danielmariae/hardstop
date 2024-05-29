package br.unitins.topicos1.model.produto;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "produto_id") // Cria uma chave estrangeira que precisa ter exatamente o mesmo valor do id da classe pai que contem o restante das informações
public class PlacaMae extends Produto {
    private String cpu;
    private String chipset;
    private String memoria;
    private String bios;
    private String grafico;
    private String lan;
    private String slots;
    private String armazenamento;

    /**
     * @return String return the cpu
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * @return String return the chipset
     */
    public String getChipset() {
        return chipset;
    }

    /**
     * @param chipset the chipset to set
     */
    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    /**
     * @return String return the memoria
     */
    public String getMemoria() {
        return memoria;
    }

    /**
     * @param memoria the memoria to set
     */
    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    /**
     * @return String return the bios
     */
    public String getBios() {
        return bios;
    }

    /**
     * @param bios the bios to set
     */
    public void setBios(String bios) {
        this.bios = bios;
    }

    /**
     * @return String return the grafico
     */
    public String getGrafico() {
        return grafico;
    }

    /**
     * @param grafico the grafico to set
     */
    public void setGrafico(String grafico) {
        this.grafico = grafico;
    }

    /**
     * @return String return the lan
     */
    public String getLan() {
        return lan;
    }

    /**
     * @param lan the lan to set
     */
    public void setLan(String lan) {
        this.lan = lan;
    }

    /**
     * @return String return the slots
     */
    public String getSlots() {
        return slots;
    }

    /**
     * @param slots the slots to set
     */
    public void setSlots(String slots) {
        this.slots = slots;
    }

    /**
     * @return String return the armazenamento
     */
    public String getArmazenamento() {
        return armazenamento;
    }

    /**
     * @param armazenamento the armazenamento to set
     */
    public void setArmazenamento(String armazenamento) {
        this.armazenamento = armazenamento;
    }

}
