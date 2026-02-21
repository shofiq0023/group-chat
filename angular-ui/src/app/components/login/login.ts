import {Component, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faRightToBracket} from '@fortawesome/free-solid-svg-icons';
import {Loader} from '../utils/loader/loader';
import {AuthService} from '../../services/auth-service';
import {toast} from 'ngx-sonner';
import {LoginRequest} from '../../model/login-request';
import {ApiResponse} from '../../model/api-response';
import {LoginResponse} from '../../model/login-response';
import {finalize} from 'rxjs';

@Component({
    selector: 'app-login',
    imports: [FormsModule, RouterLink, FaIconComponent, Loader],
    templateUrl: './login.html',
    styleUrl: './login.css',
})
export class Login {
    protected readonly toast = toast;
    public loginIcon: IconDefinition = faRightToBracket;
    public loading = signal(false);

    public userHandle: string = '';
    public password: string = '';

    constructor(private authService: AuthService, private router: Router) {}

    public handleLogin(): void {
        this.loading.set(true);

        const payload: LoginRequest = {
            userHandle: this.userHandle,
            password: this.password,
        };

        this.authService.login(payload)
            .pipe(
                finalize(() => this.loading.set(false))
            )
            .subscribe({
                next: (res: ApiResponse<LoginResponse>) => {
                    if (res.success) {
                        toast.success('Login successful!');
                        this.router.navigate(['/home']);
                    } else {
                        toast.error(res.message);
                    }
                },
                error: err => {
                    console.error(err.message);
                    toast.error('Something went wrong');
                }
            });
    }
}
