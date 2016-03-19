var ParamPageNum = 1;
var CurrDataMap = {};

var TreeData = {};		//key=envType	value=treeList
var SelForCenterType = null;	//1=数据中心    2=资源中心    3=网络区域
var SelForCenterId = null;

var SelLinkCenter = null;
var SelOpenCenter = null;

var mouseenter = false;

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
//	var selhtml = PU.getSelectOptionsHtml("V_PC_IMAGE_STATUS");
//	$("#status").html(selhtml);
	RS.ajax({url:"/dev/product/getProductDropList",ps:{addEmpty:true, addAttr:true},cb:function(rs) {
		DROP["DV_PRODUCT_CODE"] = rs;
		var selhtml = PU.getSelectOptionsHtml("DV_PRODUCT_CODE");
		$("#productId").html(selhtml);
		
		RS.ajax({url:"/res/res/getResRegionDropListMap",ps:{addEmpty:false, addAttr:true,opts:"dc|rc"},cb:function(result) {
			DROP["DV_DATA_CENTER_CODE"] = result["dc"];
			DROP["DV_RES_CENTER_CODE"] = result["rc"];
			TreeData = toTreeData(DROP["DV_DATA_CENTER_CODE"], DROP["DV_RES_CENTER_CODE"]);
			
			if(CU.isFunction(cb))cb();
		}});
	}});
}
function initComponent() {
	for(var key in TreeData) {
		var tree = TreeData[key];
		$("#sel_forcenter_"+key).treeview({data:tree,color:"#428bca",selectedBackColor:"#f0f8ff",selectedColor:"#428bca",collapseIcon:"fa fa-minus-square-o",expandIcon:"fa fa-plus-square-o",onNodeSelected: function(e, node) {
			if(node.param1!=2 && node.param1!="2") return ;
			var linkid = SelLinkCenter.prop("id");
			var imageId = linkid.substring(linkid.lastIndexOf("_")+1);
			var obj = CurrDataMap["key_"+imageId];
			CC.showMsg({msg:"您确定要将镜像[<font color='blue'>"+obj.image.imageFullName+"</font>]发布至资源中心[<font color='blue'>"+node.attributes.resName+"</font>]吗?",option:2,callback:function(r) {
				if(r != "ok") {
					SelOpenCenter.hide(); 
					return ;
				}
				var envType = node.attributes.envType;
				var dataCenterId = node.attributes.dataCenterId;
				var resCenterId = node.attributes.id;
				deployImage(envType, imageId, dataCenterId, resCenterId);
				SelOpenCenter.hide();
			}});
		}});
	}
}
function initListener() {
	for(var key in TreeData) {
		$("#sel_forcenter_"+key).on("mouseenter",function(){mouseenter=true;});
		$("#sel_forcenter_"+key).on("mouseleave",function(){mouseenter=false;});
		$("#sel_forcenter_"+key).bind("click", function() {
			if(!SelOpenCenter.is(":hidden")) SelLinkCenter.focus();
		});
	}
	
	$("#imageFullName").bind("keyup", doCdtTFKeyUp);
	$("#productId").bind("change",function(){
		var productId = $("#productId").val();
		$("#projectId").html("");
		if(!CU.isEmpty(productId)){
			RS.ajax({url:"/dev/project/getProjectDropList",ps:{addEmpty:true, addAttr:true, productId:productId},cb:function(rs) {
				DROP["DV_PROJECT_CODE"] = rs;
				var selhtml = PU.getSelectOptionsHtml("DV_PROJECT_CODE");
				$("#projectId").html(selhtml);
			}});
		}
		query();
	});
	$("#projectId").bind("change",function(){query();});
//	$("#status").bind("change",function(){query();});
	$("#btn_query").bind("click",function(){query();});
	$("#grid_pageSize").bind("change",function(){query();});
}
function initFace() {
}
/** 执行条件文本框回车查询 **/
function doCdtTFKeyUp(e) {
	if(e.keyCode === 13) query(1);
}

