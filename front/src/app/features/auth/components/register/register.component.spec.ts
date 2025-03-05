import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { expect } from "@jest/globals";

import { RegisterComponent } from "./register.component";
import { AuthService } from "../../services/auth.service";
import { RegisterRequest } from "../../interfaces/registerRequest.interface";
import { Observable, of, throwError } from "rxjs";
import { Router } from "@angular/router";
class MockAuthService {
  register(registerRequest: RegisterRequest): Observable<void> {
    return of(undefined);
  }
}
describe("RegisterComponent", () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: MockAuthService;
  let router: Router;
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
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
  it("should redirect to /login after successful register", () => {
    const navigateSpy = jest.spyOn(router, "navigate");
    const authServiceSpy = jest.spyOn(authService, "register");
    component.form.controls["email"].setValue("wrong@example.com");
    component.form.controls["firstName"].setValue("wrongFirstName");
    component.form.controls["lastName"].setValue("wrongLastName");
    component.form.controls["password"].setValue("wrongPassword");
    component.submit();
    expect(authServiceSpy).toHaveBeenCalledTimes(1);
    expect(component.onError).toBe(false);
    expect(navigateSpy).toHaveBeenCalledWith(["/login"]);
  });
  it("should set onError to true if register fails", () => {
    jest
      .spyOn(authService, "register")
      .mockReturnValue(throwError(() => new Error("Register failed")));
    const navigateSpy = jest.spyOn(router, "navigate");
    component.submit();

    expect(navigateSpy).not.toHaveBeenCalledWith();
    expect(component.onError).toBe(true); // Vérifie que onError passe à true
  });
});
