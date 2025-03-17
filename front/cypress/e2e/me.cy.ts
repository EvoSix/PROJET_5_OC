/// <reference types="cypress" />

import { getMonthName,getDayFromDate,getYearFromDate } from "../utils/Datemanagment";

describe("Me spec", () => {
    it("Shows admin user informations", () => {
        // Login mock
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: "yoga@studio.com",
                firstName: 'Admin',
                lastName: 'Admin',
                admin: true
            }
        })

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type(`${"test!12345"}{enter}{enter}`);
        
        cy.url().should('include', '/sessions');

        const userInfo = {
            id: 1,
            email: "yoga@studio.com",
            lastName: "Admin",
            firstName: "Admin",
            admin: true,
            createdAt: "2025-01-29 18:57:17",
            updatedAt: "2024-01-29 18:57:17",
        }

        cy.intercept('GET', '/api/user/1', userInfo)

        // Click on Account button
        cy.get('[routerlink="me"]').click();

        const createdAtYear = getYearFromDate(userInfo.createdAt);
        const updatedAtYear = getYearFromDate(userInfo.updatedAt);
        const createdAtMonth = getMonthName(userInfo.createdAt);
        const updatedAtMonth = getMonthName(userInfo.updatedAt);
        const createdAtDay = getDayFromDate(userInfo.createdAt);
        const updatedAtDay = getDayFromDate(userInfo.updatedAt);

        // Verify user informations are visible
        cy.contains(`${userInfo.firstName} ${userInfo.lastName.toUpperCase()}`).should('be.visible');
        cy.contains(userInfo.email).should('be.visible');
        cy.contains(userInfo.admin ? "You are admin" : "").should('be.visible');
        cy.contains(`${createdAtMonth} ${createdAtDay}, ${createdAtYear}`).should('be.visible');
        cy.contains(`${updatedAtMonth} ${updatedAtDay}, ${updatedAtYear}`).should('be.visible');
    })

    it("Shows non admin user informations", () => {
        // Login mock
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 3,
                username: "test@test.com",
                firstName: 'test',
                lastName: 'test',
                admin: false,
            }
        })

        cy.get('input[formControlName=email]').type("test@test.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
        
        cy.url().should('include', '/sessions');
        
        const userInfo = {
            id: 3,
            email: "test@test.com",
            lastName: "test",
            firstName: "test",
            admin: false,
            createdAt: "2025-01-06 18:04:48",
            updatedAt: "2025-01-06 18:04:49",
        }

        cy.intercept('GET', '/api/user/3', userInfo);

        // Click on Account button
        cy.get('[routerlink="me"]').click();

        const createdAtYear = getYearFromDate(userInfo.createdAt);
        const updatedAtYear = getYearFromDate(userInfo.updatedAt);
        const createdAtMonth = getMonthName(userInfo.createdAt);
        const updatedAtMonth = getMonthName(userInfo.updatedAt);
        const createdAtDay = getDayFromDate(userInfo.createdAt);
        const updatedAtDay = getDayFromDate(userInfo.updatedAt);

        
        // Verify user informations are visible
        cy.contains(`${userInfo.firstName} ${userInfo.lastName.toUpperCase()}`).should('be.visible');
        cy.contains(userInfo.email).should('be.visible');
        cy.contains(userInfo.admin ? "You are admin" : "Delete my account:").should('be.visible');
        cy.contains(`${createdAtMonth} ${createdAtDay}, ${createdAtYear}`).should('be.visible');
        cy.contains(`${updatedAtMonth} ${updatedAtDay}, ${updatedAtYear}`).should('be.visible');

        // Verify delete account button is visible for non admin user
        cy.get('button.mat-raised-button').should("be.visible");
        cy.get('.my2 > .mat-focus-indicator > .mat-button-wrapper > .mat-icon').should("contain","delete");
        cy.get('.ml1').should("contain","Detail");
    })
})