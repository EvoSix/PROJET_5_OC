import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatToolbarModule } from "@angular/material/toolbar";
import { RouterTestingModule } from "@angular/router/testing";
import { expect, jest } from "@jest/globals";

import { AppComponent } from "./app.component";
import { SessionService } from "./services/session.service";
import { Router } from "@angular/router";
import { of } from "rxjs";
import { AuthService } from "./features/auth/services/auth.service";

class MockSessionService {
  $isLogged() {
    return of(false);
  }
  logOut() {
    jest.fn();
  }
}
describe("AppComponent", () => {
  let router: Router;
  let sessionService: MockSessionService;
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [
        AuthService,
        { provide: SessionService, useClass: MockSessionService },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it("should create the app", () => {
    expect(app).toBeTruthy();
  });
  it("Should logout", () => {
    jest.spyOn(sessionService, "logOut");
    app.logout();
    expect(sessionService.logOut).toHaveBeenCalled();
  });
  it("Should be logged", () => {
    jest.spyOn(sessionService, "$isLogged");

    expect(app.$isLogged().subscribe((logged) => expect(logged).toBe(true)));
  });
  it("Should not be logged", () => {
    jest.spyOn(sessionService, "$isLogged");
    expect(app.$isLogged().subscribe((logged) => expect(logged).toBe(false)));
  });
  it("should navigate to home on logout", () => {
    const navigateSpy = jest.spyOn(router, "navigate");

    app.logout();

    expect(navigateSpy).toHaveBeenCalledWith([""]);
  });
});
