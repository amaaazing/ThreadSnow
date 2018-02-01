/**
 * This jQuery plugin displays pagination links inside the selected elements.
 *
 * @author cyj
 * @version 1.2
 * @param {int} total Number of entries to paginate
 * @param {Object} opts Several options (see README for documentation)
 * @return {Object} jQuery Object
 */
(function($){
	/**
	 * Calculate the maximum number of pages
	 */
	function numPages(jq) {
		var opts=$.data(jq,"pagination").options;
		return Math.ceil(opts.total/opts.items_per_page);
	};
	
	/**
	 * Calculate start and end point of pagination links depending on 
	 * current_page and num_display_entries.
	 * @return {Array}
	 */
	function getInterval(jq)  {
		var opts=$.data(jq,"pagination").options;
		var current_page = opts.current_page;
		var ne_half = Math.ceil(opts.num_display_entries/2); //5
		var np = numPages(jq);
		var upper_limit = np-opts.num_display_entries;
		var start = current_page>ne_half?Math.max(Math.min(current_page-ne_half, upper_limit), 0):0;
		var end = current_page>ne_half?Math.min(current_page+ne_half, np):Math.min(opts.num_display_entries, np);
		return [start,end];
	};
	
	/**
	 * This is the event handling function for the pagination links. 
	 * @param {int} page_id The new page number
	 */
	function pageSelected(jq,page_id, evt){
		var _3=$.data(jq,"pagination");
		var opts=_3.options;
		var pn=numPages(jq);
		if(page_id<0 || Number(page_id+1)>pn){
			return false;
		}
		page_id = page_id<0?0:page_id; // Normalize page id to sane value
		page_id = page_id>pn?pn:page_id;
		opts.current_page = page_id;
		drawLinks(jq);
		if(Number(page_id+1)>pn){
			page_id=pn-1;
		}
		var continuePropagation = opts.callback(Number(page_id+1), $(jq));
		if (evt && !continuePropagation) {
			if (evt.stopPropagation) {
				evt.stopPropagation();
			}else {
				evt.cancelBubble = true;
			}
		}
		return continuePropagation;
	};
	
	/**
	 * This function inserts the pagination links into the container element
	 */
	function drawLinks(jq) {
		var _3=$.data(jq,"pagination");
		var opts=_3.options;
		var current_page = opts.current_page;
		var panel = $(jq);
		var interval = getInterval(jq);
		var np = numPages(jq);
		panel.addClass('myPagination').empty();
		// This helper function returns a handler function that calls pageSelected with the right page_id
		var getClickHandler = function(jq,page_id) {
			return function(evt){ return pageSelected(jq,page_id,evt); };
		};
		// Helper function for generating a single link (or a span tag if it's the current page)
		var appendItem = function(page_id, appendopts){
			page_id = page_id<0?0:page_id; // Normalize page id to sane value
			page_id = page_id>np?np:page_id;
			appendopts = $.extend({text:Number(page_id)+1, classes:""}, appendopts||{});
			var lnk;
			if(page_id == current_page){
				lnk = $("<span class='current'>"+(appendopts.text)+"</span>");
			}else{
				lnk = $("<a>"+(appendopts.text)+"</a>").bind("click", getClickHandler(jq,page_id)).attr('href', opts.link_to.replace(/__id__/,page_id));			
			}
			if(appendopts.classes){lnk.addClass(appendopts.classes);}
			panel.append(lnk);
		};
		// Generate "Previous"-Link
		if(opts.prev_text &&  opts.prev_show_always && current_page > 0 && np>0){
			appendItem(current_page-1,{text:opts.prev_text, classes:"prev"});
		}
		// Generate starting points
		if (interval[0] > 0 && opts.num_edge_entries > 0){
			var end = Math.min(opts.num_edge_entries, interval[0]);
			for(var i=0; i<end; i++) {
				appendItem(i);
			}
			if(opts.num_edge_entries < interval[0] && opts.ellipse_text){
				$("<span>"+opts.ellipse_text+"</span>").appendTo(panel);
			}
		}
		// Generate interval links
		for(var i=interval[0]; i<interval[1]; i++) {
			appendItem(i);
		}
		// Generate ending points
		if (interval[1] < np && opts.num_edge_entries > 0){
			if(np-opts.num_edge_entries > interval[1]&& opts.ellipse_text){
				$("<span>"+opts.ellipse_text+"</span>").appendTo(panel);
			}
			var begin = Math.max(np-opts.num_edge_entries, interval[1]);
			for(var i=begin; i<np; i++) {
				appendItem(i);
			}	
		}
		// Generate "Next"-Link
		if(opts.next_show_always && opts.next_text){
			if(current_page < np-1){
				appendItem(current_page+1,{text:opts.next_text, classes:"next"});
			}
//			else{
//				appendItem(current_page,{text:opts.next_text, classes:"next"});
//			}
		}
		
		var pageLeft=$("<span class='myspan'>共"+np+"页&nbsp;到第<input type=\"text\" id=\"numberInput\" class='numberInput' />页 &nbsp;&nbsp; <input type=\"button\" class='confirmBtn' value=\""+opts.confirmBtn_text+"\" id=\"confirmBtn\"/></span>");
		panel.append(pageLeft);
		$("#confirmBtn").bind("click", function(e){
			var page_id=$.trim($("#numberInput").val());
			if(page_id=='' || isNaN(page_id)){
				return false;
			}
			pageSelected(jq,parseInt(page_id)-1,e); 
		});
		$("#numberInput").bind('keydown',function(e){
			var act = document.activeElement.id;
			var currKey = e.keyCode||e.which||e.charCode;
			if(act=="numberInput" && currKey == 13){ 
				$("#confirmBtn").trigger('click');
				return false;
			}
			if (e.stopPropagation) {
				e.stopPropagation();
			}else {
				e.cancelBubble = true;
			}
		});

		if(opts.is_show_top && $.trim(opts.topDomEl).length>0){
			var $topDomEl=$("#"+opts.topDomEl);
			$topDomEl.addClass('myPaginationTop').empty();
			if(np>0){
				$topDomEl.append("<em style='margin-right:8px;'>"+(current_page+1)+"/"+np+"</em>");
			}else{
				$topDomEl.append("<em style='margin-right:8px;'>0/0</em>");
			}
			var $topPre;
			var $topNext;
			if(current_page > 0 && np>0){			
				$topPre=$("<a href='javascript:void(0);' class='pre'></a>").bind("click", function(e){
					prevPage(jq,e);
				});
			}else{
				$topPre=$("<a href='javascript:void(0);' class='nopre'></a>");
			}
			if(current_page < np-1){
				$topNext=$("<a href='javascript:void(0);' class='pagnext'></a>").bind("click", function(e){
					nextPage(jq,e);
				});
			}else{
				$topNext=$("<a href='javascript:void(0);' class='nopagnext'></a>");
			}	
			$topDomEl.append($topPre).append($topNext);	
		}

		if(opts.pageList.length>0){
			var pageList=opts.pageList;
			var pageListSelect=$("<select class='pageSelect'></seelct>");
			var item=[];
			for(var pageSize=0;pageSize < pageList.length;pageSize++){

				if(opts.items_per_page==pageList[pageSize]){
					item.push('<option selected>'+pageList[pageSize]+'</option>');
				}else{
					item.push('<option>'+pageList[pageSize]+'</option>');
				}
				
			}
			pageListSelect.append(item.join(''));
			pageListSelect.insertBefore(pageLeft);
			pageListSelect.bind('change',function(){
				opts.items_per_page=parseInt($(this).val());
				selectPage(jq,0);
			});
		}
	};
	
	function selectPage(jq,page_id){ 
		pageSelected(jq,page_id);
	};
	function prevPage(jq,evt){ 
		var _3=$.data(jq,"pagination");
		var opts=_3.options;
		var current_page = opts.current_page;
		if (current_page > 0) {
			pageSelected(jq,current_page - 1,evt);
			return true;
		}else {
			return false;
		}
	};
	function nextPage(jq,evt){ 
		var _3=$.data(jq,"pagination");
		var opts=_3.options;
		var current_page = opts.current_page;

		if(current_page < numPages(jq)-1) {
			pageSelected(jq,current_page+1,evt);
			return true;
		}else {
			return false;
		}
	};

	$.fn.myPagination=function(_1a,_1b){
		if(typeof _1a=="string"){
			return $.fn.myPagination.methods[_1a](this,_1b);
		}
		_1a=_1a||{};
		return this.each(function(){
			var _1c;
			_1c=$.extend({},$.fn.myPagination.defaults,_1a);
			$.data(this,"pagination",{options:_1c});
			drawLinks(this);
		});
	};
$.fn.myPagination.methods={options:function(jq){
	return $.data(jq[0],"pagination").options;
	},refresh:function(jq){
		return jq.each(function(){
			drawLinks(this);
		});
	},selectPage:function(jq,page_id){ 
		return jq.each(function(){
			pageSelected(this,page_id);
		});
	},prevPage:function (jq){
		return jq.each(function(){
			prevPage(this);
		});
	},nextPage:function (jq){
		return jq.each(function(){
			nextPage(this);
		});
	}
};

$.fn.myPagination.defaults = {total:0,items_per_page:15,num_display_entries:5,current_page:0,pageList:[15,30],num_edge_entries:1,link_to:"#",prev_text:"上一页",next_text:"下一页",ellipse_text:"...",confirmBtn_text:"确定",prev_show_always:true,next_show_always:true,topDomEl:'paginationTop','is_show_top':true,callback:function(){return false;}};
})(jQuery);