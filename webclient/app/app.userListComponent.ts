import { Component, OnInit } from '@angular/core';
import { DataService } from './services/dataservice';
import { User } from './models/user';

@Component({
    selector: 'my-item-component',
    providers: [DataService],
    template: '<div *ngFor="let user of myItems">{{ user.username }} took {{user.total_steps}} steps</div>'
})

export class MyItemComponent implements OnInit {
    public myItems: User [];

    constructor(private _dataService: DataService) { }

    ngOnInit() {
        this.getAllItems();
    }

    private getAllItems(): void {
        this._dataService
            .GetAll()
            .subscribe((data:User[]) => {
                data.sort((a:User,b:User) =>{
                    return a.total_steps - b.total_steps
                })
                data.reverse()
                this.myItems = data
                },
                error => console.log(error),
                () => console.log('Get all Items complete'));
    }
}