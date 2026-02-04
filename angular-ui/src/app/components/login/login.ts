import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {LocalStorageService} from '../../services/local-storage-service';
import {Router, RouterLink} from '@angular/router';
import {GlobalConst} from '../../global-constant';
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faCommentDots, faRightToBracket} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
    imports: [FormsModule, RouterLink, FaIconComponent],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
    public loginIcon: IconDefinition = faRightToBracket;
}
