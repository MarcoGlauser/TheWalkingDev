import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpModule } from '@angular/http';
import { AppComponent }   from './app.component';
import { MyItemComponent} from './app.userListComponent'
import {OrderByPipe} from "./app.orderby";


@NgModule({
    imports:      [ BrowserModule ,HttpModule ],
    declarations: [ AppComponent ,MyItemComponent, OrderByPipe],
    bootstrap:    [ AppComponent ,MyItemComponent]
})
export class AppModule { }