var ParamPageNum = 1;
var CurrDataMap = {};


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
	
	if(CU.isFunction(cb))cb();
}
function initComponent() {
}
function initListener() {
	$("#code").bind("keyup", doCdtTFKeyUp);
	$("#name").bind("keyup", doCdtTFKeyUp);
	$("#btn_query").bind("click",function(){query();});
	$("#grid_pageSize").bind("change",function(){query();});
	
}
function initFace() {
}
/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}
function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	
	$("#projectMgrTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	delete CurrDataMap;
	CurrDataMap = {};
	var pageSize = $("#grid_pageSize").val();
	var code = $("#code").val();
	var name = $("#name").val();
	var status = 1;
	var orders = "CODE";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,code:code,name:name,status:status,orders:orders};
	RS.ajax({url:"/dev/project/queryMgrProdInfoPage",ps:ps,cb:function(r) {
		if(!CU.isEmpty(r)){
			var data = r.data;
			for(var i=0;i<data.length;i++){
				CurrDataMap["key_"+data[i].project.id] = data[i];
			}
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
			$('#projectMgrTable-tmpl').tmpl(r).appendTo("#projectMgrTable");
			for(var i=0;i<data.length;i++){				
				$("#a_apply_code_open_"+data[i].project.id).editable({
					display:false,
					validate: function(value) {
			           if($.trim(value) == "") return "请先选择版本库类型.";
			        },
			        //source:{1:"SVN", 2:"GIT"},
			        source:{2:"GIT"}, 
					url: function (pps) {
						return submitApplyRespCode(pps.pk, pps.value);
					}
				});
				$("#a_apply_doc_open_"+data[i].project.id).editable({
					display:false,
					validate: function(value) {
			           if($.trim(value) == "") return "请先选择版本库类型.";
			        },
			        //source:{1:"SVN", 2:"GIT"},
			        source:{2:"GIT"},
					url: function (pps) {
						return submitApplyRespDoc(pps.pk, pps.value);
					}
				});
				$("#a_set_image_url_"+data[i].project.id).editable({
					validate: function(value) {
			           if($.trim(value) == "") return "请先输入镜像库名.";
			        },
					url: function (pps) {
						return submitImageUrl(pps.pk, pps.value);
					}
				});
			}
		}
	}});
	
}


function submitApplyRespDoc(id, respType) {
	return PU.submitEditable({url:"/dev/project/applyRespDoc",ps:{projectId:id,respType:respType},cb:function(result) {
		query(ParamPageNum);
	}});	
}
function submitApplyRespCode(id, respType){
	return PU.submitEditable({url:"/dev/project/applyRespCode",ps:{projectId:id,respType:respType},cb:function(result) {
		query(ParamPageNum);
	}});
}




function submitImageUrl(id, imageRegUrl) {
	var info = CurrDataMap["key_"+id];
	var productId = info.project.productId;
	return PU.submitEditable({url:"/dev/project/updateImageReg",ps:{id:id, productId:productId,imageRegUrl:imageRegUrl}});
}


