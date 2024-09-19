import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { of, Observable, BehaviorSubject } from 'rxjs';
import * as tradersList from '../assets/trader-list.json';

@Injectable({
  providedIn: 'root'
})
export class TraderlistService {
    private traders: Trader[] = (tradersList as any).default;
    private tradersSubject = new BehaviorSubject<Trader[]>(this.traders);
  constructor() {}

  getTraderList(): Observable<Trader[]>{
    return of((this.traders as any));
  }

  deleteTrader(id: number) {
    this.traders = this.traders.filter((trader:any) => trader.id !== id);
    }

    addTrader(trader:any): void{
        this.traders.push({
            key: (this.traders.length+1).toString(),
            id: this.traders.length+1,
            firstName: trader.firstName,
            lastName: trader.lastName,
            dob: trader.dob,
            country: trader.country,
            email: trader.email,
            actions: "<button (click)='deleteTrader'>Delete Trader</button>",
            amount: 0
        });
        console.log(this.traders);
    }
}
