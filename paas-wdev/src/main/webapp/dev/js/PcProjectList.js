var ParamPageNum = 1;

function init() {
	initData(function() {
		initComponent();
		initListener();
		initFace();
		query(ParamPageNum);
	});
	
}


function initData(cb) {
	ParamPageNum = PRQ.get("pageNum");
	if(CU.isEmpty(ParamPageNum)) ParamPageNum = 1;
	RS.ajax({url:"/dev/project/getProductInfoDropListMap",ps:{addEmpty:true, addAttr:true},cb:function(result) {
		DROP["DV_MGR_PRODUCT_CODE"] = result["pro"];
		var selhtml = PU.getSelectOptionsHtml("DV_MGR_PRODUCT_CODE");
		$("#sel_productId").html(selhtml);
		DROP["DV_COMP_ROOM_CODE"] = result["room"];
		if(CU.isFunction(cb))cb();
	}});
}
function initComponent() {
}
function initListener() {
	$("#code").bind("keyup", doCdtTFKeyUp);
	$("#name").bind("keyup", doCdtTFKeyUp);
	$("#btn_query").bind("click",function(){query();});
	$("#grid_pageSize").bind("change",function(){query();});
	$("#sel_productId").bind("change",function(){query();});
	$("#btn_add").bind("click",function(){window.location = ContextPath + "/dispatch/mc/1030301?pageNum="+ParamPageNum;});
}
function initFace() {
}
/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}
function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#projectTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var productId = $("#sel_productId").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var orders = "CODE";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,code:code,name:name,productId:productId,orders:orders};
	RS.ajax({url:"/dev/project/queryInfoPage",ps:ps,cb:function(r) {
		if(!CU.isEmpty(r)){
			ParamPageNum = r.pageNum;
			$("#ul_pagination").twbsPagination({
		        totalPages: r.totalPages?r.totalPages:1,
		        visiblePages: 7,
		        startPage: r.pageNum,
		        first:"首页",
		        prev:"上一页",
		        next:"下一页",
		        last:"尾页",
		        onPageClick: function (event, page) {
		        	query(page);
		        }
		    	
		    });
			$('#projectTable-tmpl').tmpl(r).appendTo("#projectTable");
		}
	}});
	
}

