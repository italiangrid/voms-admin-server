function _gel(ig_){return document.getElementById?document.getElementById(ig_):null}

function _gelstn(ig_){if(ig_=="*"&&document.all)return document.all;return document.getElementsByTagName?document.getElementsByTagName(ig_):new Array}

function _uc(ig_){return ig_.toUpperCase()}

function _trim(ig_){return ig_.replace(/^\s*|\s*$/g,"")}

function _esc(ig_){return window.encodeURIComponent?encodeURIComponent(ig_):escape(ig_)}

var ig_va=function(ig_){return window.decodeURIComponent?decodeURIComponent(ig_):unescape(ig_)}

;var _unesc=ig_va;function _hesc(ig_){ig_=ig_.replace(/</g,"&lt;").replace(/>/g,"&gt;");ig_=ig_.replace(/"/g,"&quot;").replace(/'/g,"&#39;");return ig_}
function _striptags(ig_){return ig_.replace(/<\/?[^>]+>/gi,"")}
var ig_ua=0;function _uid(){return"obj"+ig_ua++}
function _min(ig_,ig_a){return ig_<ig_a?ig_:ig_a}
function _max(ig_,ig_a){return ig_>ig_a?ig_:ig_a}
function _IG_LoadScript(ig_,ig_a){_sendx(ig_,function(ig_b){window.eval(ig_b);eval(ig_a)}
,false,null)}
var ig_ra=navigator.userAgent.indexOf("Safari")>=0;function _sendx(ig_,ig_a,ig_b,ig_c){var ig_e=ig_wa();if(!ig_c)ig_c=null;if(!ig_e||ig_ra&&!ig_a){(new Image).src=ig_}else{ig_e.open(ig_c?"POST":"GET",ig_,true);if(ig_a){ig_e.onreadystatechange=function(){if(ig_e.readyState==4){ig_a(ig_b&&ig_e.responseXML?ig_e.responseXML:ig_e.responseText)}}
}ig_e.send(ig_c)}}
function _uhc(ig_,ig_a,ig_b){var ig_c="m_"+ig_+"_"+ig_a;var ig_e=_gel(ig_c);if(!ig_e){ig_e=document.createElement("INPUT");ig_e.type="hidden";ig_e.disabled=true;ig_e.name=ig_c;_gel("m_"+ig_+"_form").appendChild(ig_e)}ig_e.value=ig_b;ig_e.disabled=false}
function ig_wa(){var ig_=null;if(window.ActiveXObject){ig_=new ActiveXObject("Msxml2.XMLHTTP");if(!ig_){ig_=new ActiveXObject("Microsoft.XMLHTTP")}}else if(window.XMLHttpRequest){ig_=new XMLHttpRequest}return ig_}
var _et="";var _pid="";var _authpath="";var _prefid="";var _setp_url="/ig/setp";var ig_na="/ig/game";var ig_s=null;var ig_t=null;function ig_m(ig_,ig_a,ig_b){var ig_c=document.createElement("input");ig_c.type="hidden";ig_c.name=ig_a;ig_c.value=ig_b;ig_.appendChild(ig_c)}
function ig_I(){var ig_=new Object;var ig_a=document.location.search.substring(1);var ig_b=ig_a.split("&");for(var ig_c=0;ig_c<ig_b.length;ig_c++){var ig_e=ig_b[ig_c].indexOf("=");if(ig_e==-1)continue;var ig_f=ig_b[ig_c].substring(0,ig_e);var ig_g=ig_b[ig_c].substring(ig_e+1);ig_g=ig_g.replace(/\+/g," ");ig_[ig_f]=_unesc(ig_g)}return ig_}
function ig_r(){var ig_={pid:1,host:1,hl:1};var ig_a=ig_I();var ig_b="?";for(var ig_c in ig_a){if(ig_[ig_c]){ig_b+=ig_c+"="+_esc(ig_a[ig_c])+"&"}}return ig_b}
function _fsetp(ig_,ig_a,ig_b){ig_.action=_setp_url;ig_.method="get";ig_m(ig_,"url",document.location);ig_m(ig_,"et",_et);ig_m(ig_,"pid",_pid);ig_m(ig_,"ap",_authpath);ig_m(ig_,"prefid",_prefid);ig_m(ig_,"m_"+ig_a+"_t",ig_b);var ig_c=ig_I();var ig_e=ig_c["host"];var ig_f=ig_c["hl"];if(typeof ig_e!="undefined"){ig_m(ig_,"host",ig_e)}if(typeof ig_f!="undefined"){ig_m(ig_,"hl",ig_f)}return true}
var ig_x=new Array;var ig_y=false;function ig_L(){if(ig_x.length==0){ig_y=false;return}ig_y=true;_sendx(ig_x.shift(),ig_L,false,null)}
function _xsetp(ig_){ig_x.push(_setp_url+ig_r()+"et="+_et+"&pid="+_pid+"&ap="+_authpath+"&prefid="+_prefid+"&"+ig_);if(!ig_y){ig_L()}}
function _dlsetp(ig_,ig_a){if(!ig_a){ig_a=_esc(document.location)}document.location=_setp_url+ig_r()+"et="+_et+"&pid="+_pid+"&ap="+_authpath+"&prefid="+_prefid+"&url="+ig_a+"&"+ig_}
function _dlgame(ig_,ig_a,ig_b){if(!ig_b){ig_b=_esc(document.location)}document.location=ig_na+ig_r()+"et="+_et+"&game="+ig_+"&url="+ig_b+"&"+ig_a}
function _ssbc(ig_,ig_a,ig_b){var ig_c=_gelstn("*");for(var ig_e=0;ig_e<ig_c.length;ig_e++){if(ig_c[ig_e].className==ig_){ig_c[ig_e].style[ig_a]=ig_b}}}
function ig_sa(ig_){var ig_a=_gelstn("*");for(var ig_b=0;ig_b<ig_a.length;ig_b++){for(var ig_c=0;ig_c<ig_.length;ig_c++){if(ig_a[ig_b].className==ig_[ig_c][0]){ig_a[ig_b].style[ig_[ig_c][1]]=ig_[ig_c][2]}}}}
function _edit(ig_,ig_a){_gel("m_"+ig_).className="modbox_e";if(ig_a){ig_a()}ig_u("edit","m_"+ig_);return false}
function _cedit(ig_){_gel("m_"+ig_).className="modbox";_gel("m_"+ig_+"_form").reset();ig_u("canceledit","m_"+ig_);return false}
function _del(ig_,ig_a,ig_b){_xsetp("m_"+ig_+"_enab=0&m_"+ig_+"_t="+ig_a);var ig_c=_gel("undel_msg");if(ig_c){_gel("undel_title").innerHTML=_striptags(_gel("m_"+ig_+"_h").innerHTML)+" ";ig_c.style.display="block"}var ig_e=_gel("m_"+ig_);if(ig_e){ig_e.style.display="none"}ig_s=ig_;ig_t=ig_b;var ig_f=_gel(ig_b);if(ig_f){ig_f.style.display=""}_mod=true;ig_u("delete","m_"+ig_);return false}
function _delpromobox(){_xsetp("hp=0");var ig_=_gel("promobox");ig_.parentNode.removeChild(ig_);return false}
function _delmessage(){_xsetp("gm=0");var ig_=_gel("googlemessage");ig_.parentNode.removeChild(ig_);return false}
function _delpromomod(){_xsetp("pm=0");var ig_=_gel("promomod");ig_.parentNode.removeChild(ig_);return false}
function _undel(){if(ig_s==null)return;var ig_="m_"+ig_s;var ig_a=_gel(ig_);if(ig_a){ig_a.style.display="block";_xsetp("undel")}var ig_b=_gel("undel_msg");if(ig_b){ig_b.style.display="none"}if(ig_t!=null){var ig_c=_gel(ig_t);if(ig_c){ig_c.style.display="none"}}ig_s=null;ig_t=null;ig_u("undelete",ig_)}
function _zm(ig_,ig_a){var ig_b=_gel("m_"+ig_+"_b");if(ig_b){var ig_c=ig_b.style.display!="none";ig_b.style.display=ig_c?"none":"block";var ig_e=_gel("m_"+ig_+"_zippy");if(ig_e){if(ig_c){ig_e.className=ig_e.className.replace(/minbox/,"maxbox")}else{ig_e.className=ig_e.className.replace(/maxbox/,"minbox")}}_xsetp("mz="+ig_+":"+(ig_c?"1":"0")+"&t="+ig_a)}return false}
var ig_ma="https://www.google.com/accounts";function _enableGS(ig_,ig_a){ig_.action=ig_ma+"/CheckCookie";ig_.method="get";ig_m(ig_,"service",ig_a);ig_m(ig_,"continue",document.location);ig_m(ig_,"skipvpage",true);return true}
function _reload(ig_){var ig_a=ig_-(new Date).getTime();if(ig_a>1000){setTimeout("_reload("+ig_+")",ig_a);return}document.cookie="IGREL=1";document.location.reload()}
function ig_u(ig_,ig_a){switch(ig_){case "delete":case "undelete":case "edit":case "canceledit":case "zip":case "unzip":_IG_TriggerDelayedModuleEvent(ig_,ig_a,0);_IG_TriggerDelayedEvent("module"+ig_,0,ig_a);break}}
function ig_ta(ig_){_xsetp("pnlo="+(ig_?1:0))}
var _uli;var _pnlo;var _mpnlo;var _pl;var _mod;var _cbp=false;var ig_J=false;var ig_K=false;var _table=null;var _tabs=null;var _insert_to_col=1;function _upc(){var ig_=new Array;if(!_cbp){ig_[ig_.length]=["medit","display",_uli?"":"none"]}ig_[ig_.length]=["panelo","display",_pnlo?"":"none"];ig_[ig_.length]=["panelc","display",_pnlo?"none":""];if(_mod){ig_[ig_.length]=["unmod","display","none"];ig_[ig_.length]=["mod","display",""]}else{ig_[ig_.length]=["mod","display","none"];ig_[ig_.length]=["unmod"
,"display",""]}ig_sa(ig_);if(_pl){if(_cbp||_uli){if(!ig_J&&!_mpnlo){_IG_initDrag(_table,_tabs);ig_J=true}else if(!ig_K&&_mpnlo){_IG_initMobileDrag(_table);ig_K=true}}}}
var ig_q=0;function _tp(ig_){if(ig_q>0)clearInterval(ig_q);_pnlo=ig_;_mod=true;ig_ta(ig_);_upc();var ig_a=_gel("cpnl");var ig_b=_gel("cpnlc");var ig_c=_gel("nhdr");var ig_e=_gel("ehdr");var ig_f=ig_a.offsetWidth;var ig_g;var ig_h;if(ig_){ig_g=ig_b.offsetWidth;ig_h="visible";ig_e.style.display="";ig_c.style.display="none"}else{ig_g=1;ig_h="hidden";ig_e.style.display="none";ig_c.style.display=""}ig_a.style.overflow="hidden";var ig_j=100;var ig_p=10;var ig_E=0;ig_q=setInterval(function(){var ig_F=ig_E/
ig_p;var ig_ca=ig_f+(ig_g-ig_f)*ig_F;ig_a.style.width=ig_ca+"px";ig_E++;if(ig_F>=1){clearInterval(ig_q);ig_q=0;ig_a.style.width=ig_g+"px";ig_a.style.overflow=ig_h}}
,ig_j/ig_p);return false}
function _ts(ig_,ig_a){var ig_b=_gel(ig_+ig_a);var ig_c;if(ig_b.className=="mlist_open"){ig_b.className="mlist_closed";ig_c="pnlsc"}else{ig_b.className="mlist_open";ig_c="pnlso"}_xsetp(ig_c+"="+_esc(ig_a));return false}
function _add_m(ig_,ig_a){_dlsetp(ig_+_esc("&col="+_insert_to_col),ig_a)}
function _add_m_confirm(ig_,ig_a,ig_b){if(confirm(ig_a)){_add_m(ig_,ig_b)}}
function _add_f(ig_){_add_m("n_25="+_esc("url="+_esc(ig_)))}
var ig_G=/^_add_m(_confirm)?\(\"[^"]+\"(, *\"[^"]+\")?\)$/;function _find_feed(ig_){var ig_a="acd";if(!ig_){var ig_b=_gelstn("div");if(ig_b){for(var ig_c=0;ig_c<ig_b.length;ig_c++){if(ig_b[ig_c].id&&ig_b[ig_c].id.indexOf("ps")==0&&ig_b[ig_c].className=="mlist_open"){_ts("ps",ig_b[ig_c].id.substring(2))}}}if(_gel("add_custom")){ig_=_gel("add_custom").value}}if(!ig_||ig_==""){ig_a="advdsrch";ig_=_gel("add_advd").value}var ig_e=_gel("ffresults");if(ig_e){ig_e.style.display="none"}var ig_f=_gel("ffloading"
);if(ig_f){ig_f.style.display="block"}_sendx("/ig/feeds"+ig_r()+"q="+_esc(ig_)+"&page="+_esc(ig_a),ig_la,false,null);return false}
function ig_la(ig_){var ig_a=_gel("ffloading");if(ig_a){ig_a.style.display="none"}var ig_b=_gel("ffresults");if(ig_b){ig_b.style.display="block"}if(ig_.length>0&&ig_.charAt(0)=="<"){ig_b.innerHTML=ig_}else if(ig_.match(ig_G)!=null){eval(ig_)}else{eval(ig_)}}
function _add_remote_module(ig_,ig_a){_sendx("/ig/feeds"+ig_r()+"module=1&q="+_esc(ig_),function(ig_b){ig_a();ig_fa(ig_b)}
,false,null);return false}
function ig_fa(ig_){var ig_a=/^alert\(\"[^"]+\"\)$/;if(ig_.match(ig_G)!=null||ig_.match(ig_a)!=null){eval(ig_)}}
var _cet=-1;function _editTab(ig_){if(_cet!=-1)return;_renameTab();_cet=ig_;_gel("tab"+_cet+"_view").style.display="none";_gel("tab"+_cet+"_edit").style.display="";_gel("tab"+_cet+"_title").select();_gel("tab"+_cet+"_title").focus()}
function _renameTab(){if(_cet==-1)return;var ig_=_gel("tab"+_cet+"_title").value;_xsetp("rt_"+_cet+"="+_esc(ig_));_gel("tab"+_cet+"_view").innerHTML=_hesc(ig_);var ig_a=_gel("tip_tabtitle");if(ig_a){ig_a.innerHTML=_hesc(ig_)}_gel("tab"+_cet+"_edit").style.display="none";_gel("tab"+_cet+"_view").style.display="";_cet=-1;return false}
function _ListApp(ig_,ig_a,ig_b){this.items=ig_;this.deleted=[];this.item_constructor=ig_a;this.module_id=ig_b;this.app_name="m_"+ig_b+"_App";this.display_area=_gel("m_"+ig_b+"_disp");this.value_input_field=_gel("m_"+ig_b+"_val");this.name_input_field=_gel("m_"+ig_b+"_name");if(!this.name_input_field){this.name_input_field=this.value_input_field}this.default_name=this.name_input_field.value;this.default_value=this.value_input_field.value}
_ListApp.prototype.sort=function(ig_,ig_a){return ig_.t(ig_a)}
;_ListApp.prototype.Z=function(){var ig_="<table cellspacing=0 cellpadding=0 border=0>";var ig_a="";var ig_b=this.items;for(var ig_c=0;ig_c<ig_b.length;ig_c++){if(!ig_b[ig_c]){this.items.splice(ig_c,1);ig_c--}else{ig_+="<tr><td><font size=-1>"+ig_b[ig_c].M()+'</font></td><td><a href="#" onclick="'+this.app_name+".del("+ig_c+')"><img src="/ig/images/x.gif" width=16 height=13 border=0></a></td></tr>';if(parseInt(ig_b[ig_c]._uid)<0){ig_a+=ig_b[ig_c].s(ig_c)}}}var ig_e=this.deleted;var ig_f="";for(var ig_c=
0;ig_c<ig_e.length;ig_c++){if(parseInt(ig_e[ig_c]._uid)>=0){ig_f+=","+ig_e[ig_c]._uid}}ig_+="</table><input type=hidden name=m_"+this.module_id+'_add value="'+ig_a+'"><input type=hidden name=m_'+this.module_id+'_del value="'+ig_f+'">';return ig_}
;_ListApp.prototype.refresh=function(){this.items.sort(this.sort);this.display_area.innerHTML="<font size=-1>"+this.Z()+"</font>"}
;_ListApp.prototype.add=function(ig_,ig_a){if(!ig_){ig_=_trim(this.name_input_field.value)}if(!ig_a){ig_a=_trim(this.value_input_field.value)}var ig_b=new this.item_constructor(ig_,ig_a,-1);if(!ig_b.A())return;this.items[this.items.length]=ig_b;this.refresh();this.name_input_field.value=this.default_name;this.value_input_field.value=this.default_value}
;_ListApp.prototype.del=function(ig_){this.deleted[this.deleted.length]=this.items[ig_];this.items.splice(ig_,1);this.refresh()}
;function _ListItem(ig_,ig_a){this.init(ig_,ig_a)}
_ListItem.prototype.init=function(ig_,ig_a,ig_b){this._n=ig_;this._v=ig_a;this._uid=ig_b}
;_ListItem.prototype.A=function(){return this._n!=""}
;_ListItem.prototype.t=function(ig_){return 0}
;_ListItem.prototype.M=function(){return _hesc(this._n)}
;_ListItem.prototype.s=function(ig_){return"&"+_esc(this._n)+"="+_esc(this._v)}
;_BMListItem.prototype=new _ListItem;_BMListItem.prototype.constructor=_ListItem;_BMListItem.superclass=_ListItem.prototype;function _BMListItem(ig_,ig_a,ig_b){this.init(ig_,ig_a,ig_b)}
_BMListItem.prototype.A=function(){return _BMListItem.superclass.A.call(this)&&this._n!="http://"}
;_BMListItem.prototype.y=function(){if(this._v){return this._v}else{var ig_=this._n;if(ig_.indexOf("http://")==0){ig_=ig_.substring(7)}if(ig_.indexOf("www.")==0){ig_=ig_.substring(4)}return ig_}}
;_BMListItem.prototype.M=function(){return'<a href="'+this._n+'" target=bmwindow>'+_hesc(this.y())+"</a>"}
;_BMListItem.prototype.s=function(ig_){return"&b"+ig_+"="+_esc(this._n)+"&t"+ig_+"="+_esc(this._v)}
;_BMListItem.prototype.t=function(ig_){var ig_a=_uc(this.y());var ig_b=_uc(ig_.y());if(ig_a==ig_b)return 0;return ig_a<ig_b?-1:1}
;_WthrListItem.prototype=new _ListItem;_WthrListItem.prototype.constructor=_ListItem;_WthrListItem.superclass=_ListItem.prototype;function _WthrListItem(ig_,ig_a,ig_b){this.init(ig_,ig_a,ig_b)}
_WthrListItem.prototype.s=function(ig_){var ig_a="&"+_esc(this._n);if(this._v){ig_a+="="+_esc(this._v)}return ig_a}
;_FListItem.prototype=new _ListItem;_FListItem.prototype.constructor=_ListItem;_FListItem.superclass=_ListItem.prototype;function _FListItem(ig_,ig_a,ig_b,ig_c){this.init(ig_,ig_a,ig_b);if(ig_c){this._s=ig_c}else{this._s=0}}
_FListItem.prototype.s=function(ig_){return"&"+_esc(this._n)}
;_FListItem.prototype.t=function(ig_){var ig_a=this;if(ig_a._s<ig_._s)return-1;if(ig_a._s>ig_._s)return 1;var ig_b=_uc(ig_a._n);var ig_c=_uc(ig_._n);if(ig_b<ig_c)return-1;if(ig_b>ig_c)return 1;return 0}
;
function _IG_Callback(ig_){var ig_a=arguments;return function(){var ig_b=[];for(var ig_c=0;ig_c<arguments.length;ig_c++){ig_b[ig_b.length]=arguments[ig_c]}for(var ig_c=1;ig_c<ig_a.length;ig_c++){ig_b[ig_b.length]=ig_a[ig_c]}ig_.apply(null,ig_b)}
}
;
var ig_i=new Object;var ig_v={abort:1,blur:1,change:1,click:1,close:1,dragdrop:1,error:1,focus:1,keydown:1,keypress:1,keyup:1,load:1,mousedown:1,mousemove:1,mouseout:1,mouseover:1,mouseup:1,paint:1,reset:1,resize:1,scroll:1,select:1,submit:1,unload:1};var ig_k="ig_window";function _IG_AddEventHandler(ig_,ig_a){if(ig_v[ig_]&&window["on"+ig_]&&(!ig_i[ig_k]||!ig_i[ig_k][ig_])){var ig_b=window["on"+ig_];window["on"+ig_]=null;_IG_AddEventHandler(ig_,ig_b)}if(!ig_i[ig_k]||!ig_i[ig_k][ig_]){if(!ig_i[ig_k]
){ig_i[ig_k]=new Object}ig_i[ig_k][ig_]=new Array;if(ig_v[ig_]){window["on"+ig_]=function(ig_e){if(!ig_e)ig_e=window.event;ig_o(ig_,ig_k,ig_e)}
}}var ig_c=ig_i[ig_k][ig_].length;if(ig_=="unload"&&ig_i[ig_k][ig_].length>0){ig_i[ig_k][ig_][ig_c]=ig_i[ig_k][ig_][ig_c-1];ig_i[ig_k][ig_][ig_c-1]=ig_a}else{ig_i[ig_k][ig_][ig_c]=ig_a}}
function _IG_AddModuleEventHandler(ig_,ig_a,ig_b){var ig_c=(ig_a+"").indexOf("m_")==0?ig_a:"m_"+ig_a;if(!ig_i[ig_c]||!ig_i[ig_c][ig_]){if(!ig_i[ig_c]){ig_i[ig_c]=new Object}ig_i[ig_c][ig_]=new Array}ig_i[ig_c][ig_][ig_i[ig_c][ig_].length]=ig_b}
function ig_X(){for(var ig_ in ig_i){for(var ig_a in ig_i[ig_]){for(var ig_b=0;ig_b<ig_i[ig_][ig_a].length;ig_b++){ig_i[ig_][ig_a][ig_b]=null}if(ig_==ig_k&&ig_v[ig_a]){window["on"+ig_a]=null}}}}
_IG_AddEventHandler("unload",ig_X);function _IG_RemoveEventHandler(ig_,ig_a){return ig_D(ig_,ig_k,ig_a)}
function _IG_RemoveModuleEventHandler(ig_,ig_a,ig_b){var ig_c=(ig_a+"").indexOf("m_")==0?ig_a:"m_"+ig_a;return ig_D(ig_,ig_c,ig_b)}
function ig_D(ig_,ig_a,ig_b){if(ig_i[ig_a]&&ig_i[ig_a][ig_]){for(var ig_c=0;ig_c<ig_i[ig_a][ig_].length;ig_c++){if(ig_i[ig_a][ig_][ig_c]===ig_b){ig_i[ig_a][ig_][ig_c]=null;return true}}}return false}
function _IG_TriggerEvent(ig_,ig_a,ig_b,ig_c){var ig_e=[ig_,ig_k];for(var ig_f=1;ig_f<arguments.length;ig_f++){ig_e[ig_e.length]=arguments[ig_f]}ig_o.apply(null,ig_e)}
function _IG_TriggerDelayedEvent(ig_,ig_a,ig_b,ig_c,ig_e){var ig_f=[ig_,ig_k];for(var ig_g=2;ig_g<arguments.length;ig_g++){ig_f[ig_f.length]=arguments[ig_g]}setTimeout(function(){ig_o.apply(null,ig_f)}
,ig_a)}
function _IG_TriggerDelayedModuleEvent(ig_,ig_a,ig_b,ig_c,ig_e,ig_f){var ig_g=(ig_a+"").indexOf("m_")==0?ig_a:"m_"+ig_a;var ig_h=[ig_,ig_g];for(var ig_j=3;ig_j<arguments.length;ig_j++){ig_h[ig_h.length]=arguments[ig_j]}setTimeout(function(){ig_o.apply(null,ig_h)}
,ig_b)}
function _IG_TriggerModuleEvent(ig_,ig_a,ig_b,ig_c,ig_e){var ig_f=(ig_a+"").indexOf("m_")==0?ig_a:"m_"+ig_a;var ig_g=[ig_,ig_f];for(var ig_h=2;ig_h<arguments.length;ig_h++){ig_g[ig_g.length]=arguments[ig_h]}ig_o.apply(null,ig_g)}
function ig_o(ig_,ig_a,ig_b,ig_c,ig_e){if(ig_i[ig_a]&&ig_i[ig_a][ig_]){for(var ig_f=0;ig_f<ig_i[ig_a][ig_].length;ig_f++){if(ig_i[ig_a][ig_][ig_f]){var ig_g=[];for(var ig_h=2;ig_h<arguments.length;ig_h++){ig_g[ig_g.length]=arguments[ig_h]}var ig_j=window;var ig_p=_gel(ig_a);if(ig_p)ig_j=ig_p;if(ig_g.length>0&&typeof ig_g[0].type!="undefined"){if(document.all&&ig_g[0].srcElement){ig_j=ig_g[0].srcElement}else if(ig_g[0].target){ig_j=ig_g[0].target}}ig_i[ig_a][ig_][ig_f].apply(ig_j,ig_g)}}}}
function _IG_RegisterOnloadHandler(ig_){_IG_AddEventHandler("domload",ig_)}
function _IG_LoadLibraryDeferred(ig_,ig_a){_IG_RegisterOnloadHandler(function(){_IG_LoadScript(ig_,ig_a)}
)}
var _IG_time={times:{epoch:(new Date).getTime()},set_epoch:function(ig_){_IG_time.times["epoch"]=ig_}
,epoch:function(){return _IG_time.times["epoch"]}
,start:function(ig_){_IG_time.times[ig_]=(new Date).getTime()}
,stop:function(ig_){var ig_a=_IG_time.times[ig_]?_IG_time.times[ig_]:_IG_time.epoch();_IG_time.times[ig_]=(new Date).getTime()-ig_a}
,get:function(ig_){return _IG_time.times[ig_]}
,print:function(ig_){document.write("<div style='color:#999999;font-size:75%'>"+ig_+" : "+_IG_time.get(ig_)+"ms</div>")}
,stop_and_print:function(ig_){_IG_time.stop(ig_);_IG_time.print(ig_)}
};if(window._IG_time_epoch){_IG_time.set_epoch(window._IG_time_epoch)};
function ig_6(ig_){var ig_a="";var ig_b=this.L(ig_,ig_a);return ig_b!=null?ig_b+"":ig_a}
function ig_0(ig_){var ig_a=0;var ig_b=parseInt(this.L(ig_,ig_a));return isNaN(ig_b)?ig_a:ig_b}
function ig_Z(ig_){return this.getInt(ig_)?true:false}
function ig_2(){return parseInt(this.m(_IG_Prefs.R,"0"))}
function ig_3(){return parseInt(this.m(_IG_Prefs.S,"0"))}
function ig_1(){return this.getString(".lang")}
function ig__(){return this.getString(".country")}
function ig_7(ig_,ig_a){return this.m(_IG_Prefs.I+ig_,ig_a)}
function ig_4(ig_){return this.m(_IG_Prefs.E+ig_,"")}
var ig_9=/(.*)(\<ph.*?\>\s*(\<ex\>(.*?)\<\/ex\>)?\s*%1\s*\<\/ph\>)(.*)/;function ig_5(ig_,ig_a){var ig_b=this.m(_IG_Prefs.E+ig_,"");var ig_c=ig_b.match(this.Y);if(!ig_c||!ig_c[0]){return ig_b}if(!ig_a){var ig_e=ig_c[4]||"";return ig_c[1]+ig_e+ig_c[5]}return ig_c[1]+ig_a+ig_c[5]}
function ig_$(ig_,ig_a){this.i[_IG_Prefs.I+ig_]=ig_a;var ig_b="m_"+this.X+"_up_"+ig_+"="+_esc(ig_a);if(_IG_Prefs.N){_xsetp(ig_b)}else{ifpc_SetPref(ig_,ig_a)}}
function ig_8(ig_,ig_a){if(typeof ig_a=="undefined"){ig_a=null}var ig_b=this.i[ig_];return typeof ig_b!="undefined"?ig_b:ig_a}
function ig_Y(){document.write("<pre>");for(var ig_ in this.i){document.writeln(ig_+" = "+this.m(ig_,null))}document.write("</pre>")}
function _IG_Prefs(ig_){this.getString=ig_6;this.getInt=ig_0;this.getBool=ig_Z;this.getCountry=ig__;this.getLang=ig_1;this.getMsg=ig_4;this.getMsgFormatted=ig_5;this.set=ig_$;this.dump=ig_Y;this.getModuleHeight=ig_2;this.getModuleWidth=ig_3;this.i=_IG_Prefs.i[_IG_Prefs.G+ig_]||new Object;this.m=ig_8;this.L=ig_7;this.X=ig_;this.Y=ig_9}
_IG_Prefs.R="h";_IG_Prefs.S="w";_IG_Prefs.G="m_";_IG_Prefs.I="up_";_IG_Prefs.E="msg_";_IG_Prefs._parseURL=function(ig_){_IG_Prefs.N=false;var ig_a=window.location.search.substring(1).split("&");for(var ig_b=0;ig_b<ig_a.length;ig_b++){var ig_c=ig_a[ig_b].indexOf("=");if(ig_c==-1)continue;var ig_e=ig_a[ig_b].substring(0,ig_c);ig_e=ig_e.replace(/\+/g," ");var ig_f=ig_a[ig_b].substring(ig_c+1);ig_f=ig_f.replace(/\+/g," ");ig_f=_unesc(ig_f);_IG_Prefs._add(ig_,ig_e,ig_f)}}
;_IG_Prefs._add=function(ig_,ig_a,ig_b){var ig_c=_IG_Prefs.G+ig_;if(typeof _IG_Prefs.i[ig_c]!="object"){_IG_Prefs.i[ig_c]=new Object}_IG_Prefs.i[ig_c][ig_a]=ig_b}
;_IG_Prefs.i=new Object;_IG_Prefs.N=true;
function ig_B(ig_,ig_a,ig_b){var ig_c="/ig/proxy?url="+escape(ig_);if(_et!=""){ig_c+="&et="+_et}_sendx(ig_c,ig_a,ig_b,null)}
function _IG_FetchContent(ig_,ig_a){ig_B(ig_,ig_a,false)}
function _IG_FetchXmlContent(ig_,ig_a){ig_B(ig_,ig_a,true)}
function ig_C(ig_,ig_a){var ig_b="/ig/feedjson";_sendx(ig_b,function(ig_c){var ig_e={};try{ig_e=eval("("+ig_c+")")}catch(ig_f){ig_e={}}for(var ig_g in ig_a){var ig_h=ig_e[ig_g]?ig_e[ig_g]:null;ig_a[ig_g](ig_h)}ig_e=null;ig_a=null}
,false,ig_)}
var ig_H=false;var ig_ka=0;var ig_n="";var ig_w={};function _IG_FetchFeedAsJSON(ig_,ig_a,ig_b,ig_c){var ig_e="fr_"+ig_ka++;var ig_f="url="+_esc(ig_);if(ig_b){ig_f+="&val="+_esc(ig_b)}if(ig_c){ig_f+="&sum=1"}var ig_g=ig_e+"="+_esc(ig_f);if(ig_H){var ig_h={};ig_h[ig_e]=ig_a;ig_C(ig_g,ig_h)}else{if(ig_n!="")ig_n+="&";ig_n+=ig_g;ig_w[ig_e]=ig_a}}
function ig_da(){ig_H=true;if(ig_n!=""){ig_C(ig_n,ig_w)}ig_n="";ig_w=null}
_IG_AddEventHandler("domload",ig_da);
var ig_l={obj:null,init:function(ig_,ig_a){ig_.onmousedown=ig_l.start;ig_.obj=ig_a;if(isNaN(parseInt(ig_a.style.left)))ig_a.style.left="0px";if(isNaN(parseInt(ig_a.style.top)))ig_a.style.top="0px";ig_a.onDragStart=new Function;ig_a.onDragEnd=new Function;ig_a.onDrag=new Function}
,uninit:function(ig_,ig_a){window.clearInterval(ig_a.Q);ig_.onmousedown=null;ig_.obj=null;ig_a.onDragStart=null;ig_a.onDragEnd=null;ig_a.onDrag=null}
,start:function(ig_){var ig_a=ig_l.obj=this.obj;ig_=ig_l.fixE(ig_);if(ig_.which!=1){return true}ig_a.onDragStart();ig_a.lastMouseX=ig_.clientX;ig_a.lastMouseY=ig_.clientY;if(ig_d.O){ig_a.lastMouseY-=document.body.scrollTop}ig_a.Q=window.setInterval(ig_ha(ig_a,ig_pa()),10);document.onmouseup=ig_l.end;document.onmousemove=ig_l.drag;return false}
,drag:function(ig_){ig_=ig_l.fixE(ig_);if(ig_.which==0){return ig_l.end()}var ig_a=ig_l.obj;var ig_b=ig_.clientY;if(ig_d.O){ig_b-=document.body.scrollTop}var ig_c=ig_.clientX;if(ig_a.lastMouseX==ig_c&&ig_a.lastMouseY==ig_b){return false}var ig_e=parseInt(ig_a.style.top);var ig_f=parseInt(ig_a.style.left);var ig_g,ig_h;ig_g=ig_f+ig_c-ig_a.lastMouseX;ig_h=ig_e+ig_b-ig_a.lastMouseY;ig_a.style.left=ig_g+"px";ig_a.style.top=ig_h+"px";ig_a.lastMouseX=ig_c;ig_a.lastMouseY=ig_b;ig_a.onDrag(ig_g,ig_h);return false}

,end:function(ig_){ig_=ig_l.fixE(ig_);document.onmousemove=null;document.onmouseup=null;window.clearInterval(ig_l.obj.Q);var ig_a=ig_l.obj.onDragEnd();ig_l.obj=null;return ig_a}
,fixE:function(ig_){if(typeof ig_=="undefined")ig_=window.event;if(typeof ig_.layerX=="undefined")ig_.layerX=ig_.offsetX;if(typeof ig_.layerY=="undefined")ig_.layerY=ig_.offsetY;if(typeof ig_.which=="undefined")ig_.which=ig_.button;return ig_}
};var _IG_initDrag=function(ig_,ig_a){ig_d.o=ig_;ig_d.q=ig_a;ig_d.P=ig_d.o.tBodies[0].rows[0];ig_d.n=ig_d.P.cells;ig_d.g=new Array;var ig_b=0;for(var ig_c=0;ig_c<ig_d.n.length;ig_c++){var ig_e=ig_d.n[ig_c];if(ig_e.className.indexOf("ig_dnd_fixed_col")!=-1)continue;for(var ig_f=0;ig_f<ig_e.childNodes.length;ig_f++){var ig_g=ig_e.childNodes[ig_f];if(ig_g.tagName=="DIV"){ig_d.g[ig_b]=new ig_Q(ig_g);ig_b++}}}_IG_AddEventHandler("unload",ig_aa)}
;function ig_aa(){for(var ig_=0;ig_<ig_d.g.length;ig_++){ig_d.g[ig_].u();ig_d.g[ig_]=null}ig_d.g=null;ig_d.n=null;ig_d.P=null;ig_d.o=null}
var ig_d=new Object;ig_d.D=navigator.userAgent;ig_d.p=ig_d.D.indexOf("Gecko")!=-1;ig_d.z=ig_d.D.indexOf("Opera")!=-1;ig_d.O=ig_d.D.indexOf("Safari")!=-1;ig_d.H="IG_pageDivMaskId";ig_d.F="IG_moduleDivMaskId";ig_d.v=function(){ig_d.o.style.display="none"}
;ig_d.w=function(){ig_d.o.style.display=""}
;ig_d.k=null;ig_d.l=function(){if(!ig_d.k){ig_d.k=document.createElement("DIV");ig_d.k.className="modbox";ig_d.k.backgroundColor="";ig_d.k.style.border="2px dashed #aaa";ig_d.k.innerHTML="&nbsp;"}return ig_d.k}
;ig_d.h=function(ig_,ig_a){return function(){return ig_[ig_a].apply(ig_,arguments)}
}
;ig_d.V=function(){if(ig_d.q){var ig_=ig_d.q.tBodies[0].rows[0].cells;for(var ig_a=0;ig_a<ig_.length;ig_a++){var ig_b=ig_[ig_a];if(ig_b.className.indexOf("unselectedtab")<0)continue;if(ig_b.style.display=="none")continue;ig_b.isDraggableTo=true;ig_b.pagePosLeft=ig_d.d(ig_b,true);ig_b.pagePosRight=ig_b.pagePosLeft+ig_b.offsetWidth;ig_b.pagePosTop=ig_d.d(ig_b,false);ig_b.pagePosBottom=ig_b.pagePosTop+ig_b.offsetHeight}}}
;ig_d.U=function(ig_){for(var ig_a=0;ig_a<ig_d.g.length;ig_a++){var ig_b=ig_d.g[ig_a];ig_b.a.pagePosLeft=ig_d.d(ig_b.a,true);ig_b.a.pagePosTop=ig_d.d(ig_b.a,false)}var ig_c=ig_.a.nextSibling;while(ig_c){ig_c.pagePosTop-=ig_.a.offsetHeight;ig_c=ig_c.nextSibling}}
;ig_d.d=function(ig_,ig_a){var ig_b=0;while(ig_!=null){ig_b+=ig_["offset"+(ig_a?"Left":"Top")];ig_=ig_.offsetParent}return ig_b}
;ig_d.J=function(ig_){ig_d.B();var ig_a=document.createElement("DIV");ig_a.id=ig_d.H;ig_a.innerHTML="&nbsp;";ig_a.style.position="absolute";ig_a.style.width="100%";ig_a.style.height=document.body.offsetHeight+"px";ig_a.style.left="0px";ig_a.style.top="0px";ig_a.style.backgroundImage="url(http://www.google.com/ig/images/cleardot.gif)";document.body.appendChild(ig_a);if(ig_.W){ig_a=ig_a.cloneNode(true);ig_a.id=ig_d.F;ig_a.style.height=ig_.a.offsetHeight-ig_.c.offsetHeight+"px";ig_a.style.top=ig_.c.offsetHeight+
"px";ig_.a.appendChild(ig_a)}}
;ig_d.B=function(){var ig_=[ig_d.F,ig_d.H];for(var ig_a=0;ig_a<ig_.length;ig_a++){var ig_b=_gel(ig_[ig_a]);if(ig_b){ig_b.parentNode.removeChild(ig_b);ig_b=null}}}
;ig_d.$=function(){var ig_="";for(var ig_a=0;ig_a<ig_d.n.length;ig_a++){var ig_b=ig_d.n[ig_a];for(var ig_c=0;ig_c<ig_b.childNodes.length-1;ig_c++){var ig_e=ig_b.childNodes[ig_c];if(ig_e.tagName=="DIV"){ig_+=ig_!=""?":":"";ig_+=ig_e.id.substring(2)+"_"+ig_b.id.substring(2)}}}_xsetp("mp="+_esc(ig_))}
;function ig_Q(ig_){this._urlMouseUp=ig_W;this._urlClick=ig_V;this._dragStart=ig_T;this._drag=ig_R;this._dragEnd=ig_S;this.x=ig_A;this.r=ig_U;this.u=ig_z;this.e=false;this.a=ig_;this.c=_gel(ig_.id+"_h");this.b=_gel(ig_.id+"_url");this.W=this.a.getElementsByTagName("IFRAME").length>0;if(this.c){this.c.style.cursor="move";ig_l.init(this.c,this.a);this.a.onDragStart=ig_d.h(this,"_dragStart");this.a.onDrag=ig_d.h(this,"_drag");this.a.onDragEnd=ig_d.h(this,"_dragEnd");if(this.b){if(ig_d.p){this.b.onclick=
ig_d.h(this,"_urlClick")}else{this.b.onmouseup=ig_d.h(this,"_urlMouseUp")}}}}
function ig_z(){if(this.c){if(this.b){this.b.onclick=null;this.b.onmouseup=null;this.b=null}ig_l.uninit(this.c,this.a);this.a.onDragStart=null;this.a.onDrag=null;this.a.onDragEnd=null;this.c=null}this.a=null}
function ig_W(ig_){ig_=ig_l.fixE(ig_);if(this.e||!this.b||!this.b.href||ig_.which!=1){return true}this.r("titleclick");if(this.b.target||ig_.shiftKey){window.open(this.b.href,this.b.target)}else{document.location=this.b.href}return false}
function ig_V(ig_){if(!this.e&&this.b&&this.b.href){this.r("titleclick");return true}return false}
function ig_T(){ig_d.U(this);ig_d.V();this.origNextSibling=this.a.nextSibling;var ig_=ig_d.l();var ig_a=this.a.offsetHeight;if(ig_d.p){ig_a-=parseInt(ig_.style.borderTopWidth)*2}var ig_b=this.a.offsetWidth;var ig_c=ig_d.d(this.a,true);var ig_e=ig_d.d(this.a,false);ig_d.v();this.a.style.width=ig_b+"px";ig_.style.height=ig_a+"px";this.a.parentNode.insertBefore(ig_,this.a.nextSibling);this.a.style.position="absolute";this.a.style.zIndex=100;this.a.style.left=ig_c+"px";this.a.style.top=ig_e+"px";ig_d.w(
);ig_d.J(this);this.e=false;return false}
function ig_R(ig_,ig_a){if(!this.e){this.a.style.filter="alpha(opacity=50)";this.a.style.opacity=0.5;this.e=true;this.r("dragstart")}var ig_b=null;var ig_c=100000000;for(var ig_e=0;ig_e<ig_d.g.length;ig_e++){var ig_f=ig_d.g[ig_e];var ig_g=Math.sqrt(Math.pow(ig_-ig_f.a.pagePosLeft,2)+Math.pow(ig_a-ig_f.a.pagePosTop,2));if(ig_f==this)continue;if(isNaN(ig_g))continue;if(ig_g<ig_c){ig_c=ig_g;ig_b=ig_f}}this.j=null;if(ig_d.q){var ig_h=ig_d.q.tBodies[0].rows[0].cells;for(var ig_e=0;ig_e<ig_h.length;ig_e++
){var ig_f=ig_h[ig_e];if(!ig_f.isDraggableTo)continue;if(this.a.lastMouseX>=ig_f.pagePosLeft&&this.a.lastMouseX<=ig_f.pagePosRight&&this.a.lastMouseY>=ig_f.pagePosTop&&this.a.lastMouseY<=ig_f.pagePosBottom){this.j=ig_f;var ig_j=ig_d.l();if(ig_j.parentNode!=null){ig_j.parentNode.removeChild(ig_j)}break}}for(var ig_e=0;ig_e<ig_h.length;ig_e++){var ig_f=ig_h[ig_e];if(ig_f==this.j){if(ig_f.className.indexOf(" tab_hover")<0){ig_f.className+=" tab_hover"}}else{ig_f.className=ig_f.className.replace(/ tab_hover/g,
"")}}}var ig_j=ig_d.l();if(this.j==null&&ig_b!=null&&ig_j.nextSibling!=ig_b.a){ig_b.a.parentNode.insertBefore(ig_j,ig_b.a);if(ig_d.z){document.body.style.display="none";document.body.style.display=""}}}
function ig_S(){ig_d.B();if(this.x()){ig_d.$()}if(this.j){var ig_=this.j.id.match(/tab(\d+)_/)[1];var ig_a=this.a.id.match(/m_(\d+)/)[1];_xsetp("mt_"+ig_a+"="+ig_);this.a.style.display="none";this.j.className=this.j.className.replace(/ tab_hover/g,"")}if(this.e){this.r("dragend")}return true}
function ig_A(){var ig_=false;ig_d.v();this.a.style.position="";this.a.style.width="";this.a.style.zIndex="";this.a.style.filter="";this.a.style.opacity="";var ig_a=ig_d.l();if(ig_a.parentNode!=null){if(ig_a.nextSibling!=this.origNextSibling){ig_a.parentNode.insertBefore(this.a,ig_a.nextSibling);ig_=true}ig_a.parentNode.removeChild(ig_a)}ig_d.w();if(ig_d.z){document.body.style.display="none";document.body.style.display=""}return ig_}
function ig_U(ig_){switch(ig_){case "titleclick":_IG_TriggerModuleEvent(ig_,this.a.id,this.b.href);_IG_TriggerEvent("module"+ig_,this.a.id,this.b.href);break;case "dragstart":case "dragend":_IG_TriggerDelayedModuleEvent(ig_,this.a.id,0);_IG_TriggerDelayedEvent("module"+ig_,0,this.a.id);break}}
function ig_ha(ig_,ig_a){return function(){var ig_b=ig_oa();var ig_c=document.body.scrollTop;var ig_e=4;var ig_f=0.05*ig_b;var ig_g=ig_c;var ig_h=ig_.offsetTop;if(ig_.lastMouseY<=ig_f){ig_h=Math.max(0,ig_.offsetTop-ig_e);ig_g=Math.max(0,ig_c-ig_e)}else if(ig_.lastMouseY>=ig_b-ig_f){ig_h=Math.min(ig_a-ig_.offsetHeight,ig_.offsetTop+ig_e);ig_g=Math.min(ig_a-ig_b,ig_c+ig_e)}var ig_j=ig_g-ig_c;if(ig_j!=0){document.body.scrollTop=ig_g;ig_.style.top=ig_h+"px"}}
}
function ig_oa(){if(window.innerHeight<document.body.clientHeight){return window.innerHeight}return document.body.clientHeight}
function ig_pa(){if(document.body.scrollHeight>document.documentElement.clientHeight){return document.body.scrollHeight}else{return document.documentElement.clientHeight}}
;
function ig_ea(ig_){var ig_a=_gel("mobile_screen");ig_a.insertBefore(ig_,ig_a.firstChild);ig_.style.display="block";var ig_b=ig_ja(ig_.id);var ig_c=_gel("m_"+ig_b);_gel("added_m_"+ig_b).style.display="";_gel("add_m_"+ig_b).style.display="none";ig_c.style.filter="alpha(opacity=30)";ig_c.style.opacity=0.3;if(!ig_d.p){ig_c.style.width="100%"}}
function _addToMobileDisplayAndLog(ig_){ig_ea(ig_);ig_d.C("add")}
function _delFromMobileAndLog(ig_,ig_a,ig_b){ig_ia(ig_,ig_a,ig_b);ig_d.C("del")}
function ig_ia(ig_,ig_a,ig_b){var ig_c=_gel("mobile_m_"+ig_);var ig_e=_gel("m_"+ig_);ig_c.style.display="none";ig_e.style.filter="alpha(opacity=100)";ig_e.style.opacity=1;var ig_f=_gel("add_"+ig_e.id);var ig_g=_gel("added_"+ig_e.id);ig_g.style.display="none";ig_f.style.display=""}
function ig_ja(ig_){if(ig_.indexOf("mobile")==0){return ig_.substring("mobile_m_".length)}return ig_.substring("m_".length)}
function ig_ba(){for(var ig_=0;ig_<ig_d.f.length;ig_++){ig_d.f[ig_].u();ig_d.f[ig_]=null}ig_d.f=null}
ig_d.T=function(ig_){for(var ig_a=0;ig_a<ig_d.f.length;ig_a++){var ig_b=ig_d.f[ig_a];ig_b.a.pagePosLeft=ig_d.d(ig_b.a,true);ig_b.a.pagePosTop=ig_d.d(ig_b.a,false)}var ig_c=ig_.a.nextSibling;while(ig_c){ig_c.pagePosTop-=ig_.a.offsetHeight;ig_c=ig_c.nextSibling}}
;ig_d.C=function(ig_){var ig_a="";var ig_b=_gel("mobile_screen");for(var ig_c=0;ig_c<ig_b.childNodes.length;ig_c++){var ig_e=ig_b.childNodes[ig_c];if(ig_e.style.display!="none"){ig_a+=ig_a!=""?":":"";ig_a+=ig_e.id.substring(9)}}_xsetp("mobile_mp="+_esc(ig_a)+"&action="+_esc(ig_))}
;function ig_M(ig_){this._dragStart=ig_P;this._drag=ig_N;this._dragEnd=ig_O;this.u=ig_z;this.x=ig_A;this.e=false;this.a=ig_;this.a.style.cursor="move";this.c=_gel(ig_.id+"_h");this.K=_gel(ig_.id+"_b");if(this.c){ig_l.init(this.c,this.a)}if(this.K){ig_l.init(this.K,this.a)}this.a.onDragStart=ig_d.h(this,"_dragStart");this.a.onDrag=ig_d.h(this,"_drag");this.a.onDragEnd=ig_d.h(this,"_dragEnd")}
function ig_P(){ig_d.T(this);this.origNextSibling=this.a.nextSibling;var ig_=ig_d.l();var ig_a=this.a.offsetHeight;if(ig_d.p){ig_a-=parseInt(ig_.style.borderTopWidth)*2}ig_.className="";var ig_b=this.a.offsetWidth;var ig_c=ig_d.d(this.a,true);var ig_e=ig_d.d(this.a,false);ig_d.v();this.a.style.width=ig_b+"px";ig_.style.height=ig_a+"px";this.a.parentNode.insertBefore(ig_,this.a.nextSibling);this.a.style.position="absolute";this.a.style.zIndex=100;this.a.style.left=ig_c+"px";this.a.style.top=ig_e+"px"
;ig_d.w();ig_d.J(this);this.e=false;return false}
function ig_N(ig_,ig_a){if(!this.e){this.a.style.filter="alpha(opacity=70)";this.a.style.opacity=0.7;this.e=true}var ig_b=null;var ig_c=100000000;for(var ig_e=0;ig_e<ig_d.f.length;ig_e++){var ig_f=ig_d.f[ig_e];var ig_g=Math.sqrt(Math.pow(ig_-ig_f.a.pagePosLeft,2)+Math.pow(ig_a-ig_f.a.pagePosTop,2));if(ig_f==this)continue;if(isNaN(ig_g))continue;if(ig_g<ig_c){ig_c=ig_g;ig_b=ig_f}}var ig_h=ig_d.l();ig_h.className="";if(ig_b!=null&&ig_h.nextSibling!=ig_b.a){ig_b.a.parentNode.insertBefore(ig_h,ig_b.a)
;if(ig_d.z){document.body.style.display="none";document.body.style.display=""}}}
function ig_O(){ig_d.B();if(this.x()){ig_d.C("move")}return true}
var _IG_initMobileDrag=function(ig_){ig_d.o=ig_;ig_d.f=new Array;var ig_a=0;var ig_b=_gel("mobile_screen").childNodes;for(var ig_c=0;ig_c<ig_b.length;ig_c++){var ig_e=ig_b[ig_c];if(ig_e.tagName=="DIV"){if(ig_qa(ig_e)){ig_ga(ig_e)}ig_d.f[ig_a]=new ig_M(ig_e);ig_a++}}_IG_AddEventHandler("unload",ig_ba)}
;function ig_qa(ig_){var ig_a=_gel(ig_.id+"_h");if(ig_a){var ig_b=_gel(ig_.id+"_h").innerHTML;if(ig_b.indexOf("Google Reader")>=0){return true}}}
function ig_ga(ig_){var ig_a=_gel(ig_.id+"_b");for(var ig_b=0;ig_b<ig_a.childNodes.length;ig_b++){var ig_c=ig_a.childNodes[ig_b];if(ig_c.tagName=="TABLE"){ig_c.className="mmc"}}}
;

