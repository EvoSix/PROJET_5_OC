import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';

class MockAuthService {
  register = (req: RegisterRequest): Observable<void> => of(void 0);
}

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;
  let authService: MockAuthService;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [{ provide: AuthService, useClass: MockAuthService }],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

      fixture = TestBed.createComponent(RegisterComponent);
      component = fixture.componentInstance;
      authService = TestBed.inject(AuthService);
      router = TestBed.inject(Router);
      fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it("should redirect to /login after successful register", () => {
    const navigateSpy = jest.spyOn(router, "navigate");

    component.form.controls["firstName"].setValue("John");
    component.form.controls["lastName"].setValue("Doe");
    component.form.controls["email"].setValue("john@example.com");
    component.form.controls["password"].setValue("securePass");

    component.submit();

    expect(navigateSpy).toHaveBeenCalledWith(["/login"]);
  });

  it("should set onError to true if register fails", () => {
    jest
      .spyOn(authService, "register")
      .mockReturnValue(throwError(() => new Error("Register failed")));

    component.form.controls["firstName"].setValue("Error");
    component.form.controls["lastName"].setValue("User");
    component.form.controls["email"].setValue("error@example.com");
    component.form.controls["password"].setValue("123456");

    component.submit();

    expect(component.onError).toBe(true);
  });

  it("should set onError to true if fields are empty", () => {
    jest
      .spyOn(authService, "register")
      .mockReturnValue(throwError(() => new Error("Error: Fields are empty")));

    component.form.controls["firstName"].setValue("");
    component.form.controls["lastName"].setValue("");
    component.form.controls["email"].setValue("");
    component.form.controls["password"].setValue("");

    component.submit();

    expect(component.onError).toBe(true);
  });
});

