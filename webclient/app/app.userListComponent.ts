import { Component, OnInit } from '@angular/core';
import { DataService } from './services/dataservice';
import { User } from './models/user';

@Component({
    selector: 'my-item-component',
    providers: [DataService],
    template: '<div *ngFor="let user of myItems">{{ user.username }}</div>',
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
            .subscribe((data:User[]) => this.myItems = data,
                error => console.log(error),
                () => console.log('Get all Items complete'));
    }
}