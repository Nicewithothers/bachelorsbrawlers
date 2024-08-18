import {Component, OnInit} from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { LoadingComponent } from "../../shared/components/loading/loading.component";
import { YearBook } from '../../shared/models/yearbook.model';
import { YearbookService } from '../../shared/services/yearbook.service';
import { HeroService } from '../../shared/services/hero.service';
import { SoundService } from '../../shared/services/sound.service';

@Component({
  selector: 'app-yearbook',
  standalone: true,
  templateUrl: './yearbook.component.html',
  styleUrls: ['./yearbook.component.scss', '../app.component.scss'],
  imports: [RouterLink, NgOptimizedImage, LoadingComponent],
})
export class YearbookComponent implements OnInit{
  yearbooks: YearBook[] = [];
  displayedStudents: YearBook[] = [];
  selectedStudent: any;
  currentPage: number = 0;
  rowsPerPage: number = 11;
  constructor(private yearBookService: YearbookService,
    private heroService: HeroService,
    private soundService: SoundService
  ) {}

  ngOnInit(): void {
    this.getStudents();
  }

  getStudents(): void {
    this.yearBookService.getAll().subscribe({
      next: (res: YearBook[] | null) => {
        this.yearbooks = res ? [...res] : [];
        this.getNameAndPicture();
      },
      error: (error) => {
        console.error('Error fetching mails', error);
      }
    });
  }
  
  getNameAndPicture(): void {
    if(this.yearbooks.length > 0) {
      let ids = new Set(this.yearbooks.map(i => i.heroId))
        this.heroService.getHeroesByIds(Array.from(ids)).subscribe(res => {
          if(res) {
            res.forEach(e => {
              let index = this.yearbooks.findIndex(y => y.name = e.name)
              this.yearbooks[index].picture = e.picture;
              this.updateDisplayedMails();
            })
          }
        })
      }
  }

  updateDisplayedMails(): void {
    const startIndex = this.currentPage * this.rowsPerPage;
    this.displayedStudents = this.yearbooks.slice(startIndex, startIndex + this.rowsPerPage);
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.rowsPerPage < this.yearbooks.length) {
      this.currentPage++;
    }
  }

  onButtonClicked(soundPath: string): void {
    this.soundService.playSound(soundPath);
  }
}
