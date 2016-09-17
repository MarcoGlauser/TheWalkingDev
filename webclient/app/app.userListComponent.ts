import { Component, OnInit, Input } from '@angular/core';
import { DataService } from './services/dataservice';
import { User } from './models/user';
import {StepDiff} from "./models/step_diff";

@Component({
    selector: 'my-item-component',
    providers: [DataService],
    template: `
        <div *ngFor="let user of myItems | orderBy : '-total_steps' ">
            {{ user.username }} took {{user.total_steps}} steps
        </div>
        <div *ngIf="activeUsers && activeUsers.length > 0">
            <div *ngFor="let user of activeUsers | orderBy : '-total_steps' ">
            {{ user.username }},
            </div>
            Are currently Walking around
        </div>
        `
})

export class MyItemComponent implements OnInit {
    public myItems: User [] = [];
    public activeUsers: User [] = [];
    public activeIds:number[] = [];

    constructor(private _dataService: DataService) { }

    ngOnInit() {
        this.getAllItems();
        setInterval(() => {this.getActiveUsers();}, 3000);

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

    private getActiveUsers() {
        let self = this;

        this._dataService
            .GetLiveStepDiffs()
            .subscribe((diffs:StepDiff[]) => {
                let new_ids = [];
                for (let diff of diffs) {
                    new_ids.push(diff.user)
                    if (self.activeIds.indexOf(diff.user) == -1) {
                        self.activeIds.push(diff.user)
                        this._dataService
                            .GetSingle(diff.user)
                            .subscribe((data:User) => {
                                    self.activeUsers.push(data)
                                },
                                error => console.log(error),
                                () => console.log('Get all Items complete'));
                    }
                }
                for (let user of self.activeUsers){
                    if(new_ids.indexOf(user.id) == -1){
                        let index = self.activeUsers.indexOf(user)
                        self.activeUsers.splice(index, 1);
                        index = self.activeIds.indexOf(user.id)
                        self.activeIds.splice(index, 1);
                    }
                }
            },
            error => console.log(error),
            () => {
                console.log('Get all Items complete')
            });
    }
}