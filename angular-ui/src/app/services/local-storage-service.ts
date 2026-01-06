import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
    public store(key: string, value: string): void {
        localStorage.setItem(key, JSON.stringify(value));
    }

    public get(key: string): string | null {
        return localStorage.getItem(key);
    }

    public delete(key: string): void {
        localStorage.removeItem(key);
    }
}
