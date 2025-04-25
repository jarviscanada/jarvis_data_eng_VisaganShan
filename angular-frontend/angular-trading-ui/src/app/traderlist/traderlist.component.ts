import { Component } from '@angular/core';
import { Trader } from '../trader';
import { MatTableModule } from '@angular/material/table';
import { TraderlistService } from '../traderlist.service';

@Component({
  selector: 'app-traderlist',
  standalone: true,
  imports: [MatTableModule],
  templateUrl: './traderlist.component.html',
  styleUrl: './traderlist.component.scss'
})
export class TraderlistComponent {
    traders: Trader[] = [];
    displayedColumns: string[] = ['firstName', 'lastName', 'email', 'dob', 'country', 'actions'];

    constructor(private _traderlistService:TraderlistService){
        this.getTraders();
        }

    getTraders(){
        this._traderlistService.getTraderList().subscribe(
            (data: Trader[]) => {
                this.traders = data;
                console.log(this.traders)
            }, (error) => {
                console.error('Error fetching trader data:', error);
            }
        );
        }

    deleteTrader(id: number) {
        try{
            this._traderlistService.deleteTrader(id)
        } catch (err) {
            console.log(err);
        }
    }
}
