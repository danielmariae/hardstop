import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LocalStorageService } from './local-storage.service';
import { ItemCarrinho } from '../models/itemcarrinho.model';

@Injectable({
  providedIn: 'root'
})
export class CarrinhoService {

  private carrinhoSubject = new BehaviorSubject<ItemCarrinho[]>([]);
  carrinho$ = this.carrinhoSubject.asObservable();

  constructor(private localStorageService: LocalStorageService) {
    const carrinhoArmazenado = localStorageService.getItem('carrinho') || [];
    this.carrinhoSubject.next(carrinhoArmazenado);
  }

  adicionar(produto: ItemCarrinho): void {
    const carrinhoAtual = this.carrinhoSubject.value;
    //console.log(this.carrinhoSubject.value);
    const itemExistente = carrinhoAtual.find(item => item.id === produto.id);
    // console.log(consulta);
    
    if (itemExistente) {
      if (itemExistente.quantidade >= itemExistente.quantidadeLimite) {
        console.log("Quantidade no carrinho: ",itemExistente.quantidade,"\nQuantidade disponível no estoque: ",itemExistente.quantidadeLimite)
        console.error("Limite atingido!")
        return;
        } else {
        console.log("Quantidade no carrinho: ",itemExistente.quantidade,"\nQuantidade disponível no estoque: ",itemExistente.quantidadeLimite)
        // console.log(itemExistente);
        itemExistente.quantidade += produto.quantidade || 1;
      }

    } else {
      carrinhoAtual.push({ ...produto });
    }

    this.carrinhoSubject.next(carrinhoAtual);
    this.atualizarArmazenamentoLocal();
  }

  removerTudo(): void {
    this.localStorageService.removeItem('carrinho');
  }

  remover(item: ItemCarrinho): void {
    const carrinhoAtual = this.carrinhoSubject.value;
    const carrinhoAtualizado = carrinhoAtual.filter(itemCarrinho => itemCarrinho !== item);
    console.log("Quantidade no carrinho: ",item.quantidade,"\nQuantidade disponível no estoque: ",item.quantidadeLimite)

    this.carrinhoSubject.next(carrinhoAtualizado);
    this.atualizarArmazenamentoLocal();
  }

  obter(): ItemCarrinho[] {
    return this.carrinhoSubject.value;
  }

  private atualizarArmazenamentoLocal(): void {
    localStorage.setItem('carrinho', JSON.stringify(this.carrinhoSubject.value));
  }
}
