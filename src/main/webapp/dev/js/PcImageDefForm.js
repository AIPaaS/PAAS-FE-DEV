var CurrentId = "";
var CurrentPageNum = 1;
/** 初始化 **/
function init() {
	initData(function() {
		initComponent();
		initListener();
		initFace();
		if(!CU.isEmpty(CurrentId)) {
			queryInfo();
		}
	});
}

/** 初始化页面、内存等基本数据 **/
function initData(cb) {
	$("#productId").html("");
	CurrentId = PRQ.get("id");
	CurrentPageNum = PRQ.get("pageNum");
	if(CU.isEmpty(CurrentPageNum)) CurrentPageNum = 1;
	
	RS.ajax({url:"/dev/product/getProductDropList",ps:{addEmpty:true, addAttr:true},cb:function(result) {
		DROP["DV_PRODUCT_CODE"] = result;
		var selhtml = PU.getSelectOptionsHtml("DV_PRODUCT_CODE");
		$("#productId").html(selhtml);
		if(CU.isFunction(cb))cb();
	}});
}

/** 初始化组件 **/
function initComponent() {

	
}

/** 对组件设置监听 **/
function initListener() {
	$("#isExternal").bind("change",function(){
		$("#imageLockName").text("");
		if($("#isExternal").prop("checked")){
			$("#productId").val("");
			$("#projectId").val("");
			$("#div_productId").hide();
			$("#div_projectId").hide();
		}else{
			$("#div_productId").show();
			$("#div_projectId").show();
		}
	});
	$("#productId").bind("change",function(){
		var productId = $("#productId").val();
		var item = CU.getDropItemRecord("DV_PRODUCT_CODE", productId);
		reloadProjectDropList(productId);
		
		if(!CU.isEmpty(item) && !CU.isEmpty(item.attributes)) {
			$("#imageLockName").text("/"+item.attributes.code);
		}else{
			$("#imageLockName").text("");
		}
	});
	
	
	
	
	$("#projectId").bind("change",function(){
		var projectId = $("#projectId").val();
		var item = CU.getDropItemRecord("DV_PROJECT_CODE", projectId);
		if(!CU.isEmpty(item) && !CU.isEmpty(item.attributes)) {
			$("#imageLockName").text("/"+$("#imageLockName").text().split("/")[1]+"/"+item.attributes.code+"/");
		}else{
			$("#imageLockName").text("/"+$("#imageLockName").text().split("/")[1]);
		}
	});

	$("#form_imageDef").submit(function(e){
	    e.preventDefault();
	    submitForm();
	});
	RS.setAjaxLodingButton("btn_submit");
}

/** 初始化界面 **/
function initFace() {
	
}


function reloadProjectDropList(productId, cb) {
	$("#projectId").html("");
	RS.ajax({url:"/dev/project/getProjectDropList",ps:{addEmpty:true, addAttr:true, productId:productId},cb:function(result) {
		DROP["DV_PROJECT_CODE"] = result;
		var selhtml = PU.getSelectOptionsHtml("DV_PROJECT_CODE");
		$("#projectId").html(selhtml);
		if(CU.isFunction(cb)) cb();
	}});
}


function queryInfo(){
	RS.ajax({url:"/dev/image/queryDefInfoById",ps:{id:CurrentId},cb:function(rs) {
		var isExternal = rs.def.isExternal;
		$("#dirName").val(rs.def.dirName);
		$("#imageName").val(rs.def.imageName);
		$("#versionNo").val(rs.def.versionNo);
		if(isExternal){
			$("#div_productId").hide();
			$("#div_projectId").hide();
			$("#isExternal").prop("checked",true);
		}else{
			$("#div_productId").show();
			$("#div_projectId").show();
			$("#isExternal").prop("checked",false);
			$("#productId").val(rs.def.productId);
			if(!CU.isEmpty(rs.def.productId)) {
				reloadProjectDropList(rs.def.productId, function() {
					$("#projectId").val(rs.def.projectId);
				});
			}
		}
		
		
	}});
}

/**提交表单**/
function submitForm(){
	var bean = PU.getFormData("form_imageDef");
	bean.isExternal = 1; //1=是 0=否
	var isChecked = $("#isExternal").prop("checked");
	if(!isChecked){
		bean.isExternal = 0;
		bean.dirName=$("#imageLockName").text()+$("#dirName").val();
	} 
		
	if(isChecked){
		bean.productId = 0;
		bean.projectId = 0;
	}else {
		var productId = $("#productId").val();
		var projectId = $("#projectId").val();
		if(CU.isEmpty(productId)){
			CC.showMsg({msg:"所属产品不能为空"});
			return;
		}
		if(CU.isEmpty(projectId)){
			CC.showMsg({msg:"所属工程不能为空"});
			return;
		}
	}
	if(!CU.isEmpty(CurrentId)) bean.id = CurrentId;
	RS.ajax({url:"/dev/image/saveOrUpdateDef",ps:bean,cb:function(r) {
		//alert("code:"+r);
		if(r!=null && r!=undefined && r=="-999999"){
			//alert("保存失败，ImageFullName重复！请修改后重试");
			 RS.showErrMsg(null, "Code:=" + r + "保存失败，ImageFullName重复！请修改后重试");
		}else{
			var url = ContextPath+"/dispatch/mc/10308";
			if(!CU.isEmpty(CurrentPageNum)) url += "?pageNum="+CurrentPageNum;
			window.location = url;
		}
	}});
}


