var CurrentId = "";
var PageNum = "";

var MgrOpIds = null;

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
	CurrentId = PRQ.get("id");
	PageNum = PRQ.get("pageNum");
	if(CU.isEmpty(CurrentId)) CurrentId = "";
	RS.ajax({url:"/dev/project/getProductInfoDropListMap",ps:{addEmpty:true, addAttr:true},cb:function(result) {
		DROP["DV_MGR_PRODUCT_CODE"] = result["pro"];
		var proSelhtml = PU.getSelectOptionsHtml("DV_MGR_PRODUCT_CODE");
		$("#productId").html(proSelhtml);
		DROP["DV_COMP_ROOM_CODE"] = result["room"];
		var roomSelhtml = PU.getSelectOptionsHtml("DV_COMP_ROOM_CODE");
		$("#compRoomId").html(roomSelhtml);
		if(CU.isFunction(cb))cb();
	}});
}

/** 初始化组件 **/
function initComponent() {
	userSelor = new UserSelector({callback:function(ops) {
		setMgrNameFields(ops);
	}});
}

/** 对组件设置监听 **/
function initListener() {
	$("#btn_select_mgr").bind("click",showSelectMgrs);
	$("#form_project").submit(function(e){
	    e.preventDefault();
	    submitForm();
	});
	$("#productId").bind("change",function(){
		var productId = $("#productId").val();
		if(CU.isEmpty(productId)) return;
		var item = CU.getDropItemRecord("DV_MGR_PRODUCT_CODE", productId);
		if(CU.isEmpty(item) || CU.isEmpty(item.attributes)) return;
		$("#compRoomId").val(item.attributes.compRoomId);
	});
	RS.setAjaxLodingButton("btn_submit");
}

/** 初始化界面 **/
function initFace() {
	
}
function queryInfo(){
	RS.ajax({url:"/dev/project/queryInfoById",ps:{id:CurrentId},cb:function(rs) {
		PU.setFormData(rs.project, "form_project");
		setMgrNameFields(rs.mgrOps);
		var status = rs.project.status;
		$("#status1").prop("checked", status==1);
		$("#status0").prop("checked", status==0);
	}});
}
function setMgrNameFields(arr) {
	var val = "";
	MgrOpIds = [];
	for(var i=0; i<arr.length; i++) {
		var id = arr[i].id;
		var name = arr[i].opName;
		if(CU.isEmpty(id)) id = arr[i].code;
		if(CU.isEmpty(name)) name = arr[i].name;
		if(i > 0) val += "，";
		val += name;
		MgrOpIds.push(id);
	}
	$("#mgrIds").val(val);
}
function showSelectMgrs(){
	userSelor.show(MgrOpIds);
}



/**提交表单**/
function submitForm(){
	var bean = PU.getFormData("form_project");
	bean.status = $("#status1").prop("checked")?1:0;
	bean.strMgrIds = MgrOpIds.join(",");
	
	if(!CU.isEmpty(CurrentId)) bean.id = CurrentId;
	
	
	RS.ajax({url:"/dev/project/saveOrUpdate",ps:bean,cb:function(rs) {
		CurrentId = rs;
		var url = ContextPath+"/dispatch/mc/10303";
		if(!CU.isEmpty(PageNum)){
			url += "?pageNum="+PageNum;
		}
		window.location = url;
	}});
}



