/// <reference types="cypress" />

import getFormattedDate from "../utils/Datemanagment";

describe("Delete spec", () => {
    it("Deletes session", () => {

        cy.visit('/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: "yoga@studio.com",
                firstName: 'Admin',
                lastName: 'Admin',
                admin: true
            }
        });

        let sessions = [
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
            }
        ]

        cy.intercept("GET", '/api/session', {
            body: sessions,
        });
        
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
        ]

        cy.intercept('GET', '/api/teacher', teachers);

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

        cy.get('.mat-raised-button').should("be.enabled");
        cy.url().should('include', '/sessions');
        
        let session = 
            {
                id: 1,
                name: "session 1",
                description: "Session blockers 1",
                date: "2018-02-28 05:35:00",
                teacher_id: 1,
                users: [],
                createdAt: getFormattedDate(-10),
                updatedAt: getFormattedDate(3),
            }           
        

        const teacher = teachers.find((teacher) => teacher.id == session.teacher_id);

        cy.intercept("GET", `/api/session/${session.id}`, session);
        cy.intercept("GET", `/api/teacher/${session.teacher_id}`, teacher);
        cy.contains('Detail').click();

        cy.url().should("include", `/sessions/detail/${session.id}`);

        
        cy.intercept("DELETE", `/api/session/${session.id}`, {});

     
        const sessionsList = sessions.filter(s => s.id !== session.id);

        cy.intercept("GET", "/api/session", {
            body: sessionsList,
        })

        cy.contains("Delete").click();

        cy.url().should("include", "/sessions")

    })
})