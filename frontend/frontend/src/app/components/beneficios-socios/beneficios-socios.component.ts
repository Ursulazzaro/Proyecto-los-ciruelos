import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-beneficios-socios',
  templateUrl: './beneficios-socios.component.html',
  styleUrl: './beneficios-socios.component.css'
})
export class BeneficiosSociosComponent implements OnInit{
  isLoggedIn: boolean = false;


  constructor(
    private router: Router,
    private authService: AuthService,
    private toastrService: ToastrService
  ) {
    this.authService.authState$.subscribe(user => {
      this.isLoggedIn = !!user; // Si hay un usuario, isLoggedIn es true
    });
  }
  ngOnInit(): void {
    
  }

  iniciarAsociacion(): void {
    if (this.isLoggedIn) {
      this.router.navigate(['/ticket'], { 
        queryParams: { 
          asociacion: true 
        } 
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
  

  testimonials = [
    { name: 'Ana M.', text: 'Los beneficios son increíbles, especialmente los descuentos.' },
    { name: 'Luis G.', text: 'Reservar en horarios pico es súper fácil ahora. No vuelvo a jugar sin la membresía.' },
    { name: 'Fernando T.', text: 'El descuento en alquiler de equipos me ha ayudado mucho. Gran servicio.' },
  ];

}
