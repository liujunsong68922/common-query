/**
 * 这个类用来封装后台获取的所有有效元素信息
 */
class CWebDWMyUIComponent{
	/**
	 * constructor function
	 */
	constructor(uiobj,parentname,parentobj, _convertRate){
		//转换比例，默认为0.3
		this.convertRate = 0.3;
		
		//记录parentname
		this.parentname = parentname;
		
		//记录parentobj
		this.parentobj = parentobj;
		
		//如果传入转换比例，则使用新的转换比例
		if(_convertRate !=null){
			this.convertRate = _convertRate;
		}
		
		// 通用属性
		this.id = uiobj.id;
		this.name = uiobj.name;
		this.classname = uiobj.classname;
		this.text = uiobj.text;
		this.rowid = uiobj.rowid;
		this.colid = uiobj.colid;
		
		// 位置信息,乘以转换比例
		this.left = uiobj.left * this.convertRate;
		this.top = uiobj.top * this.convertRate;
		this.width = uiobj.width * this.convertRate;
		this.height = uiobj.height * this.convertRate;
		
		// 组合框的特定信息
		this.values = uiobj.values;
		this.selectedIndex = uiobj.selectedIndex;
		
		// 单选钮的特定信息
		this.children = uiobj.childElements;
		
		// 检查框的特定信息
		this.value = uiobj.value;
		
		// 线条的特定信息
		this.x1 = uiobj.x1 * this.convertRate;
		this.y1 = uiobj.y1 * this.convertRate;
		this.x2 = uiobj.x2 * this.convertRate;
		this.y2 = uiobj.y2 * this.convertRate;
		
		// 节点信息初始化
		this.node = null;
		this.textnode = null;
		
		this.toHTMLNode();
	}
	
	/**
	 * convert this object to HTML DOM node
	 */
	toHTMLNode(){
		var node=document.createElement("div");
		node.style.position="absolute";
		node.style.left  = this.left + "px";
		node.style.top = this.top +"px";
		node.style.width  = this.width + "px";
		node.style.height = this.height +"px"
		node.style.background ="#f0f0f0";
		node.style.fontSize ="smaller";
		var nodeid = this.name;
		nodeid = nodeid.replace("targPict",this.parentname);
		node.id = nodeid;
		// 根据传入的参数的属性来进行判断走分支
		if(this.classname == "" ){
			this.classname = "JLabel";
		}
		
		// 创建一个TextNode代表JLabel，存储显示标签值
		if(this.classname == "JLabel"){
			// 创建一个TextNode代表JLabel，存储显示标签值
			var textnode=document.createTextNode(this.text);
			// 设置textnode的默认样式
			node.appendChild(textnode);
			this.node = node;
			this.textnode = textnode;
			return node;
		}
		
		// 创建一个输入文本框，存储显示标签值
		if(this.classname == "JTextField"){
            var input = document.createElement("input");
            input.type = "text";
            input.value = this.text;
            input.length = this.width+"px";
            input.rowid = this.rowid;
            input.colid = this.colid;
            
            input.id = nodeid+"_text";
            
            //when onclick,set currentrow
            input.onclick = function(){
            	//alert(input.value);
            	setRow(input.rowid);
            	console.log("begin set parentobj.rowid");
            	golbal_rowid= input.rowid;
            	//parentobj.rowid = input.rowid;
            }
            
            //when datachange, modify buffer data value
            input.onchange = function(){
            	//alert("onchange");
            	setItem(input.rowid,input.colid,input.value);
            }
            node.appendChild(input);
			this.node = node;
			this.textnode = input;
			return node;
		}
		
		// 创建一个下拉选择列表
		if(this.classname == "JComboBox"){
            var input = document.createElement("select");
            // input.value = this.text;
            input.length = this.width+"px";
            console.log("combo values:"+this.values);
            // 根据返回的values字段，初始化下拉列表的数据项
            var combovalues = this.values.split("/");
            for (var combo_id = 0; combo_id <combovalues.length; combo_id++){
            	var data = combovalues[combo_id].split("\t");
            	if(data.length>1){
            		
            		var option = document.createElement("option");
            		option.setAttribute("value",data[0]);// 设置option属性值
            		option.appendChild(document.createTextNode(data[1]));
            	}
            	input.appendChild(option);
            }
            input.selectedIndex = this.selectedIndex;
            // alert("selectedIndex:"+this.selectedIndex);
            
            node.appendChild(input);
			this.node = node;
			this.textnode = input;
			return node;
		}
		
		// 开始进行单选钮的处理过程，单选钮是封装在一个JPanel里面的
		if(this.classname == "JPanel"){
			console.log("JPanel子节点数："+this.children.length);
			for(var i=0;i<this.children.length;i++){
				var mychild = this.children[i];
				
				// 处理单选按钮
				if(mychild.classname == "JRadioButton"){
					var childdiv = document.createElement("div");
					childdiv.style.top=(i * 20)+"px";
					childdiv.style.left = "0px";
					
					var childnode = document.createElement("input");
					childnode.type = "radio";
					childnode.name = this.name+"_radio";
					//设置是否选中的状态
					childnode.checked = mychild.selected;
					console.log("childnode.name:"+childnode.name);
					// 单选钮的value属性放在Tag字段里面
					childnode.value = mychild.Tag;
					console.log("childnode.value:"+childnode.value);
					// 创建一个文本节点
					console.log("mychild.Text:"+mychild.Text);
					var textnode=document.createTextNode(mychild.Text);
					
					
					childdiv.appendChild(childnode);
					childdiv.appendChild(textnode);
					node.appendChild(childdiv);
		            this.node = node;
		            this.textnode = null;
				}
			} 
			return node;
		}
		
		//开始进行复选框的编辑显示支持
		if(this.classname == "JCheckBox"){
            var input = document.createElement("input");
            input.type = "checkbox";
            input.checked = this.value;
            //input.length = this.width+"px";
            //再增加一个文本节点来显示
            var textnode=document.createTextNode(this.text);
            node.appendChild(input);
            node.appendChild(textnode);
			this.node = node;
			this.textnode = input;
			return node;			
			
		}
		console.log("不支持的classname:"+this.classname);
		this.node = node;
		return node;
	}
}	