import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
import { User } from '../models/user';

@Injectable()
export class DataService {

    private actionUrl: string;
    private headers: Headers;

    constructor(private _http: Http) {

        this.actionUrl = 'http://localhost:8000/' + 'users/users/?format=json';

        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Accept', 'application/json');
    }

    public GetAll = (): Observable<User[]> => {
        return this._http.get(this.actionUrl)
            .map((response: Response) => <User[]>response.json().results)
    }

    public GetSingle = (id: number): Observable<User> => {
        return this._http.get(this.actionUrl + id)
            .map((response: Response) => <User>response.json())
    }

    private handleError(error: Response) {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}