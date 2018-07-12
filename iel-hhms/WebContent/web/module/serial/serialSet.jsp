<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
</head>
<body>
	<div class="wrapper">
		<div class="pageContent">
			<form action="" class="form-horizontal form-input">
				<fieldset>
				    <legend>串口设置</legend> 
					<div class="control-group cols4">
						<label class="control-label">串口号：</label>
						<div class="controls">
						   <select id="com" name="serialData.com" >
                             <option value="COM1">COM1</option>
                             <option value="COM2">COM2</option>
                             <option value="COM3" selected="selected">COM3</option>
                             <option value="COM4">COM4</option>
                             <option value="COM5">COM5</option>
                             <option value="COM6">COM6</option>
                             <option value="COM7">COM7</option>
                             <option value="COM8">COM8</option>
                           </select>
						</div>
						<label class="control-label">波特率：</label>
						<div class="controls">
						   <select id="baudRate" name="serialData.baudRate" >
                             <option value="4800">4800</option>
                             <option value="9600">9600</option>
                             <option value="14400">14400</option>
                             <option value="19200">19200</option>
                             <option value="38400">38400</option>
                             <option value="56000">56000</option>
                             <option value="57600">57600</option>
                             <option value="115200" selected="selected">115200</option>
                           </select>
						</div>
					</div>
					<div class="control-group cols4">
						<label class="control-label">数据位：</label>
						<div class="controls">
						   <select id="stopBit" name="serialData.stopBit" >
                             <option value="1 bit" selected="selected">1 bit</option>
                             <option value="1.5 bit">1.5 bit</option>
                             <option value="2 bit">2 bit</option>
                           </select>
						</div>
						<label class="control-label">停止位：</label>
						<div class="controls">
						   <select id="stopBit" name="serialData.stopBit" >
                             <option value="5 bit">5 bit</option>
                             <option value="6 bit">6 bit</option>
                             <option value="7 bit">7 bit</option>
                             <option value="8 bit" selected="selected">8 bit</option>
                           </select>
						</div>			
					</div>
				    <div class="control-group cols4">
					    <label class="control-label">校验位：</label>
						<div class="controls">
						   <select id="checkBit" name="serialData.checkBit" >
                             <option value="NONE" selected="selected">NONE</option>
                             <option value="ODD">ODD</option>
                             <option value="EVEN">EVEN</option>
                             <option value="MARK">MARK</option>
                             <option value="SPACE">SPACE</option>
                           </select>
						</div>
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_filtering">连接测试</a>	
						</div>	
					</div>
				</fieldset>
				<fieldset>
					<legend>读数据</legend> 
					<div class="control-group cols4">
						<label class="control-label">设备类系：</label>
						<div class="controls">
						 
                        </div>	
                        <label class="control-label">设备编号：</label>
						<div class="controls">
						 
                        </div>		
					</div>
					 <div class="control-group cols4" >
					    <label class="control-label">读取时间：</label>
						<div class="controls">
                        </div>	
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_filtering">获取</a>	
						</div>	
					</div>
				</fieldset>
				<fieldset>
					<legend>写数据</legend> 
					<div class="control-group cols4 input">
						<label class="control-label"><em>*</em>设备类型：</label>
						 <div class="controls">
						  <select id="deviceType" name="serialData.deviceType" required="true">
                             <option value="1" selected="selected">胸牌</option>
                             <option value="2">床识别器</option>
                             <option value="3">门识别器</option>
                          </select>
                         </div>		
					     
					     <label class="control-label"><em>*</em>设备编号：</label>
						<div class="controls">
							<input maxLength=6 minLength=6 id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.name"/>'>
						</div>		
					</div>
					<div class="control-group cols4" >
					    <label class="control-label">设置时间：</label>
						<div class="controls">
                        </div>	
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_filtering">设置</a>	
						</div>	
					</div>
				</fieldset>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="js/serialSet.js"></script>
</body>
</html>
