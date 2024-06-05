/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.unitins.topicos1.repository;

import br.unitins.topicos1.model.pagamento.FormaDePagamento;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author 07863820154
 */

@ApplicationScoped
public class FormaDePagamentoRepository implements PanacheRepository<FormaDePagamento> {

}
