package com.daeliin.framework.core.controller;

import com.daeliin.framework.core.exception.PageRequestException;
import org.springframework.data.domain.Sort;

public class PageRequestParameters {
    
    private int pageNumber;
    private int pageSize;
    private Sort.Direction direction;
    private String[] properties;

    public PageRequestParameters(
            final String pageNumberParameter, 
            final String pageSizeParameter, 
            final String directionParameter, 
            final String... propertiesParameter) throws PageRequestException {
        
        buildPageNumber(pageNumberParameter);
        buildPageSize(pageSizeParameter);
        buildDirection(directionParameter);
        buildProperties(propertiesParameter);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public String[] getProperties() {
        return properties;
    }
    
    private void buildPageNumber(final String pageNumberParameter) throws PageRequestException {
        boolean pageNumberParameterIsNotCorrect = false;
        
        try {
            int pageNumberValue = Integer.valueOf(pageNumberParameter);
            
            if (pageNumberValue < 0) {
                pageNumberParameterIsNotCorrect = true;
            }
            
            this.pageNumber = pageNumberValue;
        } catch (NumberFormatException e) {
            pageNumberParameterIsNotCorrect = true;
        }
        
        if (pageNumberParameterIsNotCorrect) {
            throw new PageRequestException("Page number must be an integer, equal to or greater than 0");
        }
    }
    
    private void buildPageSize(final String pageSizeParameter) throws PageRequestException {
        boolean pageSizeParameterIsNotCorrect = false;
        
        try {
            int pageSizeValue = Integer.valueOf(pageSizeParameter);
            
            if (pageSizeValue < 0) {
                pageSizeParameterIsNotCorrect = true;
            }
            
            this.pageSize = pageSizeValue;
        } catch (NumberFormatException e) {
            pageSizeParameterIsNotCorrect = true;
        }
        
        if (pageSizeParameterIsNotCorrect) {
            throw new PageRequestException("Page size must be an integer, equal to or greater than 0");
        }
    }
    
    private void buildDirection(final String directionParameter) throws PageRequestException {
        if (directionParameter.equalsIgnoreCase("asc")) {
            this.direction = Sort.Direction.ASC;
        } else if (directionParameter.equalsIgnoreCase("desc")) {
            this.direction = Sort.Direction.DESC;
        } else {
            throw new PageRequestException("Page direction must be 'asc' or 'desc'");
        }
    }
    
    private void buildProperties(final String ... propertiesParameter) {
        this.properties = propertiesParameter;
    }
}