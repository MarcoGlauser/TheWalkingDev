import { Component, OnInit, Input } from '@angular/core';
import { DataService } from './services/dataservice';
import { User } from './models/user';
import {StepDiff} from "./models/step_diff";

@Component({
    selector: 'my-item-component',
    providers: [DataService],
    template: `
        <div class="container">
            <div class="row">
                <div id="generalStats" class="col-md-6" >
                    <div class="container-fluid">
                        <div class="row">
                            <div class="center-block text-center bs-callout bs-callout-primary"><h4>{{totalSteps}}</h4> <span class="subdesc">Total steps</span></div>
                        </div>
                        <div class="row">
                            <div class="center-block text-center bs-callout bs-callout-primary"><h4>{{dailySteps}}</h4> <span class="subdesc">steps Today</span></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-1">
                </div>
                
                <div id="ranking" class="col-md-5 bs-callout bs-callout-info col-md-offset-9">
                    <div *ngFor="let user of myItems | orderBy : '-total_steps' " >
                        <div class="text-left subdesc">
                            {{ user.username }}
                        </div>
                        <div class="progress">
                            <div class="progress-bar progress-bar-success  " [ngClass]="{'active': !activeIds.indexOf(user.id),'progress-bar-striped':!activeIds.indexOf(user.id)}" role="progressbar" [ngStyle]="{'width': user.step_percentage+'%'}" >
                                {{ user.total_steps }} steps
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="center-block text-center bs-callout bs-callout-success" *ngIf="activeUsers && activeUsers.length > 0">
                    
                    <div class="text-center pull-left person" *ngFor="let user of activeUsers | orderBy : '-total_steps' ">
                        <img class="person_icon" src="../images/person_icon.png">
                        <div>{{ user.username }}</div>
                    </div>
                    <div class="clearfix"></div>
                    <span *ngIf="activeUsers.length > 1">Are awesome and walking around</span>
                    <span *ngIf="activeUsers.length < 2">Is being mindful of his health and walking around</span>
                    
                </div>
                <div class="text-center bs-callout bs-callout-danger" *ngIf="!activeUsers || activeUsers.length == 0">Everyone is sitting on their asses. Check back later.</div>
            </div>
        </div>
        `
})

export class MyItemComponent implements OnInit {
    public myItems: User [] = [];
    public activeUsers: User [] = [];
    public activeIds:number[] = [];
    public totalSteps:number = 0;
    public dailySteps: number = 0;

    constructor(private _dataService: DataService) { }

    ngOnInit() {
        this.getAllItems();
        setInterval(() => {this.getActiveUsers();}, 2000);
        setInterval(() => {this.getTodaysSteps();}, 2000);
    }

    private getTodaysSteps(): void {
        let self = this;
        this._dataService
            .GetStepDiffsForToday()
            .subscribe((diffs:StepDiff[]) => {
                    let sum = 0;
                    for (let diff of diffs) {
                        sum += diff.number_of_steps
                    }
                    self.dailySteps = sum;
                },
                error => console.log(error),
                () => {
                    console.log('Get all Items complete')
                });
    }

    private updateUsers(): void {
        let self = this;
        for(let user of this.myItems){
            this._dataService
                .GetSingle(user.id)
                .subscribe((data:User) => {
                        let delta = data.total_steps - user.total_steps
                        self.totalSteps += delta;
                        user.total_steps = data.total_steps
                    },
                    error => console.log(error),
                    () => console.log('Get all Items complete'));
        }
        for(let user of this.myItems) {
            this.calculateStepPercentage(user)
        }
    }

    private calculateStepPercentage(user:User): void{
        user.step_percentage = Math.round(user.total_steps/this.totalSteps*100)
        console.log(user.step_percentage)
    }

    private getAllItems(): void {
        let self = this;
        this._dataService
            .GetAll()
            .subscribe((data:User[]) => {
                this.myItems = data
                for(let user of data){
                    self.totalSteps += user.total_steps
                }
                },
                error => console.log(error),
                () => {
                    console.log('Get all Items complete')
                    setInterval(() => { self.updateUsers()},2000)
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