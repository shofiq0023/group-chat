import {Component} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faUserPlus} from '@fortawesome/free-solid-svg-icons';
import {AuthService} from '../../services/auth-service';
import {SignupRequest} from '../../model/signup-request';
import {FormsModule} from '@angular/forms';
import {SignupResponse} from '../../model/signup-response';
import {toast} from 'ngx-sonner';
import {ApiResponse} from '../../model/api-response';
import {Loader} from '../utils/loader/loader';

@Component({
    selector: 'app-signup',
    imports: [
        RouterLink,
        FaIconComponent,
        FormsModule,
        Loader
    ],
    templateUrl: './signup.html',
    styleUrl: './signup.css',
})
export class Signup {
    protected readonly toast = toast;
    public readonly signupIcon: IconDefinition = faUserPlus;

    public loading: boolean = false;
    public passwordMismatch: boolean = false;

    public username: string = '';
    public fullName: string = '';
    public email: string = '';
    public password: string = '';
    public confirmPassword: string = '';

    constructor(private authService: AuthService, private router: Router) {
    }

    public handleSignup(): void {
        if (this.password != this.confirmPassword) {
            this.passwordMismatch = true;
            toast.error('Passwords do not match');
            return;
        }
        this.passwordMismatch = false;

        this.loading = true;
        const payload: SignupRequest = {
            username: this.username,
            fullName: this.fullName,
            email: this.email,
            password: this.password
        }

        this.authService.signup(payload).subscribe({
            next: (res: ApiResponse<SignupResponse>) => {
                this.loading = false;
                if (!res.success) {
                    const msg = res.message;
                    toast.error("Error!, " + msg);
                    console.error(msg);
                } else {
                    toast.success("Signup successful!")
                    toast.success("Now login using the same credentials");
                    this.router.navigate(['/login']);
                }

            },
            error: (err) => {
                this.loading = false;
                toast.error("Something went wrong!");
                console.error(err.stack);
            }
        });
    }
}
