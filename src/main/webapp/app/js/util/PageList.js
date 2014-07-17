define([
  'jquery', 
], function($) {
  /**@type util.PageList */
  var PageList = function(pageSize, list) {
    this.list = list ;
    this.available = list.length;
    this.pageSize = pageSize;
    this.availablePage  = 1;
    this.currentPage = -1 ;
    this.currentListPage ;
  
    /**@memberOf util.PageList */
    this.getPageSize = function() { return pageSize  ; };
    
    /**@memberOf util.PageList */
    this.setPageSize = function(pageSize) {
      this.pageSize = pageSize ;
      this.setAvailablePage(this.available) ;
    };
    
    /**@memberOf util.PageList */
    this.getCurrentPage = function() { return this.currentPage ; };
    
    /**@memberOf util.PageList */
    this.getAvailable = function() { return this.available ; };
    
    /**@memberOf util.PageList */
    this.getAvailablePage = function() { return this.availablePage ; };
    
    /**@memberOf util.PageList */
    this.getDataList = function() { return this.list ; };
    
    /**@memberOf util.PageList */
    this.getPrevPage = function() {
      if(this.currentPage == 1) return 1 ;
      return this.currentPage - 1 ;
    };
    
    /**@memberOf util.PageList */
    this.getNextPage = function() {
      if(this.currentPage == this.availablePage) return this.currentPage ;
      return this.currentPage + 1 ;
    };
    
    /**@memberOf util.PageList */
    this.currentPageItems = function() {
      if(this.currentListPage == null) {
        this.populateCurrentPage(this.currentPage) ;
      }
      return this.currentListPage  ;
    };
    
    /**@memberOf util.PageList */
    this.setCurrentPage = function(pages) { 
      this.currentListPage =  pages ; 
    };
    
    /**@memberOf util.PageList */
    this.getPage = function(page) {
      this.checkAndSetPage(page) ;
      this.populateCurrentPage(page) ;
      return this.currentListPage ;
    };
    
    /**@memberOf util.PageList */
    this.checkAndSetPage = function(page) {
      if(page < 1 || page > this.availablePage) {
        throw new Exception("Page is out of range " + page) ;
      }
      this.currentPage =  page ;
    };
    
    /**@memberOf util.PageList */
    this.setAvailablePage = function(available) {
      this.available = available ;
      if (available == 0)  {
        this.availablePage = 1 ; 
        this.currentPage =  1 ;
      } else {
        var pages = Math.ceil(available / this.pageSize) ;
        //if(available % this.pageSize > 0) pages++ ;
        this.availablePage = pages ;
        this.currentPage =  1 ;
      }
      this.currentListPage = null ;
    };
    
    /**@memberOf util.PageList */
    this.getFrom = function() { 
      return (this.currentPage - 1) * this.pageSize ; 
    };
    
    /**@memberOf util.PageList */
    this.getTo = function() { 
      var to = this.currentPage * this.pageSize ; 
      if (to > this.available ) to = this.available ;
      return to ;
    };
    
    /**@memberOf util.PageList */
    this.getItemOnCurrentPage = function(idx) {
      return this.currentListPage[idx] ;
    };
    
    /**@memberOf util.PageList */
    this.removeItemOnCurrentPage = function(idx) {
      var from = this.getFrom() ;
      var realIdx = from + idx ;
      var cpage = this.getCurrentPage() ;
      this.list.splice(realIdx, 1);
      this.setAvailablePage(this.list.length) ;
      if(this.getAvailablePage() < cpage) cpage = this.getAvailablePage() ;
      this.getPage(cpage) ;
    };
    
    /**@memberOf util.PageList */
    this.getSubRange = function(page, rangeSize) {
      if(page < 1 || page > this.availablePage) {
        throw new RuntimeException("page " + page + " is out of range") ; 
      }
      var range = [];
      if(rangeSize >= this.availablePage) {
        range[0] = 1 ;
        range[1] = this.availablePage ;
        return range ;
      }
      
      var half = rangeSize/2 ;
      if(page - half < 1) {
        range[0] = 1 ;
        range[1] = rangeSize ;
      } else if(page + (rangeSize - half) > this.availablePage) {
        range[0] = this.availablePage -  rangeSize ;
        range[1] = this.availablePage ;
      } else {
        range[0] = page - half;
        range[1] = page + (rangeSize - half) ;
      }
      return  range ;
    };
    
    this.populateCurrentPage = function(page) {
      this.currentListPage = this.list.slice(this.getFrom(), this.getTo()) ;
    };
    
    //---------------------------------------------------------------------------
    
    this.setAvailablePage(list.length) ;
  };
  return PageList ;
});