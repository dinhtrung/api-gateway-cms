import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CmsGatewayTestModule } from '../../../test.module';
import { DomainDetailComponent } from 'app/entities/domain/domain-detail.component';
import { Domain } from 'app/shared/model/domain.model';

describe('Component Tests', () => {
  describe('Domain Management Detail Component', () => {
    let comp: DomainDetailComponent;
    let fixture: ComponentFixture<DomainDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ domain: new Domain('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CmsGatewayTestModule],
        declarations: [DomainDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DomainDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DomainDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load domain on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.domain).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