function toTreeData(dcs, rcs) {
	var tree = {};
	var pnmap = {};		//key=env value=pnobj
	var dcmap = {};
	for(var i=0; i<dcs.length; i++) {
		dcmap[dcs[i].code] = dcs[i];
		dcs[i].id = dcs[i].code;
		dcs[i].text = dcs[i].name;
		dcs[i].icon = "fa fa-database";
	}
	
	for(var i=0; i<rcs.length; i++) {
		var envType = rcs[i].attributes.envType;
		if(CU.isEmpty(pnmap["env"+envType])) pnmap["env"+envType] = {};
		if(CU.isEmpty(pnmap["env"+envType][rcs[i].parentCode])) {
			pnmap["env"+envType][rcs[i].parentCode] = CU.clone(dcmap[rcs[i].parentCode]);
		}
		
		rcs[i].id = rcs[i].code;
		rcs[i].text = rcs[i].name;
		rcs[i].icon = "fa fa-sitemap";
		var pn = pnmap["env"+envType][rcs[i].parentCode];
		if(CU.isEmpty(pn.childNodes)) pn.childNodes = [];
		pn.childNodes.push(rcs[i]);
	}
	
	for(var key in pnmap) {
		var pnobj = pnmap[key];
		var treels = [];
		for(var pn in pnobj) {
			var p = pnobj[pn];
			if(CU.isEmpty(p.childNodes)) continue ;
			treels.push(p);
		}
		tree[key] = treels;
	}
	
	return tree;
}

function query(pageNum){
	if(CU.isEmpty(pageNum)) pageNum = 1;
	$("#imageDeployTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var imageFullName = $("#imageFullName").val();
	var productId = $("#productId").val();
	var projectId = $("#projectId").val();
	var status = 1;
	var orders = "ID";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,imageFullName:imageFullName,productId:productId,projectId:projectId,status:status,orders:orders};
	
	RS.ajax({url:"/dev/image/queryImageInfoPage",ps:ps,cb:function(r) {
		if(!CU.isEmpty(r)) {
			var data = r.data;
			for(var i=0; i<data.length; i++) {
				CurrDataMap["key_"+data[i].image.id] = data[i];
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
		        	refresh1(page);
		        }
		    	
		    });
			$('#imageDeployTable-tmpl').tmpl(r).appendTo("#imageDeployTable");
			for(var i=0; i<data.length; i++) {
				$("#a_deploy_image_"+data[i].image.id).bind("click",function() {
					var obj = CurrDataMap["key_"+this.id.substring(this.id.lastIndexOf("_")+1)];
					SelLinkCenter = $(this);
					SelOpenCenter = $("#sel_forcenter_env"+obj.image.status);
					showResCenter();
				});
				$("#a_deploy_image_"+data[i].image.id).on("blur", function() {
					if(!mouseenter) SelOpenCenter.hide();
				});
			}
		}
	}});
}

function removeImageDef(id){
	var obj = CurrDataMap["key_"+id];
	CC.showMsg({msg:"您确定要删除镜像[<font color='blue'>"+obj.def.imageFullName+"</font>]吗?",option:2,callback:function(r) {
		if(r != "ok") return ;
		RS.ajax({url:"/dev/image/removeImageById",ps:{id:id},cb:function() {
			query(ParamPageNum);
		}});
	}});
}

function showResCenter(){
	SelOpenCenter.css("top", SelLinkCenter.offset().top-SelLinkCenter.height()*2);
	SelOpenCenter.css("left", SelLinkCenter.offset().left-SelOpenCenter.width()*2+SelOpenCenter.width()/4);
	SelOpenCenter.show(); 
}


function deployImage(envType, imageId, dataCenterId, resCenterId) {
	var url = "/dev/image";
	switch(envType) {		//1=开发    2=测试    3=生产
		case 1: url += "/deployImage2Dev"; break;
		case 2: url += "/deployImage2Test"; break;
		case 3: url += "/deployImage2Release"; break;
		default : alert("请选择资源中心!"); return ;
	}
	
	RS.ajax({url:url, ps:{imageId:imageId, dataCenterId:dataCenterId, resCenterId:resCenterId, remark:""},cb:function(rs) {
		if(!CU.isEmpty(rs)) {
			var msg = "发布失败：<font color='red'>"+rs+"</font>";
			setTimeout(function() {
				CC.showMsg({msg:msg,callback:function() {
					query(ParamPageNum);
				}});
			}, 500);
		}else {
			query(ParamPageNum);
		}
	}});
}



