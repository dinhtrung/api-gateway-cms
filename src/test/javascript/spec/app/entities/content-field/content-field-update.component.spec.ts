import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CmsGatewayTestModule } from '../../../test.module';
import { ContentFieldUpdateComponent } from 'app/entities/content-field/content-field-update.component';
import { ContentFieldService } from 'app/entities/content-field/content-field.service';
import { ContentField } from 'app/shared/model/content-field.model';

describe('Component Tests', () => {
  describe('ContentField Management Update Component', () => {
    let comp: ContentFieldUpdateComponent;
    let fixture: ComponentFixture<ContentFieldUpdateComponent>;
    let service: ContentFieldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CmsGatewayTestModule],
        declarations: [ContentFieldUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ContentFieldUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContentFieldUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContentFieldService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContentField('123');
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
        const entity = new ContentField();
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
