import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type ToastType = 'success' | 'error';
export interface ToastMessage {
  text: string;
  type: ToastType;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private messagesSubject = new BehaviorSubject<ToastMessage[]>([]);
  messages$ = this.messagesSubject.asObservable();

  show(text: string, type: ToastType = 'success', duration = 3000) {
    const messages = this.messagesSubject.value;
    this.messagesSubject.next([...messages, { text, type }]);
    setTimeout(() => this.remove(0), duration);
  }

  remove(index: number) {
    const messages = [...this.messagesSubject.value];
    messages.splice(index, 1);
    this.messagesSubject.next(messages);
  }
} 