import { Component, OnInit } from '@angular/core';
import { ToastService, ToastMessage } from './toast.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent implements OnInit {
  messages: ToastMessage[] = [];

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.messages$.subscribe(messages => {
      this.messages = messages;
    });
  }

  closeToast(index: number) {
    this.toastService.remove(index);
  }
} 