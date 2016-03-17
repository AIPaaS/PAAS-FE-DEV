var CurrDataMap = {};
var CurrTaskMap = {};
var ParamPageNum = 1;
var CurrObj;
var CurrTask;


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
	var selhtml = PU.getSelectOptionsHtml("V_PC_BUILD_DEF_IS_BUILD_IMAGE");
	$("#sel_isBuildImage").html(selhtml);
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
	$("#buildName").bind("keyup", doCdtTFKeyUp);
	$("#roomName").bind("keyup", doCdtTFKeyUp);
	$("#sel_productId").bind("change", function() {
		var productId = $("#sel_productId").val();
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
	$("#sel_isBuildImage").bind("change", function() {
		query();
	});
	$("#btn_add").bind(
			"click",
			function() {
				window.location = ContextPath + "/dispatch/mc/1030501?pageNum="
						+ ParamPageNum;
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
	$("#buildTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html(
			'<ul id="ul_pagination" class="pagination-sm"></ul>');
	var buildName = $("#buildName").val();
	var isBuildImage = $("#sel_isBuildImage").val();
	var productId = $("#sel_productId").val();
	var projectId = $("#sel_projectId").val();
	var pageSize = $("#grid_pageSize").val();
	var orders = "ID";
	var ps = {
		pageNum : pageNum,
		pageSize : pageSize,
		buildName : buildName,
		isBuildImage : isBuildImage,
		productId : productId,
		projectId : projectId,
		orders : orders
	};
	RS.ajax({
		url : "/dev/build/queryDefInfoPage",
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
				$('#buildTable-tmpl').tmpl(r).appendTo("#buildTable");
				for (var i = 0; i < data.length; i++) {
					
					//console.log(data);
					if(data[i].lastBuildTask.status=="2"){
						$("#thisBackBuildStatues_"+ data[i].def.id).val("2");
						$("#thisBackBuildId_"+ data[i].def.id).val(data[i].lastBuildTask.backBuildId);
						$("#a_build_task_gj_"+data[i].def.id).css("display", "none"); 
						$("#a_build_task_zd_"+data[i].def.id).css("display", "inline-block");
					}

					// 构建
					$("#a_build_task_gj_" + data[i].def.id).bind(
							"click",
							function() {
								var obj = CurrDataMap["key_"
										+ this.id.substring(this.id
												.lastIndexOf("_") + 1)];
								gj_BuildDef(obj.def.id);
							});

					// 历史
					$("#a_build_task_" + data[i].def.id).bind(
							"click",
							function() {
								var obj = CurrDataMap["key_"
										+ this.id.substring(this.id
												.lastIndexOf("_") + 1)];
								CurrObj=obj;
								queryBuildTaskRecord();
							});

					// 删除
					$("#a_remove_build_" + data[i].def.id).bind(
							"click",
							function() {
								var obj = CurrDataMap["key_"
										+ this.id.substring(this.id
												.lastIndexOf("_") + 1)];
								removeBuildDef(obj.def.id);
							});

					// 中止
					$("#a_build_task_zd_" + data[i].def.id).bind(
							"click",
							function() {
								var obj = CurrDataMap["key_"
										+ this.id.substring(this.id
												.lastIndexOf("_") + 1)];
								PcBuild_ZD(obj.def.id);
							});

				}
			}
		}
	});

}

//构建
function gj_BuildDef(id){
	$("#thisBackBuildId_"+id).val("");//每次构建时情况上次的返回id
	var backId = "";
	var obj = CurrDataMap["key_"+id];
		
	RS.ajax({url:"/dev/buildtask/saveBuildTask",ps:{id:id},cb:function(data) {
		 //调用完ajax返回的BackBuildId值
		backId = data;

		$("#thisBackBuildId_"+id).val(backId);
		
		$("#a_build_task_gj_"+id).css("display", "none"); 
		$("#a_build_task_zd_"+id).css("display", "inline-block");
		$("#td_build_task_msage_"+id).text("构建运行中");

	}});
	
}

// 删除
function removeBuildDef(id) {
	var allS = $("#a_build_task_gjname_" + id).text().trim();
	if (allS.substr(0,1)=='/') allS=allS.substr(1);
	var obj = CurrDataMap["key_" + id];
	CC.showMsg({
		msg : "您确定要删除构建任务[<font color='blue'>" + obj.def.buildName
				+ "</font>]吗?",
		option : 2,
		callback : function(r) {
			if (r != "ok")
				return;
			RS.ajax({
				url : "/dev/build/removeDefById",
				ps : {
					id : id,
					alls : allS
				},
				cb : function(data) {
					if (data != null && data != undefined && data == "-1") {
						 RS.showErrMsg(null, "Code:=" + data + "  构建运行中，拒绝删除！");
						return false;
					}
					else if (data != null && data != undefined && data == "-2") {
						 RS.showErrMsg(null, "Code:=" + data + "  删除失败！");
						return false;
					}
					else {
						//CC.showMsg({msg:"Code:=" + data + "  删除成功"});
						query(ParamPageNum);
					}

				}
			});
		}
	});
}

function queryBuildTaskRecord() {
    var pagenum = 1;
    var pageSize = 10;
    var orders = "CREATE_TIME desc";
    var buildDefId = CurrObj.def.id;
    var ps = {
	pageNum : pagenum,
	pageSize : pageSize,
	buildDefId : buildDefId,
	orders : orders
    };
    $("#iso_list").empty();
    RS.ajax({
	url : "/dev/buildtask/queryBuildTaskInfoList",
	ps : ps,
	cb : function(data) {
	    if (!CU.isEmpty(data)) {
		var iso_list = "";
		for (var i = 0; i < data.length; i++) {
		    CurrTaskMap["key_" + data[i].id] = data[i];
		    if (i == 0) {
			iso_list += "<li class='active' id='record_"
				+ data[i].id
				+ "'><a href=\"javascript:void(0)\">"
				+ CurrObj.imageDef.imageName + "-"
				+ data[i].backBuildId + "</a></li>";
		    } else {
			iso_list += "<li  id='record_" + data[i].id
				+ "'><a href=\"javascript:void(0)\">"
				+ CurrObj.imageDef.imageName + "-"
				+ data[i].backBuildId + "</a></li>";
		    }

		}
		$("#iso_list").append(iso_list);
		for (var i = 0; i < data.length; i++) {
		    $("#record_" + data[i].id).bind(
			    "click",
			    function() {
				$(this).addClass("active").siblings()
					.removeClass("active");
				clearInterval(timer);
				var task = CurrTaskMap["key_"
					+ this.id.substring(this.id
						.lastIndexOf("_") + 1)];
				CurrTask = task;
				$("#stdoutList").empty();
				$("#iso_tag").text(task.depTag);
				queryTaskRecord();
			    });
		}
		$("#iso_name").text(CurrObj.imageDef.imageName);
		$("#prod_proj").text(
			CurrObj.product.code + '|'
				+ CurrObj.project.code);
		CurrTask = data[0];
		$("#iso_tag").text(data[0].depTag);
		queryTaskRecord();

		$("#buildTask_modal").modal("show");

	    } else {
		RS.showErrMsg(null, "查询失败");
	    }

	}
    });
}

function PcBuild_ZD(id) {// 构建中止
	var bId = $("#thisBackBuildId_" + id).val();// 获取BackBuildId
	//alert("id:" + id+",bId:"+bId)
	var allS = $("#a_build_task_gjname_" + id).text().trim(); // (产品code/工程code/构建名)
	if (allS.substr(0,1)=='/') allS=allS.substr(1);
	if (bId != null && bId != undefined && !bId == "") {
		RS.ajax({
			url : "/dev/buildtask/updateBuildTaskStatusByBackId",
			ps : {
				buildDefId: id,
				backBuildId : bId,
				alls : allS
			},
			cb : function(data) {
				if (data != null && data != undefined && (data=="0"||data=="-1")) {
					 RS.showErrMsg(null, "Code:=" + data + "中止失败！");
					$("#td_build_task_msage_" + id).text("中止失败！");
					return false;
				} else {
					//CC.showMsg({msg:"Code:=" + data + "构建已中断"});
					$("#td_build_task_msage_" + id).text("构建已中断");
					query(ParamPageNum);
				}
			},
			errcb : function(errorCode, errorMsg) {
				 RS.showErrMsg(null, errorCode + " : " + errorMsg);
			}
		});
	} else {
		 RS.showErrMsg(null, "构建返回的BackBuildId为空,找不到中止对象！");
	}

}

function queryTaskRecord() {
    var task = CurrTask;
    var buildName = CurrObj.def.buildName;
    if (buildName.substr(0, 1) == '/')
	buildName = buildName.substr(1);
    var build_id = task.backBuildId;
    $("#status").text("");
    if (buildName != null || buildName != "" || build_id != ""
	    || build_id != null) {
	var ps = {
	    repo_name : buildName,
	    build_id : build_id
	};
	RS.ajax({
	    url : "/dev/buildtask/queryTaskRecord",
	    ps : ps,
	    cb : function(data) {
		if (!CU.isEmpty(data)) {
		    if (data.hasOwnProperty("error_code")) {
			RS.showErrMsg(null, "查询失败");
		    } else if (data.duration != "") {
			$("#start_time").text(data.started_at);
			$("#status").text(data.status);
			var stdoutlist = data.stdout.split("\n");
			var stdout = '';
			var length=$(".stdout li").length;
			for (var i = length; i < stdoutlist.length; i++) {
			    stdout += "<li style='width:676px; height:auto'>"
				    + stdoutlist[i] + "</li>";
			}
			$("#stdoutList").append(stdout);
			if (data.building == false) {
			} else {
			    setTimeout('queryTaskRecord()', 3000);
			}

		    } else if (data.duration == "") {
			if (task.status == 2){
			    RS.showErrMsg(null, '镜像构建在启动中，请稍后进行查询！');
			   
			    
			}else{
			    RS.showErrMsg(null, "查询失败,请稍后重试");
			}
			   
		    }

		} else {
		    RS.showErrMsg(null, "请求失败");
		}
	    }
	})
    } else {
	RS.showErrMsg(null, "请求失败");
    }

}

 
