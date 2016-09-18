import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpModule } from '@angular/http';
import { MyItemComponent} from './app.userListComponent'
import {OrderByPipe} from "./app.orderby";


@NgModule({
    imports:      [ BrowserModule ,HttpModule ],
    declarations: [ MyItemComponent, OrderByPipe],
    bootstrap:    [ MyItemComponent]
})
export class AppModule { }