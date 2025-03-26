/// <reference types="cypress" />

import getFormattedDate from "../utils/Datemanagment";

describe("Update spec", () => {
  it("Updates session", () => {
    cy.visit("/login");

    cy.intercept("POST", "/api/auth/login", {
      body: {
        id: 1,
        username: "yoga@studio.com",
        firstName: "Admin",
        lastName: "Admin",
        admin: true,
      },
    });

    const sessions = [
      {
        id: 1,
        name: "session 1",
        description: "Session blockers 1",
        date: "2018-02-28 05:35:00",
        teacher_id: 1,
        users: [],
        createdAt: getFormattedDate(-10),
        updatedAt: getFormattedDate(3),
      },
      {
        id: 2,
        name: "Session",
        description: "Session blockers 2",
        date: "2024-09-01 02:00:00",
        teacher_id: 2,
        users: [],
        createdAt: getFormattedDate(),
        updatedAt: getFormattedDate(),
      },
    ];

    cy.intercept("GET", "/api/session", {
      body: sessions,
    }).as("sessions");

    const teachers = [
      {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2025-03-02 17:39:57",
        updatedAt: "2025-03-02 17:39:57",
      },
      {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-02 17:39:57",
        updatedAt: "2025-03-02 17:39:57",
      },
    ];

    cy.intercept("GET", "/api/teacher", teachers);

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type(
      `${"test!12345"}{enter}{enter}`
    );

    cy.get(".mat-raised-button").should("be.enabled");
    cy.url().should("include", "/sessions");

    let session = {
      id: 1,
      name: "session 1",
      description: "Session blockers 1",
      date: "2018-02-28 05:35:00",
      teacher_id: 1,
      users: [],
      createdAt: getFormattedDate(-10),
      updatedAt: getFormattedDate(3),
    };

    const teacher = teachers.find(
      (teacher) => teacher.id == session.teacher_id
    );

    cy.intercept("GET", "/api/session/1", session);

    cy.intercept("GET", `/api/teacher/${session.teacher_id}`, teacher);

    cy.contains("Edit").click();

    cy.url().should("include", "/sessions/update/1");

    const teacherIndex = 0;
    const selectedTeacher = teachers[teacherIndex];

    cy.get("input[formControlName=name]").clear();
    cy.get("input[formControlName=date]").clear();
    cy.get("textarea[formControlName=description]").clear();

    const sessionForm = {
      id: sessions[sessions.length - 1].id,
      name: "Une session mise à jour.",
      date: "2023-09-16",
      teacher: `${selectedTeacher.firstName} ${selectedTeacher.lastName}`,
      description: "Une description mise à jour.",
    };

    cy.get("input[formControlName=name]").type(sessionForm.name);
    cy.get("input[formControlName=date]").type(sessionForm.date);

    const teacher_input = cy.get("mat-select[formControlName=teacher_id]");
    teacher_input.click();
    teacher_input.get("mat-option");
    teacher_input.contains(sessionForm.teacher).click();

    cy.get("textarea[formControlName=description]").type(
      sessionForm.description
    );

    session = {
      id: sessionForm.id,
      name: sessionForm.name,
      description: sessionForm.description,
      date: sessionForm.date,
      teacher_id: selectedTeacher.id,
      users: [],
      createdAt: session.createdAt,
      updatedAt: new Date().toDateString(),
    };
    cy.intercept("PUT", "/api/session/1", {
      body: session,
    });

    let sessionIndex = sessions.findIndex((obj) => obj.id == 1);
    const updatedSessions = [
      ...sessions.slice(0, sessionIndex),
      session,
      ...sessions.slice(sessionIndex + 1),
    ];

    cy.intercept("GET", "/api/session", {
      body: updatedSessions,
    });

    cy.get("button[type=submit]").click();
  });

  it("No update, empty form fields, disabled submit button", () => {
    cy.visit("/login");

    cy.intercept("POST", "/api/auth/login", {
      body: {
        id: 1,
        username: "yoga@studio.com",
        firstName: "Admin",
        lastName: "Admin",
        admin: true,
      },
    });

    const sessions = [
      {
        id: 1,
        name: "session 1",
        description: "Session blockers 1",
        date: "2018-02-28 05:35:00",
        teacher_id: 1,
        users: [],
        createdAt: getFormattedDate(-10),
        updatedAt: getFormattedDate(3),
      },
      {
        id: 2,
        name: "Session",
        description: "Session blockers 2",
        date: "2024-09-01 02:00:00",
        teacher_id: 2,
        users: [],
        createdAt: getFormattedDate(),
        updatedAt: getFormattedDate(),
      },
    ];

    cy.intercept("GET", "/api/session", {
      body: sessions,
    }).as("sessions");

    const teachers = [
      {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2025-03-02 17:39:57",
        updatedAt: "2025-03-02 17:39:57",
      },
      {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-02 17:39:57",
        updatedAt: "2025-03-02 17:39:57",
      },
    ];

    cy.intercept("GET", "/api/teacher", teachers);

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type(
      `${"test!12345"}{enter}{enter}`
    );

    cy.get(".mat-raised-button").should("be.enabled");
    cy.url().should("include", "/sessions");
    let session = {
      id: 1,
      name: "session 1",
      description: "Session blockers 1",
      date: "2018-02-28 05:35:00",
      teacher_id: 1,
      users: [],
      createdAt: getFormattedDate(-10),
      updatedAt: getFormattedDate(3),
    };

    const teacher = teachers.find(
      (teacher) => teacher.id == session.teacher_id
    );

    cy.intercept("GET", "/api/session/1", session);

    cy.intercept("GET", `/api/teacher/${session.teacher_id}`, teacher);

    cy.contains("Edit").click();

    cy.url().should("include", "/sessions/update/1");

    cy.get("input[formControlName=name]").clear();
    cy.get("input[formControlName=date]").clear();
    cy.get("textarea[formControlName=description]").clear();

    const sessionForm = {
      name: "",
      date: "",
      teacher: "",
      description: "",
    };

    cy.get("input[formControlName=name]").should(
      "have.value",
      sessionForm.name
    );
    cy.get("input[formControlName=date]").should(
      "have.value",
      sessionForm.date
    );
    cy.get("textarea[formControlName=description]").should(
      "have.value",
      sessionForm.description
    );

    cy.get("button[type=submit]").should("be.disabled");
  });
});
