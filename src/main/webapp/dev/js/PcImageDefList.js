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
	var selhtml = PU.getSelectOptionsHtml("V_IMAGE_IS_EXTERNAL");
	$("#sel_isExternal").html(selhtml);
	RS.ajax({url:"/dev/product/getProductDropList",ps:{addEmpty:true, addAttr:true},cb:function(rs) {
		DROP["DV_PRODUCT_CODE"] = rs;
		var selhtml = PU.getSelectOptionsHtml("DV_PRODUCT_CODE");
		$("#sel_productId").html(selhtml);
		if(CU.isFunction(cb))cb();
	}});
}
function initComponent() {
}
function initListener() {
	$("#imageFullName").bind("keyup", doCdtTFKeyUp);
	$("#sel_productId").bind("change",function(){
		var productId = $("#sel_productId").val();
		$("#sel_projectId").html("");
		if(!CU.isEmpty(productId)){
			RS.ajax({url:"/dev/project/getProjectDropList",ps:{addEmpty:true, addAttr:true, productId:productId},cb:function(rs) {
				DROP["DV_PROJECT_CODE"] = rs;
				var selhtml = PU.getSelectOptionsHtml("DV_PROJECT_CODE");
				$("#sel_projectId").html(selhtml);
			}});
		}
		query();
	});
	$("#sel_projectId").bind("change",function(){query();});
	$("#btn_query").bind("click",function(){query();});
	$("#grid_pageSize").bind("change",function(){query();});
	$("#sel_isExternal").bind("change",function(){query();});
	$("#btn_add").bind("click",function(){window.location = ContextPath + "/dispatch/mc/1030801?pageNum="+ParamPageNum;});
}
function initFace() {
}
/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}

function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#imageTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var imageFullName = $("#imageFullName").val();
	var isExternal = $("#sel_isExternal").val();
	var productId = $("#sel_productId").val();
	var projectId = $("#sel_projectId").val();
	var orders = "ID";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,imageFullName:imageFullName,isExternal:isExternal,productId:productId,projectId:projectId,orders:orders};
	RS.ajax({url:"/dev/image/queryDefInfoPage",ps:ps,cb:function(r) {
		if(!CU.isEmpty(r)){
			var data = r.data;
			for(var i=0; i<data.length; i++) {
				CurrDataMap["key_"+data[i].def.id] = data[i];
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
			$('#imageTable-tmpl').tmpl(r).appendTo("#imageTable");
			for(var i=0; i<data.length; i++) {
				$("#a_remove_image_def_"+data[i].def.id).bind("click",function(){
					var obj = CurrDataMap["key_"+this.id.substring(this.id.lastIndexOf("_")+1)];
					removeImageDef(obj.def.id);
				});
			}
		}
	}});
	
}

function removeImageDef(id){
	//alert(id)
	var obj = CurrDataMap["key_"+id];
	CC.showMsg({msg:"您确定要删除镜像[<font color='blue'>"+obj.def.imageFullName+"</font>]吗?",option:2,callback:function(r) {
		if(r != "ok") return ;
		RS.ajax({url:"/dev/image/removeDefById",ps:{id:id},cb:function(data) {
			
			if (data != null && data != undefined && data=="-1") {
				 //RS.showErrMsg(null, "Code:=" + data + "删除失败！");
				alert("Code:=" + data + "该镜像定义存在  构建中的任务，拒绝删除！")
				return false;
			} else {
				alert("Code:=" + data + "删除成功")
				query(ParamPageNum);
			}
			
		}});
	}});
}