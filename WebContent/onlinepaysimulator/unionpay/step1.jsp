<%@ page language="java" import="java.util.*,java.math.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String base = request.getContextPath();
	String orderNumber = request.getParameter("orderNumber");
	String orderAmount = request.getParameter("orderAmount");
	BigDecimal bd =new BigDecimal(orderAmount); 
	orderAmount = bd.divide(new BigDecimal(100)).setScale(2).toString();
%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>银联在线支付-银行卡综合性网上交易转接清算平台！</title>
<meta http-equiv="X-UA-Compatible" content="requiresActiveX=true" />
<meta name="renderer" content="webkit">
<meta name="keywords"
	content='银联,银联在线,中国银联,银联支付,银联在线支付,中国银联在线,中国银联在线支付,银联支付平台' />
<meta
	content='银联在线支付是中国银联联合商业银行共同推出的集成化、综合性、开放性网上支付平台,全面支持各类型银联卡,可为银联卡持卡人的购物缴费、商旅预定、慈善捐款、转账还款等提
供安全、快捷、多选择、全球化的支付服务.'
	name="description" />

<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
<link href='<%=base%>/resources/paymentsimulator/css/up.global.upop.css'
	rel="stylesheet" type="text/css" />
<link href='<%=base%>/resources/paymentsimulator/css/up.pos.css'
	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=base%>/resources/shop/js/jquery.js"></script>
<script>
$(function(){
	$("#cardNumberTip").hide();
});

function validateCard(){
	var cardNumber = $("#cardNumber").val();
	if(cardNumber.length<19){
		$("#cardNumberTip").show();
		return false;
	}else{
		$("#cardNumberTip").hide();
		return true;
	}
}
function submitForm(){
	if(validateCard()){
		$("#cardPayForm").submit();
	}
}
</script>

</head>

<body>

	<div id="upopNoticeInfo2" style="width: 950px; margin: 0 auto;">
		<div id="upgg_location-63"></div>
	</div>
	<div class="header_wrap">
		<div class="header">
			<div class="fl">
				<a id="header-online-unionpay-href-id2"
					href="https://www.95516.com/" target="_blank" class="logo_unionPay">银联在线支付</a>
				<div id="upgg_location-87"></div>
			</div>
			<div class="fr">
				<p class="userlogin">
					<a id="header-online-unionpay-href-id"
						href="https://www.95516.com/" target="_blank">首页</a>|<a
						id="header-help-center-id"
						href="https://static.95516.com/static/help/index.html"
						target="_blank">帮助中心</a> <em>|</em>
				<div class="select_box" locale="en_US">
					<span id="laguage_select" class="laguage_select" locale="en_US"
						title="Change language&#13;更改语言">English</span>
					<ul class="select_item
 dn">
						<li><a href="#" locale="zh_TW">繁體中文</a></li>
						<li><a href="#" locale="ja_JP">日本語</a></li>
						<li><a href="#" locale="ko_KR">한국어</a></li>
						<li><a href="#" locale="fr_FR">Français</a></li>
						<li><a href="#" locale="es_ES">Español</a></li>
						<li><a href="#" locale="ru_RU">русский</a></li>
						<li><a href="#" locale="pt_PT">Português</a></li>
						<li><a href="#" locale="ar_AR">عربي </a></li>
					</ul>
				</div>
				</p>
				<strong>24小时客服热线95516</strong>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		window._UPOP_ENTER_TIME = new Date().getTime();
	</script>
	<div class="content
 clearfix">






		<div class="order clearfix">
			<input type="hidden" id="merCode" name="merCode"
				value="898111148990267" />



			<!--消费方式  todo transType.getCode-->

			<div class="order_main">
				<p class=" order_sum ">
					<span class="item_title"> 订单金额： </span><em class="order_money"><%=orderAmount%></em>
					<em id="pay-currency-type"> 元 </em>
				</p>
				<p class=" order_num " title="38171119371450439607">订单编号：<%=orderNumber %></p>
				<p class="fl txt_over" title="天购网">
					<span class="item_title">商户名称：</span><img
						src="https://static.95516.com/static/merchant/logos4gateway/898111148990267.gif"
						onerror="this.style.display
