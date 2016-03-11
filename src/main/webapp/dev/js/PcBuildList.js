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
	var selhtml = PU.getSelectOptionsHtml("V_PC_BUILD_DEF_IS_BUILD_IMAGE");
	$("#sel_isBuildImage").html(selhtml);
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
	$("#buildName").bind("keyup", doCdtTFKeyUp);
	$("#roomName").bind("keyup", doCdtTFKeyUp);
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
	$("#sel_isBuildImage").bind("change",function(){query();});
	$("#btn_add").bind("click",function(){window.location = ContextPath +"/dispatch/mc/1030501?pageNum="+ParamPageNum;});
}
function initFace() {
}
/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}
function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#buildTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var buildName = $("#buildName").val();
	var isBuildImage = $("#sel_isBuildImage").val();
	var productId = $("#sel_productId").val();
	var projectId = $("#sel_projectId").val();
	var pageSize = $("#grid_pageSize").val();
	var orders = "ID";
	var ps = {pageNum:pageNum,pageSize:pageSize,buildName:buildName,isBuildImage:isBuildImage,productId:productId,projectId:projectId,orders:orders};
	RS.ajax({url:"/dev/build/queryDefInfoPage",ps:ps,cb:function(r) {
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
			$('#buildTable-tmpl').tmpl(r).appendTo("#buildTable");
			for(var i=0; i<data.length; i++) {
				$("#a_remove_build_"+data[i].def.id).bind("click",function(){
					var obj = CurrDataMap["key_"+this.id.substring(this.id.lastIndexOf("_")+1)];
					removeBuildDef(obj.def.id);
				});
				
				$("#a_zd_build_"+data[i].def.id).bind("click",function(){
					var obj = CurrDataMap["key_"+this.id.substring(this.id.lastIndexOf("_")+1)];
					PcBuild_ZD(obj.def.id);//给绑定事件
				});
				
			}
		}
	}});
	
}
function removeBuildDef(id){
	var obj = CurrDataMap["key_"+id];
	CC.showMsg({msg:"您确定要删除构建任务[<font color='blue'>"+obj.def.buildName+"</font>]吗?",option:2,callback:function(r) {
		if(r != "ok") return ;
		RS.ajax({url:"/dev/build/removeDefById",ps:{id:id},cb:function(data) {
			if(data!=null && data!=undefined && data=="-1"){
				alert("Code:="+data+"  操作进行中，拒绝删除！");
				return false;
			}else{
				alert("Code:="+data+"  删除成功");
				query(ParamPageNum);
			}

		}});
	}});
}

function queryBuildTaskRecord(){
	
}

function PcBuild_ZD(id){//构建中止
	alert("PcBuild_ZD :"+id)
	//ajax
	query(ParamPageNum);
}





