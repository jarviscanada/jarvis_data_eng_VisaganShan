import { Component } from '@angular/core';
import { TraderlistComponent } from '../traderlist/traderlist.component';
import { AddTraderModalComponent } from '../add-trader-modal/add-trader-modal.component';
import { MatDialog , MatDialogModule} from '@angular/material/dialog';
import { Trader } from '../trader';
import { TraderlistService } from '../traderlist.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [TraderlistComponent, MatDialogModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})

export class DashboardComponent {
   constructor(private dialog:MatDialog, private traderService:TraderlistService){}

   openDialog(): void {
        const dialogRef = this.dialog.open(AddTraderModalComponent, {
        });

        dialogRef.afterClosed().subscribe((result: any) => {
        if (result)
        this.traderService.addTrader(result);
        });
      
    }


}
