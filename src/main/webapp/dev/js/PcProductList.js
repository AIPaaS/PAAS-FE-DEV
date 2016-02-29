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
	RS.ajax({url:"/res/res/getCompRoomDropList",ps:{addEmpty:true, addAttr:true},cb:function(result) {
		DROP["DV_COMP_ROOM_CODE"] = result;
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
	$("#btn_add").bind("click",function(){window.location = ContextPath + "/dispatch/mc/1030101?pageNum="+ParamPageNum;});
}
function initFace() {
}

/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}

function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#productTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var orders = "CODE";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,code:code,name:name,orders:orders};
	RS.ajax({url:"/dev/product/queryInfoPage",ps:ps,cb:function(r) {
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
			$('#productTable-tmpl').tmpl(r).appendTo("#productTable");
		}
	}});
	
}