='none';document.getElementById('merchantNameDisplay_id').style.width='240px';" /><em
						id="merchantNameDisplay_id">天购网</em>
				</p>

				<!-- <div class="order_flag">
					<a id="order-details-down-id" class="down">订单详情<span>[+]</span></a><a
						id="order-details-up-id" class="up dn">订单详情<span>[-]</span></a>
				</div> -->


			</div>

			<div class="order_detail dn">
				<div class="order_others">

					<p class=" order_date ">交易日期：2015-12-18</p>
					<p class=" order_sort ">
						交易种类：
						<!--<p:enum instance="01" />  todo-->
						直接消费
					</p>
					<p class="order_mon">交易币种： 人民币</p>
				</div>
				<div class="order_jigou">本商户由银联合作机构提供收单服务：北京银联商务</div>
			</div>



			<div class="upgg_location_242_class">
				<div id="upgg_location-242"></div>
			</div>



		</div>
		<div class="enter clearfix">
			<div class="blue_title">
				<span class="blue_lf"></span>
				<div class="pay_tab">
					<span class="tab_lf"></span><span class="tab_txt">银联卡支付</span><span
						class="tab_rt"></span>
				</div>


				<span class="blue_rt"></span>
			</div>
			<div class="tab_content index_bg">
				<div class="ct_bg clearfix">
					<div class="clear"></div>
					<form action="step2.jsp?orderNumber=<%=orderNumber %>&orderAmount=<%=orderAmount%>" id="cardPayForm" method="post">
						<input type="hidden" name="machineInfo" id="machineInfo_bankcard" />
						<input type="hidden" name="machineInfoDecodeKey"
							id="machineInfoDecodeKey" value="wrXEvuHe" />
						<div class="enter_box fl">
							<div class="box_content box_rt clearfix">
								<div class="info_line" style="padding-bottom: 10px;">
									<div class="label_item card_title">
										<b>直接付款</b>
									</div>
								</div>
								<div class="info_line">
									<div class="pay_proce"></div>
								</div>
								<div class="info_line">
									<div style="padding-left: 70px; position: relative;"
										class="ipt_box bankNumber">
										<input
											style="width: 324px; height: 43px; line-height: 43px; font-size: 14px; font-family: Arial, Helvetica, sans-serif;"
											type="text" name="cardNumber" class="ipt_txt note put_bank"
											autocomplete="off" maxlength="19" id="cardNumber" /><span
											class="close_val dn"></span>

									</div>
									<div class="clear"></div>
									<div id="cardNumberTip"
										style="margin: 0px; padding: 0px; background: transparent none repeat scroll 0% 0%;">
										<div class="note_info user txt_error_index">卡号位数不足，请您核对</div>
									</div>
								</div>
								<div class="btn_line" style="padding-left: 70px;">
									<input type="button" class="btn_blue" value='下一步' id="btnNext" onclick="submitForm();" />


								</div>
							</div>
						</div>
					</form>
					<form
						action="step2.jsp"
						id="loginForm" method="post">

						<div id="loginDiv" class="enter_box fr">
							<div class="pay_qa"
								style="border-top: 1px solid #ddd; padding-top: 15px;">
								<h2>支付遇到问题？</h2>
								<dl>
									<dt>1.如果我没有注册，如何进行付款？</dt>
									<dd style="display: block">答：如果您没有注册账户，您可以在页面左侧直接输入卡号，点击“下一步”进行付款。您输入的银行卡信息需通过发卡行验证后才能完成交易，安全
										又便捷。</dd>
									<dt>2.为什么要安装控件？控件不能下载，我该如何解决？</dt>
									<dd>
										答：安装输入控件可以保障您输入信息的安全，防止密码被窃取；点击密码输入框中的提示链接或<a
											id="upeditor-download-id" class="installUPEditor">这里</a>下载安装。
									</dd>
									<dt>3.付款时，遇到提示"网站安全证书有问题"，我该如何解决？</dt>
									<dd>
										答：您需要点击<a id="basic-certification-download-id"
											href="https://static.95516.com/static/basis
