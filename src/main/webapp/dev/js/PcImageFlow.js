var ParamPageNum = 1;
var CurrDataMap = {};
var DefId = "";

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
	DefId = PRQ.get("defId");
	if(CU.isEmpty(ParamPageNum)) ParamPageNum = 1;

	RS.ajax({url:"/dev/image/queryDefById",ps:{id:DefId},cb:function(rs) {
		if(CU.isEmpty(rs)) {
			alert("没有对应的镜像定义!");
			pageBack();
		}
		$("#span_title").html("["+rs.imageFullName+"]");
		if(CU.isFunction(cb))cb();
	}});
}
function initComponent() {
}
function initListener() {
	$('#maskedDate').datetimepicker({
		minView: "month",
		format: "yyyy-mm-dd", 
		language: "zh-CN",
		autoclose:true
	});
	$("#maskedDate").bind("change", function(){query();});
	$("#buildNo").bind("keyup", doCdtTFKeyUp);
	$("#a_backToImageList").bind("click",pageBack);
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
	
	$("#imageTable").html("");
	$("#ul_pagination").remove();
	$("#pagination_box").html('<ul id="ul_pagination" class="pagination-sm"></ul>');
	var pageSize = $("#grid_pageSize").val();
	var buildNo = $("#buildNo").val();
	var status = 1; //1=快照 2=开发 3=测试 4=生产
	var orders = "ID DESC";
	
	var ps = {pageNum:pageNum,pageSize:pageSize,defId:DefId,buildNo:buildNo,status:status,orders:orders};
	
	var d = $("#maskedDate").val();
	if(!CU.isEmpty(d)) {
		d = d.replace(/-/g, "");
		var d1 = d + "000000";
		var d2 = d + "235959";
		ps.startBuildTime = d1;
		ps.endBuildTime = d2;
	}
	
	RS.ajax({url:"/dev/image/queryImageInfoPage",ps:ps,cb:function(r) {
		if(!CU.isEmpty(r)){
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
		        	query(page);
		        }
		    	
		    });
			$('#imageTable-tmpl').tmpl(r).appendTo("#imageTable");
			for(var i=0; i<data.length; i++) {
				$("#a_remove_image_"+data[i].image.id).bind("click",function(){
					var obj = CurrDataMap["key_"+this.id.substring(this.id.lastIndexOf("_")+1)];
					$("#span_title").val(obj.image.imageFullName);
					removeImageDef(obj.image.id);
				});
			}
		}
	}});
	
}

function removeImageDef(id){
	var obj = CurrDataMap["key_"+id];
	CC.showMsg({msg:"您确定要删除镜像[<font color='blue'>"+obj.image.imageFullName+"</font>]吗?",option:2,callback:function(r) {
		if(r != "ok") return ;
		RS.ajax({url:"/dev/image/removeImageById",ps:{id:id},cb:function() {
			query(ParamPageNum);
		}});
	}});
}

function pageBack() {
	var url = ContextPath + "/dispatch/mc/10306?1=1";
	if(!CU.isEmpty(ParamPageNum)) url += "&pageNum="+ParamPageNum;
	window.location = url;
}