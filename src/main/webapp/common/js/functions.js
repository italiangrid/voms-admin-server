
function toggleVisibility(url, element_name, img_path){

    var el_name = element_name+"_content";
    var el = document.getElementById(el_name)
   	var button_element = document.getElementById(element_name+"_button");

	if (button_element == null){
		alert(element_name+"_button is null!")
		return
	}
	
	if (el.style.display == '')
		el.style.display = 'block';
	
	if (Element.visible(el_name)){
		
		button_element.src= img_path+"/maximize.png";
		storeStatus(url,element_name,false)
	
	}else{
	
         button_element.src=img_path+"/minimize.png";
		storeStatus(url, element_name, true)
	}
	
	Element.toggle(el_name)

}

function storeStatus(url,element_name, status){
	
	url = url+"?method=storeStatus&panelId="+element_name+"&status="+status

	req = new XMLHttpRequest()
	req.onreadystatechange = handleStoreStatus
	
	try{
	
		req.open("GET",url,true)
		
	}catch(e){
		alert("Problem communicating with server!\n"+e)
	}
	
	req.send(null)

}

function getStatus(url,element_name,img_path){

	url = url+"?method=getStatus&panelId="+element_name

	req = new XMLHttpRequest()
	req.onreadystatechange = new handleGetStatus(url,element_name,img_path)
	
	try{
	
		req.open("GET",url,true)
		
	}catch(e){
		alert("Problem communicating with server!\n"+e)
	}
	
	req.send(null)	
	
}

function handleStoreStatus(){

	if (req.readyState == 4){
		if (req.status == 200){
			// Ok!
		}else {
		
			alert("Problem with server response!\n"+req.statusText)
		}
	}
}


function handleGetStatus(url, element_name, img_path){

	this.url = url
	this.element_name = element_name
	this.img_path = img_path
	
	var me = this
	
	this.handleEvent = function(){
		if (req.readyState == 4){
			if (req.status == 200){
		
				var response = req.responseXML
				alert('ResponseText: '+req.responseText)
				
				var el = response.getElementById(me.element_name)
				alert(el)
				
			}else {
			
				alert("Problem with server response!\n"+req.statusText)
			}
		}
	}
}

function getCookie(name){

	var cookie = document.cookie.match(new RegExp('(^|;)\\s*' + escape(name) + '=([^;\\s]*)'));
	return (cookie ? unescape(cookie[2]) : null);
}

function setCookie(name, value){

	return (document.cookie= escape(name)+ '=' + escape(value || '') + expire);
}

function ask_confirm(msg, dest, cancel_msg){

    var conf_box = window.confirm(msg)
    
    if (conf_box)
        window.location = dest
    else{
    
        // Get messages div
        var messages = document.getElementById("messages")
        if (messages == null){
            return
        }
        
        if (!cancel_msg)
            return

        messages.innerHTML = "<div class='messages'>"+cancel_msg+"</div>";
        
    }
}
