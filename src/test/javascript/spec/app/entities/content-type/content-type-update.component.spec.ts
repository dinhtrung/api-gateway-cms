import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CmsGatewayTestModule } from '../../../test.module';
import { ContentTypeUpdateComponent } from 'app/entities/content-type/content-type-update.component';
import { ContentTypeService } from 'app/entities/content-type/content-type.service';
import { ContentType } from 'app/shared/model/content-type.model';

describe('Component Tests', () => {
  describe('ContentType Management Update Component', () => {
    let comp: ContentTypeUpdateComponent;
    let fixture: ComponentFixture<ContentTypeUpdateComponent>;
    let service: ContentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CmsGatewayTestModule],
        declarations: [ContentTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ContentTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContentTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContentTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContentType('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContentType();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
