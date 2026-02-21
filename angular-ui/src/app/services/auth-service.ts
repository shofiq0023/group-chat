import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse} from '../model/api-response';
import {SignupResponse} from '../model/signup-response';
import {SignupRequest} from '../model/signup-request';
import {ApiList} from '../api-list';
import {LoginResponse} from '../model/login-response';
import {LoginRequest} from '../model/login-request';
import {LocalStorageService} from './local-storage-service';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private readonly USERNAME: string = 'username';
    private readonly USER_ID: string = 'userId';
    private readonly TOKEN: string = 'token';
    private readonly LOGGED_IN: string = 'loggedIn';

    constructor(private http: HttpClient, private storage: LocalStorageService) {
    }

    public signup(payload: SignupRequest): Observable<ApiResponse<SignupResponse>> {
        return this.http.post<ApiResponse<SignupResponse>>(ApiList.API_BASE + ApiList.API_AUTH + ApiList.SIGNUP, payload);
    }

    public login(payload: LoginRequest): Observable<ApiResponse<LoginResponse>> {
        return this.http.post<ApiResponse<LoginResponse>>(ApiList.API_BASE + ApiList.API_AUTH + ApiList.LOGIN, payload);
    }

    public storeCredentials(response: ApiResponse<LoginResponse>): void {
        this.storage.store(this.LOGGED_IN, 'true');
        this.storage.store(this.USER_ID, response.data?.userId);
        this.storage.store(this.USERNAME, response.data?.username);
        this.storage.store(this.TOKEN, response.data?.token);
    }

    public removeCredentials(): void {
        this.storage.delete(this.LOGGED_IN);
        this.storage.delete(this.USER_ID);
        this.storage.delete(this.USERNAME);
        this.storage.delete(this.TOKEN);
    }

}
