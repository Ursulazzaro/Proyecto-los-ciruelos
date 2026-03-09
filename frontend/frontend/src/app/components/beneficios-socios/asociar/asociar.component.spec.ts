import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsociarComponent } from './asociar.component';

describe('AsociarComponent', () => {
  let component: AsociarComponent;
  let fixture: ComponentFixture<AsociarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AsociarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AsociarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
