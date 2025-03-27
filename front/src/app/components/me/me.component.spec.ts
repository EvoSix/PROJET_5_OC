import { HttpClientModule } from "@angular/common/http";

import {
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
  tick,
} from "@angular/core/testing";

import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";

import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { SessionService } from "src/app/services/session.service";
import { expect } from "@jest/globals";
import { MeComponent } from "./me.component";
import { UserService } from "src/app/services/user.service";
import { of } from "rxjs";
import { User } from "src/app/interfaces/user.interface";
import { Router } from "@angular/router";
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from "@angular/platform-browser/animations";
import { RouterTestingModule } from "@angular/router/testing";

class MockUserService {
  getById(id: string) {
    return of({
      id: 1,
      email: "John.doe@example.com",
      firstName: "John",
      lastName: "Doe",
      admin: true,
      password: "secret",
      createdAt: new Date(),
      updatedAt: new Date(),
    } as User);
  }
  delete(id: string) {
    return of({});
  }
}


describe("MeComponent", () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let router: Router;
  let matSnackBar: MatSnackBar;
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },

    logOut: jest.fn(),

  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        NoopAnimationsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,

      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useClass: MockUserService },
      ],

    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should get user", fakeAsync(() => {
    jest.spyOn(userService, "getById");

    component.ngOnInit();
    tick();
    flush();
    expect(component.user).toBeDefined();
    expect(component.user?.email).toBe("John.doe@example.com");
  }));

  it("sould back", () => {
    jest.spyOn(window.history, "back");
    component.back();

    expect(window.history.back).toHaveBeenCalled();
  });

  it("sould delte user", fakeAsync(() => {
    jest.spyOn(userService, "delete");
    const snackBarSpy = jest.spyOn(matSnackBar, "open");
    const sessionLogoutSpy = jest.spyOn(mockSessionService, "logOut");
    const navigateSpy = jest.spyOn(router, "navigate");

    component.delete();
    tick();
    flush();
    expect(userService.delete).toHaveBeenCalledWith("1");
    expect(snackBarSpy).toHaveBeenCalledWith(
      "Your account has been deleted !",
      "Close",
      { duration: 3000 }
    ); 
    expect(sessionLogoutSpy).toHaveBeenCalled();
  }));
});
