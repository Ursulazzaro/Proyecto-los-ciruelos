import { Injectable, inject } from '@angular/core';
import {
  Auth,
  UserCredential,
  GoogleAuthProvider,
  signInWithPopup,
  authState,
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  User,
  sendPasswordResetEmail,
  EmailAuthProvider,
  updatePassword,
  reauthenticateWithCredential,
  sendEmailVerification,
  fetchSignInMethodsForEmail,
} from '@angular/fire/auth';
import { map, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { applyActionCode, getAuth, getRedirectResult, onAuthStateChanged, signInWithRedirect, signOut } from 'firebase/auth';


export interface Credential {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private auth: Auth = inject(Auth);
  private router: Router = inject(Router);
  private user: User | null = null;
  readonly authState$: Observable<User | null> = authState(this.auth);

  constructor(private fb: FormBuilder, private toastrService: ToastrService) {
    onAuthStateChanged(this.auth, (user) => {
      this.user = user; // Actualiza el estado del usuario
    });
  }

  // Método para verificar si el usuario está autenticado
  isAuthenticated(): boolean {
    const user = this.auth.currentUser;
    return !!user && user.emailVerified;
  }

   // Método para hacer logout
    logout(): Promise<void> {
      return signOut(this.auth)
        .then(() => {
          this.router.navigate(['/home'], { queryParams: {}, replaceUrl: true });
        })
        .catch((error) => {
          console.error('Error al cerrar sesión', error);
          this.toastrService.error('Hubo un error al cerrar sesión', 'Error');
          throw error;
        });
    }
      
  async updatePassword(newPassword: string): Promise<void> {
    const user = this.auth.currentUser;
    if (user) {
      return updatePassword(user, newPassword); 
    } else {
      throw new Error('No hay usuario autenticado');
    }
  }
  
  registerWithEmailAndPassword(credential: Credential): Promise<UserCredential> {
    return createUserWithEmailAndPassword(this.auth, credential.email, credential.password)
      .then(async (userCredential) => {
        if (userCredential.user) {
          await this.enviarEmailVerification(userCredential); // Esperamos que firebase mande el mail
        }
        return userCredential;
      })  
      .catch(error => {
        throw error;
      });
  }
  
  async verifyEmailWithCode(oobCode: string): Promise<void> {
    const auth = getAuth();
    try {
      await applyActionCode(auth, oobCode); 
      console.log('Correo electrónico verificado');
    } catch (error) {
      console.error('Error al verificar el correo electrónico:', error);
      throw error; 
    }
  }

  loginWithEmailAndPassword(credential: Credential): Promise<UserCredential> {
    return signInWithEmailAndPassword(this.auth, credential.email, credential.password)
      .then(async (userCredential) => {
        // Verificar si el correo está verificado
      if (!userCredential.user?.emailVerified) {
        // Cerrar sesión si no verificó el mail
        await signOut(this.auth);

        // Lanzar error personalizado
        const error: any = new Error('Correo no verificado');
        error.code = 'auth/email-not-verified';

        throw error;
      }
        this.toastrService.success("Bienvenido de nuevo! Nos alegra verte otra vez.", "Exito");
  
        return userCredential;
      })
      .catch((error) => {
        // Dejar que FirebaseErrorService maneje el error
        throw error;
      });
  }

  async getToken(): Promise<string> {
    const user = this.auth.currentUser;
    if (user) {
      return await user.getIdToken();
    }
    throw new Error('No hay usuario conectado');
  }
  
  async loginWithGoogleProvider(): Promise<UserCredential> {
    const provider = new GoogleAuthProvider();
  
    try {
     return await signInWithPopup(this.auth, provider);
      
    } catch (error: any) {
      return error;
    }
  }  

async enviarEmailVerification(userCredential: UserCredential): Promise<void> {
  const user = userCredential.user;

  if (!user) return;

  try {
    const actionCodeSettings = {
      url: 'http://localhost:4200/verificar-correo',
      handleCodeInApp: true
    };

    await sendEmailVerification(user, actionCodeSettings);

    this.toastrService.info(
      "Correo de verificación enviado. Revisá tu email.",
      "Verificación requerida"
    );

  } catch (error) {
    console.error('Error enviando mail:', error);
    this.toastrService.error(
      "No se pudo enviar el correo de verificación.",
      "Error"
    );
  }
}

  async resetPassword(email: string): Promise<void> {
    await sendPasswordResetEmail(this.auth, email);
  }

  getUserEmail(): Observable<string | null> {
    return this.authState$.pipe(
      map((user) => user?.email || null) // Si el usuario está autenticado, devuelve el email; si no, devuelve null
    );
  }

  getUsuario(): Observable<User | null> {
    return this.authState$; // Devuelve el estado actual del usuario autenticado
  }
  
}
