import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterTestingModule } from "@angular/router/testing";
import { expect } from "@jest/globals";
import { SessionService } from "src/app/services/session.service";

import { LoginComponent } from "./login.component";
import { Router } from "@angular/router";
import { LoginRequest } from "../../interfaces/loginRequest.interface";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { of, throwError } from "rxjs";
import { AuthService } from "../../services/auth.service";

class MockAuthService {
  login(loginRequest: LoginRequest) {
    return of({
      token: "fake-token",
      id: 1,
      username: "user",
      firstName: "Will",
      lastName: "Smith",
      type: "user",
    } as SessionInformation);
  }
}
describe("Composant: Login", () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let authService: MockAuthService;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        { provide: AuthService, useClass: MockAuthService },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should redirect to /sessions after successful login", () => {
    const navigateSpy = jest.spyOn(router, "navigate");

    component.form.controls["email"].setValue("test@example.com");
    component.form.controls["password"].setValue("password123");

    component.submit();

    expect(navigateSpy).toHaveBeenCalledWith(["/sessions"]);
  });

  it("should set onError to true if login fails", () => {
    jest
      .spyOn(authService, "login")
      .mockReturnValue(throwError(() => new Error("Login failed")));

    component.form.controls["email"].setValue("wrong@example.com");
    component.form.controls["password"].setValue("wrongpassword");

    component.submit();

    expect(component.onError).toBe(true);
  });

  it("should set onError to true if login fails", () => {
    jest
      .spyOn(authService, "login")
      .mockReturnValue(throwError(() => new Error("Error: Fields are empty")));

    component.form.controls["email"].setValue("");
    component.form.controls["password"].setValue("");

    component.submit();

    expect(component.onError).toBe(true);
  });
});
