import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
import { User } from '../models/user';
import { StepDiff } from '../models/step_diff';

@Injectable()
export class DataService {

    private actionUrl: string;
    private headers: Headers;
    private server: string;

    constructor(private _http: Http) {

        this.server = 'http://172.31.5.34:8000/'
        this.actionUrl = this.server + 'users/users/?format=json';

        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Accept', 'application/json');
    }

    public GetAll = (): Observable<User[]> => {
        return this._http.get(this.actionUrl)
            .map((response: Response) => <User[]> response.json().results)
    }

    public GetSingle = (id: number): Observable<User> => {
        let actionUrl = this.server + 'users/users/' + id + '/?format=json';
        return this._http.get(actionUrl)
            .map((response: Response) => <User> response.json())
    }

    public GetLiveStepDiffs = (): Observable<StepDiff[]> => {
        let fiveMinutes = Math.floor(Date.now()/1000) - 60*1;
        return this.getDiffsForTime(fiveMinutes)
    }

    public GetStepDiffsForToday = (): Observable<StepDiff[]> => {
        let day = Math.floor(Date.now()/1000) - 24*60*60;
        return this.getDiffsForTime(day)
    }

    private getDiffsForTime = (time): Observable<StepDiff[]> => {
        let actionUrl = this.server + 'datapoints/step_diffs/?format=json&created_at_gte='+time;
        return this._http.get(actionUrl)
            .map((response: Response) => <StepDiff[]> response.json().results)
    }

    private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}