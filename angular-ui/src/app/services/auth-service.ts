import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse} from '../model/api-response';
import {SignupResponse} from '../model/signup-response';
import {SignupRequest} from '../model/signup-request';
import {ApiList} from '../api-list';
import {LoginResponse} from '../model/login-response';
import {LoginRequest} from '../model/login-request';

@Injectable({
    providedIn: 'root',
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    public signup(payload: SignupRequest): Observable<ApiResponse<SignupResponse>> {
        return this.http.post<ApiResponse<SignupResponse>>(ApiList.API_BASE + ApiList.API_AUTH + ApiList.SIGNUP, payload);
    }

    public login(payload: LoginRequest): Observable<ApiResponse<LoginResponse>> {
        return this.http.post<ApiResponse<LoginResponse>>(ApiList.API_BASE + ApiList.API_AUTH + ApiList.LOGIN, payload);
    }

}
