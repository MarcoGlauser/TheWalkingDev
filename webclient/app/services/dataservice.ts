import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
import { User } from '../models/user';

@Injectable()
export class DataService {

    private actionUrl: string;
    private headers: Headers;
    private server: string;

    constructor(private _http: Http) {

        this.server = 'http://localhost:8000/'
        this.actionUrl = this.server + 'users/users/?format=json';

        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Accept', 'application/json');
    }

    public GetAll = (): Observable<User[]> => {
        return this._http.get(this.actionUrl)
            .map((response: Response) => <User[]>response.json().results)
    }

    public GetSingle = (id: number): Observable<User> => {
        let actionUrl = this.server + 'users/users/' + id + '/?format=json';
        return this._http.get(actionUrl)
            .map((response: Response) => <User>response.json())
    }

    private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}