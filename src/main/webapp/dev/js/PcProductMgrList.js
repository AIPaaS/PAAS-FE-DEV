
var CurrDataMap = {};
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
	if(CU.isFunction(cb)) cb();
}
function initComponent() {
}
function initListener() {
	$("#code").bind("keyup", doCdtTFKeyUp);
	$("#name").bind("keyup", doCdtTFKeyUp);
	$("#btn_query").bind("click",function(){query(1);});
	$("#grid_pageSize").bind("change",function(){query(1);});
}
function initFace() {
}

/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}

function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#productMgrTable").html("");
	delete CurrDataMap;
	CurrDataMap = {};
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var orders = "CODE";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,code:code,name:name,orders:orders};
	RS.ajax({url:"/dev/product/queryMgrPage",ps:ps,cb:function(r) {
		var data = r.data;
		for(var i=0; i<data.length; i++) {
			CurrDataMap["key_"+data[i].id] = data[i];	
		}
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
			$('#productMgrTable-tmpl').tmpl(r).appendTo("#productMgrTable");
			for(var i=0;i<data.length;i++){
				$("#a_apply_doc_open_"+data[i].id).editable({
					display:false,
					validate: function(value) {
			           if($.trim(value) == "") return "请先选择版本库类型.";
			        },
			        source:{1:"SVN", 2:"GIT"},
					url: function (pps) {
						return submitApplyDoc(pps.pk, pps.value);
					}
				});
			}
		}
	}});
}


function submitApplyDoc(id, respType){
	return PU.submitEditable({url:"/dev/product/applyRespDoc",ps:{productId:id,respType:respType},cb:function(result) {
		query(ParamPageNum);
	}});
}
