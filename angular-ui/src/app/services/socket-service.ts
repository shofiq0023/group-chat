import {Injectable} from '@angular/core';
import {io, Socket} from 'socket.io-client';
import {GlobalConst} from '../global-constant';
import {Observable} from 'rxjs';
import {NgxSonnerToaster, toast} from 'ngx-sonner';
import {BroadcastMessage} from '../model/broadcast-message';

export interface Data {
    to: string;
    message: string;
    from?: string;
}

@Injectable({
    providedIn: 'root',
})
export class SocketService {
    protected readonly toast = toast;

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
            toast.success('Connected!');
        });

        this.socket.on('connect_error', (error) => {
            console.error('Connection error:', error);
            toast.error('There was a problem connecting...!');
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

    public onGetMessage(): Observable<BroadcastMessage> {
        return new Observable(observer => {
            if (!this.socket) {
                observer.error('Socket not connected');
                return;
            }

            this.socket.on('broadcast_message', (data: BroadcastMessage) => {
                observer.next(data);
            });

            return () => {
                this.socket?.off('broadcast_message');
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

    public sendBroadcastMessage(message: BroadcastMessage): void {
        if (!this.socket?.connected) {
            console.error('Socket not connected');
            return;
        }

        this.socket.emit('broadcast_message', message);
    }
}