/cer/root.zip"
											target="_blank">这里</a>重新安装浏览器根证书，安装具体过程可以点击<a
											href="https://static.95516.com/static/page/help2/help/detail_16.html#active_mail_1"
											target="_blank">这里</a>到帮助中心中查看。浏览器根证书可用来保障您的网络购物环境更安全，让您的银联在线支付付款体验更顺畅。
									</dd>

								</dl>
								<p class="pay_qa_more">
									如需查看更多问题，请点击<a id="help-center-id" target="_blank"
										href="https://static.95516
.com/static/help/index.html">帮助中心</a>
									或联系<a id="unionpay-customer-service-url-id" target="_blank"
										href="https://95516.unionpay.com
/web/icc/chat/chat?c=1&s=4&st=1&depid=ff8080813daaa230013de380641900ec&">在线客服</a>
								</p>
							</div>
							<div class="reg_pop dn">
								<p class="ebank_title">快速注册</p>
								<div class="ebank_link">
									<p style="padding: 0 0 20px 95px;">请在新开页面完成注册</p>
									<input id="reg_pop_button" type="button"
										class="btn_blue btn_reg" value='注册成功，继续支付'
										style="margin-left: 90px" /> <a id="reg_pop_cancelbtn"
										style="font-size: 12px; color: #497DA4; padding-left: 10px;">取消注册</a>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="sup_bank" id="bankSupportList"></div>
		<div class="ad_index clearfix">
			<div id="upgg_location-42"></div>
			<div id="upgg_location-43"></div>
		</div>





	</div>

	<script type="text/javascript"
		src='https://static.95516.com/gw/b2c/resources/upop/js/up/up.js?v=7dd4225b8fb8fec44feb204e1eacd87'></script>




	<div class="footer">
		<div>
			<a id="china-unionpay-href-id" href="http://cn.unionpay.com/"
				target="_blank">银联官网</a>|<a id="footer-online-unionpay-href-id"
				href="https://www.95516.com/" target="_blank">银联在线支付</a>|<a
				id="online-unionpay-about-href-id"
				href="http://static.95516.com/static/page/177.html" target="_blank">关于我们</a>|<a
				id="online-unionpay-terms-href-id"
				href="http://static.95516.com/static/page/183.html" target="_blank">网站使用条款</a>
		</div>
		<p>
			中国银联股份有限公司版权所有 &copy; 2002-2015 <em>沪ICP备07032180号</em>
		</p>
	</div>

	<div class="dn" id="hidden_area">
		<input type="hidden" name="devTrace" value="[151.50, 151.50]">
		<input type="hidden" name="upop_release_version" value="r_1_0_0">
		<div class="dn" id="upeditorMachineInfo_area"></div>
		<input type="hidden" name="secureKey_id" id="secureKey_id"
			value="wrXEvuHe">
	</div>
	<div id="upeditorMachineInfo_area"></div>



	</script>
</body>
</html>




<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网关前置登陆页面支持银行列表-银联在线支付</title>
</head>

<body>
	<style type="text/css">
html {
	color: #333;
	background: #fff;
}

body {
	font: 12px/1.5 tahoma, arial, \5b8b\4f53;
	margin: 0;
	padding: 0;
	background: #fff;
}

p, div, span {
	margin: 0;
	padding: 0;
}
/*支持银行*/
.sup_title {
	border-top: 1px dashed #c3c3c3;
	border-bottom: 1px dashed #c3c3c3;
	height: 34px;
	line-height: 34px;
	background: #ededed;
	width: 100%;
	margin-top: 10px;
	text-indent: 20px;
}

.sup_title p {
	float: left;
}

