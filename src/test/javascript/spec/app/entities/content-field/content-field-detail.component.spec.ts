import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CmsGatewayTestModule } from '../../../test.module';
import { ContentFieldDetailComponent } from 'app/entities/content-field/content-field-detail.component';
import { ContentField } from 'app/shared/model/content-field.model';

describe('Component Tests', () => {
  describe('ContentField Management Detail Component', () => {
    let comp: ContentFieldDetailComponent;
    let fixture: ComponentFixture<ContentFieldDetailComponent>;
    const route = ({ data: of({ contentField: new ContentField('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CmsGatewayTestModule],
        declarations: [ContentFieldDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ContentFieldDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContentFieldDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contentField on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contentField).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
