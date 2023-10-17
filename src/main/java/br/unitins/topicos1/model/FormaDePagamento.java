package br.unitins.topicos1.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "forma_de_pagamento")
public class FormaDePagamento extends DefaultEntity {

    @Enumerated(EnumType.ORDINAL)
    private ModalidadePagamento modalidade;

    Double valorPago;

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public ModalidadePagamento getModalidade() {
        return modalidade;
    }

    public void setModalidade(ModalidadePagamento modalidade) {
        this.modalidade = modalidade;
    }
    
}
