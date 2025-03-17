/// <reference types="cypress" />

import getFormattedDate from "../utils/Datemanagment";

describe("Detail spec", () => {
    it("Participate to session", () => {
        // Login mock
        cy.visit('/login');

        const user = {
            id: 4,
            username: "test@test.com",
            firstName: 'test',
            lastName: 'test',
            admin: false
        }
        cy.intercept('POST', '/api/auth/login', {
            body: user
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

        cy.get('input[formControlName=email]').type(user.username);
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

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

        cy.intercept("GET", "/api/session/1", {
            body:session
        });

        cy.intercept("GET", `/api/teacher/${session.id}`, teacher)

        const sessionCard =  cy.get('.items > :nth-child(1)');
        sessionCard.contains('Detail').click();
        cy.contains('Participate').should("be.visible");

        cy.intercept("POST", `/api/session/1/participate/${user.id}`, {})
        cy.intercept("GET", "/api/session/1", {
            id: 1,
            name: "session 1",
            description: "Session blockers 1",
            date: "2018-02-28 05:35:00",
            teacher_id: 1,
            users: [user.id],
            createdAt: getFormattedDate(-10),
            updatedAt: getFormattedDate(3),            
        })
        cy.contains("Participate").click();


    })

    it("Should cancel participation", () => {
        // Login mock
        cy.visit('/login');

        const user = {
            id: 4,
            username: "test@test.com",
            firstName: 'test',
            lastName: 'test',
            admin: false
        }
        cy.intercept('POST', '/api/auth/login', {
            body: user
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

        cy.get('input[formControlName=email]').type("toto3@toto.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

        cy.get('.mat-raised-button').should("be.enabled");
        cy.url().should('include', '/sessions');

        let session = 
        {
            id: 1,
            name: "session 1",
            description: "Session blockers 1",
            date: "2018-02-28 05:35:00",
            teacher_id: 1,
            users: [user.id],
            createdAt: getFormattedDate(-10),
            updatedAt: getFormattedDate(3),
        }           
    

        const teacher = teachers.find((teacher) => teacher.id == session.teacher_id);

        cy.intercept("GET", "/api/session/1", {
            body:session
        });

        cy.intercept("GET", `/api/teacher/${session.id}`, teacher)

        const sessionCard =  cy.get('.items > :nth-child(1)');
        sessionCard.contains('Detail').click();
        
        cy.intercept("DELETE", `/api/session/1/participate/${user.id}`, {});
        cy.intercept("GET", "/api/session/1", {
            id: 1,
            name: "session 1",
            description: "Session blockers 1",
            date: "2018-02-28 05:35:00",
            teacher_id: 1,
            users: [user.id],
            createdAt: getFormattedDate(-10),
            updatedAt: getFormattedDate(3),              
        })
        // Do not participate button
        const doNotParticipateButton =  cy.contains('Do not participate');
        doNotParticipateButton.should("be.visible");
        doNotParticipateButton.click();
    })
})