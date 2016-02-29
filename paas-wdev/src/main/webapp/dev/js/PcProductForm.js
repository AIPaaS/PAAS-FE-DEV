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
	RS.ajax({url:"/res/res/getCompRoomDropList",ps:{addEmpty:true, addAttr:true},cb:function(result) {
		DROP["DV_COMP_ROOM_CODE"] = result;
		var roomselhtml = PU.getSelectOptionsHtml("DV_COMP_ROOM_CODE");
		$("#compRoomId").html(roomselhtml);
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
	$("#form_product").submit(function(e){
	    e.preventDefault();
	    submitForm();
	});
	RS.setAjaxLodingButton("btn_submit");
}

/** 初始化界面 **/
function initFace() {
	
}



function queryInfo(){
	RS.ajax({url:"/dev/product/queryInfoById",ps:{id:CurrentId},cb:function(rs) {
		PU.setFormData(rs.product, "form_product");
		var status = rs.product.status;
		setMgrNameFields(rs.mgrOps);
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
	var bean = PU.getFormData("form_product");
	bean.status = $("#status1").prop("checked")?1:0;
	if(CU.isEmpty(MgrOpIds)) {
		CC.showMsg({msg:"请选择产品管理员!"});
		return ;
	}
	bean.strMgrIds = MgrOpIds.join(",");
	
	if(!CU.isEmpty(CurrentId)) bean.id = CurrentId;
	
	
	RS.ajax({url:"/dev/product/saveOrUpdate",ps:bean,cb:function(rs) {
		CurrentId = rs;
		var url = ContextPath+"/dispatch/mc/10301";
		if(!CU.isEmpty(PageNum)){
			url += "?pageNum="+PageNum;
		}
		window.location = url;
	}});
}