.sup_title p span {
	font-family: Arial, Helvetica, sans-serif;
	color: #ff7000;
	font-size: 14px;
	margin-right: 2px;
}

.sup_title a {
	color: #4b7caf;
	text-decoration: none;
}

.sup_title a:hover {
	text-decoration: underline;
}

.ibanks {
	width: 940px;
	padding: 20px 0 0 10px;
}

.ibanks span {
	float: left;
	height: 23px;
	line-height: 23px;
	margin: 0 15px 15px 0;
	color: #999;
	display: inline-block;
	padding-left: 30px;
	position: relative;
}

.ibanks span a {
	width: 15px;
	height: 17px;
	display: inline-block;
	background: url(https :// online.unionpay.com/ static/ cms/ img/ 25/
		559a31d1-6932-4988-a416-bdb17398b5a5 .gif) no-repeat 0 -732px;
	position: absolute;
	top: -5px;
	right: -14px;
	text-indent: -100px;
	overflow: hidden;
}

.ibanks .bank_logo {
	background: url(https :// online.unionpay.com/ static/ cms/ img/ 25/
		559a31d1-6932-4988-a416-bdb17398b5a5 .gif) no-repeat 0 0;
}

.ibanks .boc {
	background-position: 5px -24px;
}

.ibanks .ceb {
	background-position: 0 -618px;
}

.ibanks .cmbc {
	background-position: 5px -47px;
}

.ibanks .hxb {
	background-position: 5px -411px;
}

.ibanks .beijing {
	background-position: 5px -289px;
}

.ibanks .icbc {
	background-position: 5px -127px;
}

.ibanks .abc {
	background-position: 5px 2px;
}

.ibanks .ccb {
	background-position: 5px -73px;
}

.ibanks .pingan {
	background-position: 0 -646px;
}

.ibanks .spd {
	background-position: 5px -182px;
}

.ibanks .gdb {
	background-position: 5px -320px;
}

.ibanks .sdb {
	background-position: 5px -349px;
}

.ibanks .cmb {
	background-position: 5px -209px;
}

.ibanks .shanghai {
	background-position: 5px -382px;
}

.ibanks .citic {
	background-position: 5px -239px;
}

.ibanks .cib {
	background-position: 5px -264px;
}

.ibanks .psbc {
	background-position: 5px -155px;
}

.ibanks .comm {
	background-position: 5px -674px;
}

.ibanks .nbcb {
	background-position: 5px -527px;
}

.ibanks .hkbea {
	background-position: 5px -560px;
}

.ibanks .citi {
	background-position: 2px -592px;
}
</style>
	<!--S 支持银行-->
	<div class="sup_title">
		<p>
			支持<span>200</span>多家银行，<a
				href="https://online.unionpay.com/static/page/613.html"
				target="_blank">点此查看更多</a>
		</p>
	</div>
	<div class="ibanks">
		<span class="bank_logo icbc">工商银行</span> <span class="bank_logo abc">农业银行</span>
		<span class="bank_logo boc">中国银行</span> <span class="bank_logo ccb">建设银行</span>
		<span class="bank_logo comm">交通银行</span> <span class="bank_logo psbc">邮储银行</span>
		<span class="bank_logo citic">中信银行</span> <span class="bank_logo ceb">光大银行</span>
		<span class="bank_logo hxb">华夏银行</span> <span class="bank_logo cmbc">民生银行</span>
		<span class="bank_logo gdb">广发银行</span> <span class="bank_logo sdb">深发银行</span>
		<span class="bank_logo cmb">招商银行</span> <span class="bank_logo cib">兴业银行</span>
		<span class="bank_logo spd">浦发银行</span> <span class="bank_logo pingan">平安银行</span>
		<span class="bank_logo beijing">北京银行</span> <span
			class="bank_logo shanghai">上海银行</span> <span class="bank_logo nbcb">宁波银行</span>
		<span class="bank_logo citi">花旗银行</span>
	</div>
	<!--E 支持银行-->
</body>
</html>