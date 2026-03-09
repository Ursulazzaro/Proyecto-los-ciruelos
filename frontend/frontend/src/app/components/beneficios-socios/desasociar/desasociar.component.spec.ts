import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DesasociarComponent } from './desasociar.component';

describe('DesasociarComponent', () => {
  let component: DesasociarComponent;
  let fixture: ComponentFixture<DesasociarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DesasociarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DesasociarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
