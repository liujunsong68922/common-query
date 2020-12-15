var xmlHttpReturn; // 定义Ajax调用以后产生的全局返回值存放位置,Ajax调用通过jQuery实现
var _webdwui = new Map();
var golbal_rowid=0;

//回调函数
function WebdwUI_callback(index) {
	console.log("enter callback");
	var status = xmlHttpReturn.status;
	console.log("status:"+status);

	if(status == 200){
		if(_webdwui.get(index)){
			var view = _webdwui.get(index);
			view.uuid = xmlHttpReturn.uuid;
			view.uiArray = xmlHttpReturn.uiobjList;
			view.removeAllChild();
			view.drawAllChild();	
			alert(xmlHttpReturn.message);
		}else{
			alert("canot find webdwui.");
			alert("index:"+index);
		}
	}else{
		console.log(xmlHttpReturn.message);
		alert("Message:" + xmlHttpReturn.message);
	}
}

/**
 * 绘图展示对象，从后台获取数据集合，并在前台负责完成展示工作
 */
class CWebDWUIView{
	// constructor function
	constructor(_parentId){
		this.uiArray = new Array();
		this.parentId = _parentId; // 绘图的根节点对象的ID和名称,代表一个界面上的DIV对象
		this.parentName = _parentId; // 名称与ID同名（基本约定）
		this.parentDom  = document.getElementById(_parentId); // 绘图的根节点对象
		this.uuid =""; //记录uuid的值
		this.rowid = 0; //当前行
		this.colid = 0; //当前列序号
		//保存对于对象的引用
		//_webdwui = this;
		_webdwui.set(_parentId,this);
	}
	
	// clean function
	// 删除设定根节点下面的所有子节点
	removeAllChild(){
		if(this.parentDom == null){
			console.log("parentDom is null;")
			return;
		}
		var children = this.parentDom.children;
		
		console.log("begin remove:children.length = "+children.length);
		
		for(var i=0;i<children.length;){
			// 从第一个开始删除，于是一个一个全删除掉了
			if(children[0] !=null){
				this.parentDom.removeChild(children[0]);
			}
		}
	}
	// draw function
	// draw all element in uiArray
	drawAllChild(){
		//this.uiArray = xmlHttpReturn;
		
		if(this.uiArray == null){
			console.log("uiArray is null,exit drawAllChild.");
			return;
		}
		if(this.parentDom == null){
			console.log("parentDom is null,exit drawAllChild.");
			return;
		}
		// 循环绘制所有对象
		// todo:根据classname来走分支对象进行DOM元素绘制
		for(var i=0;i<this.uiArray.length;i++){
			// get ui description
			var ui = this.uiArray[i];
			// convert it to dom object
			var parentname = this.parentDom.id;
			console.log("parentname:"+parentname);
			
			var uiComponent = new CWebDWMyUIComponent(ui,parentname,this.parentDom);
			// append it to parentObject
			//console.log("i,classname:"+i+" ,"+uiComponent.classname);
			this.parentDom.appendChild(uiComponent.node);
		}
	}	
		
	/**
	 * setDataobject function 这一方法用来在界面上设置数据窗口对象
	 */
	setDataobject(token,dwname){
		var surl="/webdw/setdataobject?token="+token+"&dwname="+dwname;
		//add parentId argment.
		surl = surl +"&parentId="+this.parentId;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				// 调用回调函数时，将data里面的parentId作为参数
				WebdwUI_callback(data.parentId);
            }
		});
	}
	
	/**
	 * setDataobject function 这一方法用来在界面上设置数据窗口对象
	 */
	insertrow(token,uuid){
		var surl="/webdw/insertrow?token="+token+"&uuid="+uuid;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				WebdwUI_callback(data.parentId);
            }
		});
	}
	/**
	 * setDataobject function 这一方法用来在界面上设置数据窗口对象
	 */
	deleterow(token,uuid){
		var surl="/webdw/deleterow?token="+token+"&uuid="+uuid+"&rowid="+this.rowid;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				WebdwUI_callback(data.parentId);
            }
		});
	}	
	/**
	 * 这一方法用来在界面上设置数据窗口对象，并检索后台数据
	 */
	retrieve(token,dwname,args){
		var surl="/webdw/retrieve?token="+token+"&dwname="+dwname+"&args="+args;
		// 增加parentId参数
		surl = surl + "&parentId="+this.parentId;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				WebdwUI_callback(data.parentId);
				//return xmlHttpReturn;
            }
		});
	}
	
	/**
	 * 这一方法用来在界面上设置数据窗口对象，并检索后台数据
	 */
	setrow(uuid,rowid){
		this.rowid = rowid;
		//var surl="/webdw/setrow?uuid="+uuid+"&rowid="+rowid;
		// 增加随机数
		//surl = surl+"&rand="+(Math.random()*100);
		
		//xmlHttpReturn="";
//		$.ajax({
//            type: "GET",
//            url: surl,
//            data: "",
//            async:false,
//            dataType: "json",
//            success: function(data){
//            	// get return data.
//				console.log(data);
//				xmlHttpReturn = data;
//				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
//				//WebdwUI_callback();
//				return xmlHttpReturn;
//            }
//		});
	}
	
	/**
	 * 这一方法用来在界面上设置数据窗口对象，并检索后台数据
	 */
	setitem(uuid,rowid,colid,data){
		this.rowid = rowid;
		var surl="/webdw/setitem?uuid="+uuid+"&rowid="+rowid+"&colid="+colid+"&data="+data;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				//WebdwUI_callback();
				return xmlHttpReturn;
            }
		});
	}
	
	/**
	 * 这一方法用来在界面上设置数据窗口对象，并检索后台数据
	 */
	update(token,uuid){
		//this.rowid = rowid;
		var surl="/webdw/update?token="+token+"&uuid="+uuid;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				WebdwUI_callback(data.parentId);
				return xmlHttpReturn;
            }
		});
	}
	
		/**
	 * 这一方法用来在界面上设置数据窗口对象，并检索后台数据
	 */
	retrievebysql(token,strsql,stype){
		var surl="/webdw/retrievebysql?token="+token+"&strsql="+strsql+"&stype="+stype;
		// 增加随机数
		surl = surl+"&rand="+(Math.random()*100);
		
		xmlHttpReturn="";
		$.ajax({
            type: "GET",
            url: surl,
            data: "",
            async:false,
            dataType: "json",
            success: function(data){
            	// get return data.
				console.log(data);
				xmlHttpReturn = data;
				// 调用全局性的回调函数，回调函数在调用者的界面上进行定义
				WebdwUI_callback();
				return xmlHttpReturn;
            }
		});
	}
	
}


