var ParamPageNum = 1;
var CurrDataMap = {};
var CurrObj;
 

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
    if (CU.isEmpty(ParamPageNum))
	ParamPageNum = 1;
    var selhtml = PU.getSelectOptionsHtml("V_IMAGE_IS_EXTERNAL");
    $("#sel_isExternal").html(selhtml);
    RS.ajax({
	url : "/dev/product/getProductDropList",
	ps : {
	    addEmpty : true,
	    addAttr : true
	},
	cb : function(rs) {
	    DROP["DV_PRODUCT_CODE"] = rs;
	    
	    var selhtml = PU.getSelectOptionsHtml("DV_PRODUCT_CODE");
	    $("#sel_productId").html(selhtml);
	    if (CU.isFunction(cb))
		cb();
	}
    });
 
    
}
function initComponent() {
}
function initListener() {
    $("#imageFullName").bind("keyup", doCdtTFKeyUp);
    $("#sel_productId").bind("change", function() {
	var productId = $("#sel_productId").val();
	
	if(productId!=""){
		$("#sel_projectId").css("background-color","#FFF");
		$("#sel_projectId").attr("disabled", false);
	}else{
		$("#sel_projectId").css("background-color","#f5f5f5");
		$("#sel_projectId").attr("disabled", true);
	}
	
	$("#sel_projectId").html("");
	if (!CU.isEmpty(productId)) {
	    RS.ajax({
		url : "/dev/project/getProjectDropList",
		ps : {
		    addEmpty : true,
		    addAttr : true,
		    productId : productId
		},
		cb : function(rs) {
		    DROP["DV_PROJECT_CODE"] = rs;
		    var selhtml = PU.getSelectOptionsHtml("DV_PROJECT_CODE");
		    $("#sel_projectId").html(selhtml);
		}
	    });
	}
	query();
    });
    $("#sel_projectId").bind("change", function() {
	query();
    });
    $("#btn_query").bind("click", function() {
	query();
    });
    $("#grid_pageSize").bind("change", function() {
	query();
    });
    $("#sel_isExternal").bind("change", function() {
	query();
    });
    $("#cancleUpload").bind("click", function() {
	var file = $("#imageFile") ;
	file.after(file.clone().val(""));      
	file.remove(); 
	$("#copyFile").val("");
	$("#tag").val("");
	$("#tagList").empty();
	$("#uploadModal").modal("hide");
    });
    $("#upLoad").bind("click", function() {
	upLoad();
    });
    $("#tag").blur(function(){
	var code = $(this).val();
	var result=checkTag(code);
        if(!result){
              alert("tag标记格式输入错误！格式为X.X.X 且X为0~9的数字 或latest！");
         }
        
    });
}
function initFace() {
}
/** 执行条件文本框回车查询 * */
function doCdtTFKeyUp(e) {
    if (e.keyCode === 13)
	query(1);
}


function query(pageNum) {
    if (CU.isEmpty(pageNum))
	pageNum = 1;
    $("#imageTable").html("");
    $("#ul_pagination").remove();
    $("#pagination_box").html(
	    '<ul id="ul_pagination" class="pagination-sm"></ul>');
    var pageSize = $("#grid_pageSize").val();
    var imageFullName = $("#imageFullName").val();
    var isExternal = $("#sel_isExternal").val();
    var productId = $("#sel_productId").val();
    var projectId = $("#sel_projectId").val();
    var orders = "ID";

    var ps = {
	pageNum : pageNum,
	pageSize : pageSize,
	imageFullName : imageFullName,
	isExternal : isExternal,
	productId : productId,
	projectId : projectId,
	orders : orders
    };
    RS.ajax({
	url : "/dev/image/queryDefInfoPage",
	ps : ps,
	cb : function(r) {
	    if (!CU.isEmpty(r)) {
		var data = r.data;
		for (var i = 0; i < data.length; i++) {
		    CurrDataMap["key_" + data[i].def.id] = data[i];
		}
		ParamPageNum = r.pageNum;
		$("#ul_pagination").twbsPagination({
		    totalPages : r.totalPages ? r.totalPages : 1,
		    visiblePages : 7,
		    startPage : r.pageNum,
		    first : "首页",
		    prev : "上一页",
		    next : "下一页",
		    last : "尾页",
		    onPageClick : function(event, page) {
			query(page);
		    }

		});

		$('#imageTable-tmpl').tmpl(r).appendTo("#imageTable");
		for (var i = 0; i < data.length; i++) {
		    $("#a_remove_image_" + data[i].def.id).bind(
			    "click",
			    function() {
				var obj = CurrDataMap["key_"
					+ this.id.substring(this.id
						.lastIndexOf("_") + 1)];
				removeImageDef(obj.lastImage.id);
			    });
		    $("#a_upload_image_" + data[i].def.id).bind(
			    "click",
			    function() {
				var obj = CurrDataMap["key_"
					+ this.id.substring(this.id
						.lastIndexOf("_") + 1)];
				queryImageId(obj);
			    })
		}
	    }
	}
    });

}

