package br.unitins.topicos1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "produto_id") // Cria uma chave estrangeira que precisa ter exatamente o mesmo valor do id da classe pai que contem o restante das informações
public class Processador extends Produto {

    private String soquete;
    private String pistas;
    private String bloqueado;
    private String compatibilidadeChipset;
    private String canaisMemoria;
    private String capacidadeMaxMemoria;
    private String pontenciaBase;
    private String potenciaMaxima;
    private String frequenciaBase;
    private String frequenciaMaxima;
    private String tamanhoCacheL3;
    private String tamanhoCacheL2;
    private String numNucleosFisicos;
    private String numThreads;
    private String velMaxMemoria;
    private String conteudoEmbalagem;

    /**
     * @return String return the soquete
     */
    public String getSoquete() {
        return soquete;
    }

    /**
     * @param soquete the soquete to set
     */
    public void setSoquete(String soquete) {
        this.soquete = soquete;
    }

    /**
     * @return String return the pistas
     */
    public String getPistas() {
        return pistas;
    }

    /**
     * @param pistas the pistas to set
     */
    public void setPistas(String pistas) {
        this.pistas = pistas;
    }

    /**
     * @return String return the bloqueado
     */
    public String getBloqueado() {
        return bloqueado;
    }

    /**
     * @param bloqueado the bloqueado to set
     */
    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    /**
     * @return String return the compatibilidadeChipset
     */
    public String getCompatibilidadeChipset() {
        return compatibilidadeChipset;
    }

    /**
     * @param compatibilidadeChipset the compatibilidadeChipset to set
     */
    public void setCompatibilidadeChipset(String compatibilidadeChipset) {
        this.compatibilidadeChipset = compatibilidadeChipset;
    }

    /**
     * @return String return the canaisMemoria
     */
    public String getCanaisMemoria() {
        return canaisMemoria;
    }

    /**
     * @param canaisMemoria the canaisMemoria to set
     */
    public void setCanaisMemoria(String canaisMemoria) {
        this.canaisMemoria = canaisMemoria;
    }

    /**
     * @return String return the capacidadeMaxMemoria
     */
    public String getCapacidadeMaxMemoria() {
        return capacidadeMaxMemoria;
    }

    /**
     * @param capacidadeMaxMemoria the capacidadeMaxMemoria to set
     */
    public void setCapacidadeMaxMemoria(String capacidadeMaxMemoria) {
        this.capacidadeMaxMemoria = capacidadeMaxMemoria;
    }

    /**
     * @return String return the pontenciaBase
     */
    public String getPontenciaBase() {
        return pontenciaBase;
    }

    /**
     * @param pontenciaBase the pontenciaBase to set
     */
    public void setPontenciaBase(String pontenciaBase) {
        this.pontenciaBase = pontenciaBase;
    }

    /**
     * @return String return the potenciaMaxima
     */
    public String getPotenciaMaxima() {
        return potenciaMaxima;
    }

    /**
     * @param potenciaMaxima the potenciaMaxima to set
     */
    public void setPotenciaMaxima(String potenciaMaxima) {
        this.potenciaMaxima = potenciaMaxima;
    }

    /**
     * @return String return the frequenciaBase
     */
    public String getFrequenciaBase() {
        return frequenciaBase;
    }

    /**
     * @param frequenciaBase the frequenciaBase to set
     */
    public void setFrequenciaBase(String frequenciaBase) {
        this.frequenciaBase = frequenciaBase;
    }

    /**
     * @return String return the frequenciaMaxima
     */
    public String getFrequenciaMaxima() {
        return frequenciaMaxima;
    }

    /**
     * @param frequenciaMaxima the frequenciaMaxima to set
     */
    public void setFrequenciaMaxima(String frequenciaMaxima) {
        this.frequenciaMaxima = frequenciaMaxima;
    }

    /**
     * @return String return the tamanhoCacheL3
     */
    public String getTamanhoCacheL3() {
        return tamanhoCacheL3;
    }

    /**
     * @param tamanhoCacheL3 the tamanhoCacheL3 to set
     */
    public void setTamanhoCacheL3(String tamanhoCacheL3) {
        this.tamanhoCacheL3 = tamanhoCacheL3;
    }

    /**
     * @return String return the tamanhoCacheL2
     */
    public String getTamanhoCacheL2() {
        return tamanhoCacheL2;
    }

    /**
     * @param tamanhoCacheL2 the tamanhoCacheL2 to set
     */
    public void setTamanhoCacheL2(String tamanhoCacheL2) {
        this.tamanhoCacheL2 = tamanhoCacheL2;
    }

    /**
     * @return String return the numNucleosFisicos
     */
    public String getNumNucleosFisicos() {
        return numNucleosFisicos;
    }

    /**
     * @param numNucleosFisicos the numNucleosFisicos to set
     */
    public void setNumNucleosFisicos(String numNucleosFisicos) {
        this.numNucleosFisicos = numNucleosFisicos;
    }

    /**
     * @return String return the numThreads
     */
    public String getNumThreads() {
        return numThreads;
    }

    /**
     * @param numThreads the numThreads to set
     */
    public void setNumThreads(String numThreads) {
        this.numThreads = numThreads;
    }

    /**
     * @return String return the velMaxMemoria
     */
    public String getVelMaxMemoria() {
        return velMaxMemoria;
    }

    /**
     * @param velMaxMemoria the velMaxMemoria to set
     */
    public void setVelMaxMemoria(String velMaxMemoria) {
        this.velMaxMemoria = velMaxMemoria;
    }

    /**
     * @return String return the conteudoEmbalagem
     */
    public String getConteudoEmbalagem() {
        return conteudoEmbalagem;
    }

    /**
     * @param conteudoEmbalagem the conteudoEmbalagem to set
     */
    public void setConteudoEmbalagem(String conteudoEmbalagem) {
        this.conteudoEmbalagem = conteudoEmbalagem;
    }

}
