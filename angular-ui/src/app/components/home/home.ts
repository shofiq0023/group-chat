import {ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SocketService} from '../../services/socket-service';
import {Subject, takeUntil} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {FaIconComponent, IconDefinition} from '@fortawesome/angular-fontawesome';
import {faBullhorn, faCommentDots, faLinkSlash, faPaperPlane, faUserGroup} from '@fortawesome/free-solid-svg-icons';
import {toast} from 'ngx-sonner';
import {BroadcastMessage} from '../../model/broadcast-message';

interface Message {
    from: string;
    to: string;
    message: string;
    timestamp: Date;
}

@Component({
    selector: 'app-home',
    imports: [CommonModule, FormsModule, FaIconComponent],
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
    public messages: BroadcastMessage[] = [];
    public selectedUser = '';
    public messageText = '';

    @ViewChild("messageList") private messageListContainer!: ElementRef;

    private destroy$ = new Subject<void>();

    constructor(private socketService: SocketService, private cdr: ChangeDetectorRef) {
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

    private scrollToBottom(): void {
        const el = this.messageListContainer.nativeElement;
        el.scrollTop = el.scrollHeight;
    }

    public connectToServer(): void {
        if (!this.username.trim()) {
            toast.warning('Please enter a username');
            return;
        }

        localStorage.setItem('username', this.username.toLowerCase());
        this.socketService.connect(this.username.toLowerCase());
        this.isConnected = true;

        // Subscribe to incoming messages
        this.socketService.onGetMessage()
            .pipe(takeUntil(this.destroy$))
            .subscribe({
                next: (data: BroadcastMessage) => {
                    const message: BroadcastMessage = {
                        fromUsername: data.fromUsername || 'unknown',
                        message: data.message,
                        timestamp: data.timestamp,
                    };
                    this.messages.push(message);
                    this.cdr.detectChanges();
                    this.scrollToBottom();
                },
                error: (err) => console.error('Error receiving message:', err)
            });
    }

    public disconnect(): void {
        this.socketService.disconnect();
        this.isConnected = false;
        this.messages = [];
        localStorage.removeItem('username');
        toast.warning('Disconnected');
    }

    public sendMessage(): void {
        if (!this.messageText.trim()) {
            toast.warning('Please enter a message');
            return;
        }

        const message: BroadcastMessage = {
            fromUsername: this.username.toLowerCase(),
            message: this.messageText,
            timestamp: new Date()
        }

        this.socketService.sendBroadcastMessage(message);
        this.messageText = '';
    }
}
