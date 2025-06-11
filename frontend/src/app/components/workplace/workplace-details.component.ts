import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-workplace-details',
  templateUrl: './workplace-details.component.html',
  styleUrls: ['./workplace-details.component.scss']
})
export class WorkplaceDetailsComponent implements OnInit {
  workplaceId!: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.workplaceId = this.route.snapshot.paramMap.get('id')!;
  }
} 