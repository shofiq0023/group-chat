import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {LocalStorageService} from '../../services/local-storage-service';
import {Router} from '@angular/router';
import {GlobalConst} from '../../global-constant';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
    public username: string = '';

    constructor(private storageService: LocalStorageService, private router: Router) { }

    public performLogin(): void {
        this.setUser();
        this.router.navigate(['/home']);
    }

    private setUser(): void {
        this.storageService.store(GlobalConst.USERNAME_KEY, this.username);
        this.storageService.store(GlobalConst.LOGIN_KEY, true.toString());
    }
}
