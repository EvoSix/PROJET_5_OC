/// <reference types="cypress" />

import fieldValidator, { checkEmailFormat } from "../utils/Fieldmanagment"


describe('Register spec', () => {
    
    it('Register successful', () => {
          cy.visit('/register')
  
          cy.intercept('POST', '/api/auth/register', {
            body: {
              firstName: 'firstName',
              lastName: 'lastName',
              email: 'yoga@studio.com',
              password: 'password'
            },
          })
  
          cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []).as('session')

          const firstName : string = "firstName";
          const lastName : string = "lastName";
          const email : string = "yoga@studio.com";
          const password : string = "password";
  
          cy.get('input[formControlName=firstName]').type(firstName);
          cy.get('input[formControlName=lastName]').type(lastName);
          cy.get('input[formControlName=email]').type(email);
          cy.get('input[formControlName=password]').type(password);

          if(
            fieldValidator(firstName,3,20) &&
            fieldValidator(lastName,3,20) &&
            checkEmailFormat(email) &&
            fieldValidator(password,3,40) 
          ){
            cy.get('.register-form > .mat-focus-indicator').should("be.enabled");
            cy.get('.register-form > .mat-focus-indicator').click()
            cy.url().should('include', '/login');           
          }else{
            cy.get('.register-form > .mat-focus-indicator').should("be.disabled");
            cy.get('.register-form > .mat-focus-indicator').click();
            cy.url().should('not.include', '/login');
            cy.contains("An error occurred").should("be.visible");   
          }
    })
  
    it("Register failed, invalid fields", () => {
      cy.visit('/register')
  
      cy.intercept('POST', '/api/auth/register', {
        body: "Bad request",
        statusCode: 400,
      })

      const firstName : string = "aa";
      const lastName : string = "aa";
      const email : string = "a@a.a";
      const password : string = "a";

      cy.get('input[formControlName=firstName]').type(firstName);
      cy.get('input[formControlName=lastName]').type(lastName);
      cy.get('input[formControlName=email]').type(email);
      cy.get('input[formControlName=password]').type(password);

      if(
        fieldValidator(firstName,3,20) &&
        fieldValidator(lastName,3,20) &&
        checkEmailFormat(email) &&
        fieldValidator(password,3,40) 
      ){
        cy.get('.register-form > .mat-focus-indicator').should("be.enabled");
        cy.get('.register-form > .mat-focus-indicator').click()
        cy.url().should('include', '/login');           
      }else{
        cy.get('.register-form > .mat-focus-indicator').click();
        cy.url().should('not.include', '/login');
        cy.contains("An error occurred").should("be.visible");   
      }      
    })
  })
  
describe('User Registration', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: { message: 'User registered successfully!' },
    }).as('registerUser');

    cy.visit('/register');
  });

  it('should navigate to the Register page', () => {
    cy.url().should('include', '/register');
  });

  it('should remain disabled when attempting to register with missing fields', () => {
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('button[type="submit"]').click({ force: true });
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should register a user with valid inputs', () => {
    cy.get('input[formControlName=email]').type('will.smith@gmail.com');
    cy.get('input[formControlName=firstName]').type('Will');
    cy.get('input[formControlName=lastName]').type('Smith');
    cy.get('input[formControlName=password]').type('test123');

    cy.get('button[type="submit"]').should('not.be.disabled').click({ force: true });

    cy.wait('@registerUser').its('response.statusCode').should('eq', 201);
    cy.url().should('include', '/login');
  });
});