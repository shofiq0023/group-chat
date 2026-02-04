import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faRightToBracket, faUserPlus} from '@fortawesome/free-solid-svg-icons';

@Component({
    selector: 'app-signup',
    imports: [
        RouterLink,
        FaIconComponent
    ],
    templateUrl: './signup.html',
    styleUrl: './signup.css',
})
export class Signup {
    public signupIcon: IconDefinition = faUserPlus;
    protected readonly loginIcon = faRightToBracket;
}