function removeImageDef(id) {
    var obj = CurrDataMap["key_" + id];
    CC.showMsg({
	msg : "您确定要删除镜像[<font color='blue'>" + obj.def.imageFullName
		+ "</font>]吗?",
	option : 2,
	callback : function(r) {
	    if (r != "ok")
		return;
	    RS.ajax({
		url : "/dev/image/removeImageById",
		ps : {
		    id : id
		},
		cb : function() {
		    query(ParamPageNum);
		}
	    });
	}
    });
}

function queryImageId(obj) {
    $("#tagList").empty();
    CurrObj = obj;
    if (obj.def.isExternal == 0) {
	var defId = obj.def.id;
	var orders = "CREATE_TIME desc"
	var ps = {
	    defId : defId,
	    orders : orders
	};
	RS.ajax({
	    url : "/dev/image/queryImageTags",
	    ps : ps,
	    cb : function(data) {
		if (!CU.isEmpty(data)) {
		    var tag = "";
		    for (var i = 0; i < data.length; i++) {
			tag += "<option>" + data[i].depTag + "</option>"
		    }
		    $("#tagList").append(tag);
		}

	    }
	});

    }
    $("#uploadModal").modal("show");

}

function getFile(obj, inputName) {
    var fileName = "";
    if (typeof (fileName) != "undefined") {
	fileName = $(obj).val().split("\\").pop();
    }
    
    var fileExt=(/[.]/.exec(fileName)) ? /[^.]+$/.exec(fileName.toLowerCase()) : ''; 
    if(fileExt=="tar"){
	
    }else if(fileExt=='gz'){
	var temp=fileName.split(".gz")[0];
	var tempExt=temp.substring(temp.lastIndexOf(".")+1);
	if(tempExt!="tar"){
	    alert("上传文件格式错误！");
	    return ;
	}
    }else{
	alert("上传文件格式错误！");
	return ;
    }
    var size=obj.files[0].size;
    var MaxSize=2*1024*1024*1024;
    if(size>MaxSize){
	alert("上传文件不能超过2G");
	return ;
    }
    
    $("input[name='" + inputName + "']").val(fileName);
}

function upLoad() {
    var tag=$("#tag").val();
    var result=checkTag(tag);
    if(!result){
       alert("tag标记格式输入错误！格式为X.X.X 且X为0~9的数字 或latest！");
       return;   
    }
    if(tag==""||tag==undefined){
	alert("tag标记为空，请输入tag!");
	return;  
    }
    
    var formData = new FormData($("#uploadForm")[0]);
    formData.append("defid", CurrObj.def.id);
    formData.append("image_name", CurrObj.def.imageFullName);
    formData.append("ext_name", CurrObj.def.imageName);
    var file=$("#imageFile").val();
    if(file==""||file==undefined){
	alert("请选择要上传的镜像！");
	return;
    }
    $("#uploadForm").hide();
    $(".spinner").show();
    $.ajax({
	url : ContextPath+"/dev/image/uploadImage",
	type : 'post',
	data : formData,
	cache : false,
	processData : false,
	contentType : false,
	success : function(data) {
	    var json=JSON.parse(data);
	    if(json.result=='success'){
		$(".spinner").css('display','none');
		$(".upload_info").text("上传成功");
		$(".upload_info").css('display','block'); 
		setTimeout("hideModal(true)",3000);
	    }else{
		$(".spinner").css('display','none');
		$(".upload_info").text("上传失败");
		$(".upload_info").css('display','block'); 
		setTimeout("hideModal(false)",3000);
	    }
	    
	},
	error : function(data) {
	    $(".upload_info").text("上传失败");
	    setTimeout("hideModal(false)",3000);
	}
    });
    
  
}
function hideModal(flag){
    if(flag){
	$(".upload_info").css('display','none'); 
	$(".upload_info").text("");
	$("#uploadModal").modal("hide");
	$("#tag").val("");
	$("#tagList").empty();
	var file = $("#imageFile") ;
	file.after(file.clone().val(""));      
	file.remove(); 
	$("#copyFile").val("");
	$("#uploadForm").css('display','block');
    }else{
	$(".upload_info").css('display','none'); 
	$(".upload_info").text("");
	$("#uploadForm").css('display','block');
    }
}
function checkTag(tag){
    var tag_reg = /^\d{1}.\d{1}.\d{1}$/;
    if(tag_reg.test(tag)||tag=='latest'){
         return true;
      }else{
         return false;
      }
}
