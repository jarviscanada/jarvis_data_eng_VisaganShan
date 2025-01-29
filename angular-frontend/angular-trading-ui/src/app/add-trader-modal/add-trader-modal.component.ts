import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { ReactiveFormsModule, Validator } from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { MatDialogRef } from '@angular/material/dialog';



@Component({
  selector: 'app-add-trader-modal',
  standalone: true,
  imports: [
     MatFormFieldModule,
     MatInputModule,
     ReactiveFormsModule,
    ],
  templateUrl: './add-trader-modal.component.html',
  styleUrl: './add-trader-modal.component.scss'
})
export class AddTraderModalComponent {
    traderForm = new FormGroup({
        firstName: new FormControl<string>(''),
        lastName: new FormControl<string>(''),
        email: new FormControl<string>(''),
        country: new FormControl<string>(''),
        dob: new FormControl<string>('')
    });

    constructor(private dialogRef:MatDialogRef<AddTraderModalComponent>){}
    onSubmit(): void{
        
        console.log(this.traderForm.value)
        if(this.traderForm.value){
            this.dialogRef.close(this.traderForm.value);
        }
    }

    closeDialog(): void {
        this.dialogRef.close();
    }
}
