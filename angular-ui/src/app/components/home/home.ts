import {Component, OnDestroy, OnInit} from '@angular/core';
import {Data, SocketService} from '../../services/socket-service';
import {Subject, takeUntil} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faBullhorn, faCommentDots, faLinkSlash, faPaperPlane, faUserGroup} from '@fortawesome/free-solid-svg-icons';
import {NgxSonnerToaster, toast} from 'ngx-sonner';

interface Message {
    from: string;
    to: string;
    message: string;
    timestamp: Date;
}

@Component({
    selector: 'app-home',
    imports: [CommonModule, FormsModule, FaIconComponent, NgxSonnerToaster],
    templateUrl: './home.html',
    styleUrl: './home.css',
})
export class Home implements OnInit, OnDestroy {
    protected readonly toast = toast;

    public disconnectIcon: IconDefinition = faLinkSlash;
    public groupIcon: IconDefinition = faUserGroup;
    public broadcastIcon: IconDefinition = faBullhorn;
    public sendIcon: IconDefinition = faPaperPlane;
    public chatIcon: IconDefinition = faCommentDots;

    public username = '';
    public isConnected = false;
    public users: string[] = [];
    public messages: Message[] = [];
    public selectedUser = '';
    public messageText = '';

    private destroy$ = new Subject<void>();

    constructor(private socketService: SocketService) {
    }

    ngOnInit(): void {
        // Check if already connected (e.g., from route state or localStorage)
        const savedUsername = localStorage.getItem('username');
        if (savedUsername) {
            this.username = savedUsername;
            this.connectToServer();
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
        this.socketService.disconnect();
    }

    public connectToServer(): void {
        if (!this.username.trim()) {
            toast.warning('Please enter a username');
            return;
        }

        localStorage.setItem('username', this.username);
        this.socketService.connect(this.username);
        this.isConnected = true;
        toast.success('Connected!');

        // Subscribe to user list updates
        this.socketService.onUserList()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (users) => {
                    this.users = users.filter(u => u !== this.username);
                    console.log('Users updated:', users);
                },
                error: (err) => console.error('Error receiving user list:', err)
            });

        // Subscribe to incoming messages
        this.socketService.onGetMessage()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (data: Data) => {
                    const message: Message = {
                        from: data.from || 'Unknown',
                        to: data.to,
                        message: data.message,
                        timestamp: new Date()
                    };
                    this.messages.push(message);
                    console.log('Message received:', message);
                },
                error: (err) => console.error('Error receiving message:', err)
            });
    }

    public disconnect(): void {
        this.socketService.disconnect();
        this.isConnected = false;
        this.users = [];
        this.messages = [];
        localStorage.removeItem('username');
        toast.warning('Disconnected');
    }

    public sendMessage(): void {
        if (!this.messageText.trim()) {
            toast.warning('Please enter a message');
            return;
        }

        if (!this.selectedUser) {
            // Broadcast message
            this.socketService.sendBroadcastMessage(this.messageText);
        } else {
            // Direct message
            const data: Data = {
                to: this.selectedUser,
                message: this.messageText,
                from: this.username
            };
            this.socketService.sendMessage(data);

            // Add to local messages
            this.messages.push({
                from: this.username,
                to: this.selectedUser,
                message: this.messageText,
                timestamp: new Date()
            });
        }

        this.messageText = '';
    }

    public selectUser(user: string): void {
        this.selectedUser = user;
    }
}
