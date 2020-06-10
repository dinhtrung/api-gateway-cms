import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CmsGatewayTestModule } from '../../../test.module';
import { ContentTypeDetailComponent } from 'app/entities/content-type/content-type-detail.component';
import { ContentType } from 'app/shared/model/content-type.model';

describe('Component Tests', () => {
  describe('ContentType Management Detail Component', () => {
    let comp: ContentTypeDetailComponent;
    let fixture: ComponentFixture<ContentTypeDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ contentType: new ContentType('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CmsGatewayTestModule],
        declarations: [ContentTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ContentTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContentTypeDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load contentType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contentType).toEqual(jasmine.objectContaining({ id: '123' }));
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
