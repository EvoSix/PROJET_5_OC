/// <reference types="cypress" />



import getFormattedDate, { getMonthName,getYearFromDate, getDayFromDate } from "../utils/Datemanagment";

describe("Information session spec", () => {
    it("Shows session informations", () => {

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

        const session = {
            id: 1,
            name: "Session 1",
            description: "Session blockers 1",
            date: "2018-02-28 05:35:00",
            teacher_id: 1,
            users: [],
            createdAt: getFormattedDate(-10),
            updatedAt: getFormattedDate(3),  
        }

        cy.intercept('GET', '/api/session/1', {body:session});

        const sessions = [
            {
                id: 1,
                name: "Session 1",
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

        const teacher =  {
            id: 1,
            lastName: "DELAHAYE",
            firstName: "Margot",
            createdAt: "2025-03-02 17:39:57",
            updatedAt: "2025-03-02 17:39:57",
        }

        cy.intercept('GET', '/api/teacher/1', teacher);

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

        cy.get('.mat-raised-button').should("be.enabled");
        cy.url().should('include', '/sessions');
        
   
        cy.contains("Rentals available").should("be.visible");
        sessions.forEach((session) => {
            cy.contains(session.name).should('be.visible');
            cy.contains(session.description).should('be.visible');
            const picture = cy.get('img.picture');
            picture.should('have.attr','src', 'assets/sessions.png');
            picture.should('have.attr','alt', 'Yoga session');
            const year = getYearFromDate(session.date);
            const month = getMonthName(session.date,"en-US");
            const day = getDayFromDate(session.date);
            const date = `Session on ${month} ${day}, ${year}`;
            cy.contains(date).should('be.visible');
            cy.contains("Detail").should("be.visible");
        })        

        cy.contains('Detail').click();

        cy.url().should("include", "/sessions/detail/1");

    
        cy.contains(session.name).should("be.visible");
        cy.contains(`${teacher.firstName} ${teacher.lastName}`).should("be.visible");
        
   
        const picture = cy.get('img.picture');
        picture.should('have.attr','src', 'assets/sessions.png');
        picture.should('have.attr','alt', 'Yoga session');
        cy.contains(`${session.users.length} attendees`).should("be.visible");
        
       
        const year = getYearFromDate(session.date);
        const month = getMonthName(session.date,"en-US");
        const day = getDayFromDate(session.date);
        cy.contains(`${month} ${day}, ${year}`).should("be.visible");
        
      
        cy.contains("Description:").should("be.visible");
        cy.contains(session.description).should("be.visible");
        
      
        const createYear = getYearFromDate(session.createdAt);
        const createMonth = getMonthName(session.createdAt,"en-US");
        const createDay = getDayFromDate(session.createdAt);
        const updateYear = getYearFromDate(session.updatedAt);
        const updateMonth = getMonthName(session.updatedAt,"en-US");
        const updateDay = getDayFromDate(session.updatedAt);

        cy.contains(`${createMonth} ${createDay}, ${createYear}`).should("be.visible");
        cy.contains(`${updateMonth} ${updateDay}, ${updateYear}`).should("be.visible");
    })
    
    it("Shows delete button for admin user", () => {
   
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

        const session = {
            id: 1,
            name: "session 1",
            description: "Session blockers 1",
            date: "2018-02-28 05:35:00",
            teacher_id: 1,
            users: [],
            createdAt: getFormattedDate(-10),
            updatedAt: getFormattedDate(3),  
        }


        cy.intercept('GET', '/api/session/1', {body:session});

        const sessions = [
            {
                id: 1,
                name: "Session 1",
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

        const teacher =  {
            id: 1,
            lastName: "DELAHAYE",
            firstName: "Margot",
            createdAt: "2025-03-02 17:39:57",
            updatedAt: "2025-03-02 17:39:57",
        }
        cy.intercept('GET', '/api/teacher/1', teacher);

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);

        cy.get('.mat-raised-button').should("be.enabled");
        cy.url().should('include', '/sessions');

     
        cy.contains("Rentals available").should("be.visible");
        sessions.forEach((session) => {
            cy.contains(session.name).should('be.visible');
            cy.contains(session.description).should('be.visible');
            const picture = cy.get('img.picture');
            picture.should('have.attr','src', 'assets/sessions.png');
            picture.should('have.attr','alt', 'Yoga session');
            const year = getYearFromDate(session.date);
            const month = getMonthName(session.date,"en-US");
            const day = getDayFromDate(session.date);
            const date = `Session on ${month} ${day}, ${year}`;
            cy.contains(date).should('be.visible');
            cy.contains("Detail").should("be.visible");
        })        

        cy.contains('Detail').click();

        cy.url().should("include", "/sessions/detail/1");

  
        const deleteButton = cy.get("button[mat-raised-button]");
        deleteButton.get("mat-icon").should("contain", "delete");
        deleteButton.get("span.ml1").should("contain", "Delete");
        deleteButton.contains("Delete").should("be.visible")
    })
})