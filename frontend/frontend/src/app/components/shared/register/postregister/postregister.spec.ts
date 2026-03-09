import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { PostRegisterComponent } from './postregister.component';
import { Router } from '@angular/router';

describe('PostRegistroComponent', () => {
  let component: PostRegisterComponent;
  let fixture: ComponentFixture<PostRegisterComponent>;
  let routerMock = { navigate: jasmine.createSpy('navigate') };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [PostRegisterComponent],
      providers: [{ provide: Router, useValue: routerMock }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate on submit with valid form', () => {
    component.form.patchValue({ phones: '123456', playerCategory: 'Principiante' });
    component.submit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('should not navigate on submit with invalid form', () => {
    component.submit();
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
