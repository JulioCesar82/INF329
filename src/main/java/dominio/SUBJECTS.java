/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

/**
 * Enum representing various book subjects.
 * Implements the Category interface to categorize books.
 * 
 * @author User
 */
public enum SUBJECTS implements Category {
    ARTS, BIOGRAPHIES, BUSINESS, CHILDREN,
        COMPUTERS, COOKING, HEALTH, HISTORY,
        HOME, HUMOR, LITERATURE, MYSTERY,
        NON_FICTION, PARENTING, POLITICS,
        REFERENCE, RELIGION, ROMANCE,
        SELF_HELP, SCIENCE_NATURE, SCIENCE_FICTION,
        SPORTS, YOUTH, TRAVEL;
}