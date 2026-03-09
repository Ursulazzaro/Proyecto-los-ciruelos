import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { applyActionCode } from 'firebase/auth'; 

@Component({
  selector: 'app-verificar-correo',
  templateUrl: './verificar-correo.component.html',
  styleUrls: ['./verificar-correo.component.scss']
})
export class VerificarCorreoComponent implements OnInit {


  constructor(
    private authService: AuthService, 
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const oobCode = params['oobCode'];
      if (oobCode) {
        this.authService.verifyEmailWithCode(oobCode).then(() => {
          console.log('Correo verificado con éxito');
          this.router.navigate(['/login']);
        }).catch(error => {
          console.error('Error en la verificación del correo:', error);
        });
      } else {
        console.log('Código de verificación no proporcionado.');
      }
    });
  }
  
  
  

  goToHome(): void {
    this.router.navigate(['/login']);  
  }
}
