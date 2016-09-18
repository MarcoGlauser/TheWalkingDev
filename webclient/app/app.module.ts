import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpModule } from '@angular/http';
import { AppComponent }   from './app.component';
import { MyItemComponent} from './app.userListComponent'

@NgModule({
    imports:      [ BrowserModule ,HttpModule ],
    declarations: [ AppComponent ,MyItemComponent],
    bootstrap:    [ AppComponent ,MyItemComponent]
})
export class AppModule { }