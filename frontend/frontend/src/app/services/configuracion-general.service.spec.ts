import { TestBed } from '@angular/core/testing';

import { ConfiguracionGeneralService } from './configuracion-general.service';

describe('ConfiguracionGeneralService', () => {
  let service: ConfiguracionGeneralService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfiguracionGeneralService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
