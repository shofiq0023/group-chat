import {Injectable} from '@angular/core';
import {io, Socket} from 'socket.io-client';
import {GlobalConst} from '../global-constant';
import {Observable} from 'rxjs';

export interface Data {
    to: string;
    message: string;
    from?: string;
}

@Injectable({
    providedIn: 'root',
})
export class SocketService {
    private socket: Socket | null = null;
    private serverUrl = GlobalConst.socketServer; // Update with your server URL

    public connect(username: string): void {
        if (this.socket?.connected) {
            console.warn('Socket connection already connected');
            return;
        }

        this.socket = io(this.serverUrl, {
            query: {username}
        });

        this.socket.on('connect', () => {
            console.log('Connected to server with ID:', this.socket?.id);
        });

        this.socket.on('connect_error', (error) => {
            console.error('Connection error:', error);
        });

        this.socket.on('disconnect', (reason) => {
            console.log('Disconnected:', reason);
        });
    }

    public disconnect(): void {
        if (this.socket) {
            this.socket.disconnect();
            this.socket = null;
        }
    }

    public isConnected(): boolean {
        return this.socket?.connected || false;
    }

    public onUserList(): Observable<string[]> {
        return new Observable(observer => {
            if (!this.socket) {
                observer.error('Socket not connected');
                return;
            }

            this.socket.on('userList', (users: string[]) => {
                observer.next(users);
            });

            return () => {
                this.socket?.off('userList');
            };
        });
    }

    public onGetMessage(): Observable<Data> {
        return new Observable(observer => {
            if (!this.socket) {
                observer.error('Socket not connected');
                return;
            }

            this.socket.on('getMessage', (data: string) => {
                const parsedData: Data = JSON.parse(data);
                observer.next(parsedData);
            });

            return () => {
                this.socket?.off('getMessage');
            };
        });
    }

    public sendMessage(data: Data): void {
        if (!this.socket?.connected) {
            console.error('Socket not connected');
            return;
        }

        const jsonData = JSON.stringify(data);
        this.socket.emit('sendMessage', jsonData);
    }

    public sendBroadcastMessage(message: string): void {
        if (!this.socket?.connected) {
            console.error('Socket not connected');
            return;
        }

        const data = JSON.stringify({ message });
        this.socket.emit('message', data);
    }
}
