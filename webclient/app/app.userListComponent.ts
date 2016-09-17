import { Component, OnInit, Input } from '@angular/core';
import { DataService } from './services/dataservice';
import { User } from './models/user';

@Component({
    selector: 'my-item-component',
    providers: [DataService],
    template: `
        <div *ngFor="let user of myItems | orderBy : '-total_steps' ">
            {{ user.username }} took {{user.total_steps}} steps
        </div>
        `
})

export class MyItemComponent implements OnInit {
    @Input() public myItems: User [];

    constructor(private _dataService: DataService) { }

    ngOnInit() {
        this.getAllItems();
    }

    private updateUsers(): void {
        for(let user of this.myItems){
            this._dataService
                .GetSingle(user.id)
                .subscribe((data:User) => {
                        user.total_steps = data.total_steps
                    },
                    error => console.log(error),
                    () => console.log('Get all Items complete'));
        }
    }

    private getAllItems(): void {
        let self = this;
        this._dataService
            .GetAll()
            .subscribe((data:User[]) => {
                this.myItems = data
                },
                error => console.log(error),
                () => {
                    console.log('Get all Items complete')
                    setInterval(() => { self.updateUsers()},5000)
                });
    }
}