import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule, NgIf, NgOptimizedImage} from "@angular/common";
import {LoadingComponent} from "../../shared/components/loading/loading.component";
import {Mail} from "../../shared/models/mail.model";
import {MailService} from "../../shared/services/mail.service";
import {ItemTooltipComponent} from "../../shared/components/tooltips/item-tooltip/item-tooltip.component";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-mail',
  templateUrl: './mail.component.html',
  standalone: true,
  styleUrls: ['./mail.component.scss', '../app.component.scss'],
  imports: [NgOptimizedImage, LoadingComponent, ItemTooltipComponent, NgIf, CommonModule,TableModule, ButtonModule],

})
export class MailComponent implements OnInit{
  unseen: number | null = 0;
  mails: Mail[] = [];
  displayedMails: Mail[] = [];
  selectedMail: Mail = {
    subject: '',
    sender: '',
    message: '',
    created: '',
    seen: false
  };
  currentPage: number = 0;
  rowsPerPage: number = 11;
  constructor(private mailService: MailService, private soundService: SoundService) {}

  ngOnInit(): void {
    this.listMails();
  }

  listMails(): void {
    this.mailService.listMails().subscribe({
      next: (mails: Mail[] | null) => {
        this.mails = mails ? [...mails] : [];
        this.updateDisplayedMails();

      },
      error: (error) => {
        console.error('Error fetching mails', error);
      }
    });
  }
  updateDisplayedMails(): void {
    const startIndex = this.currentPage * this.rowsPerPage;
    this.displayedMails = this.mails.slice(startIndex, startIndex + this.rowsPerPage);
  }

  selectMail(mail: Mail): void {
    if (!mail.seen) {
      mail.seen = true; // Mark the mail as seen when clicked
      this.mailService.updateUnreadCount(this.mails); // Update the unread count
    }
    this.selectedMail = mail;
  }



  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.updateDisplayedMails();
    }
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.rowsPerPage < this.mails.length) {
      this.currentPage++;
      this.updateDisplayedMails();
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
