
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}
function getInvoiceUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/invoice"
}
//BUTTON ACTIONS

//UI DISPLAY METHODS
function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}
function displayOrderList(data){
    var $tbody = $('#order-table').find('tbody');
    	$tbody.empty();
    	for(var i in data){
    		var e = data[i];
    		var buttonHtml = ' <button onclick="displayParticularOrder(' + e.id + ')">View Order</button>'
    		var minute = '00';
    		var hour = '00';
    		var date = '00';
    		var month = '00';
    		(parseInt(e.date_time[4])<10)?minute='0'+e.date_time[4] : minute=e.date_time[4];
    		(parseInt(e.date_time[3])<10)?hour='0'+e.date_time[3] : hour=e.date_time[3];
    		(parseInt(e.date_time[2])<10)?date = '0'+e.date_time[2] : date = e.date_time[2];
    		(parseInt(e.date_time[1])<10)?month='0'+e.date_time[1] : month=e.date_time[1];
    		var row = '<tr>'
    		+ '<td>' + e.id + '</td>'
    		+ '<td>' + date+'-'+month+'-'+e.date_time[0]+',  '
    		         + hour+':'+minute +'</td>'
    		+ '<td>' + buttonHtml + '</td>'
    		+ '</tr>';
            $tbody.append(row);
    	}
}



function displayParticularOrder(id){
    var url = getOrderUrl()+"/"+id;
    	$.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	        $('#view-order-modal').modal('toggle');
    	   		displayOrderItems(data);
    	   },
    	   error: handleAjaxError
    	});
}
function displayOrderItems(data){
    var $tbody = $('#order-view').find('tbody');
    $tbody.empty();
    let sum = 0;
    for(var i in data){
        var e = data[i];
        var amount = parseInt(e.quantity) * parseFloat(e.selling_price);
        amount = Math.round(amount * 100) / 100
        var row = '<tr>'
        + '<td>' + e.barcode + '</td>'
        + '<td>' + e.name + '</td>'
        + '<td>' + e.quantity + '</td>'
        + '<td>Rs ' + e.selling_price + '</td>'
        + '<td>Rs ' + amount + '</td>'
        + '</tr>';
        $tbody.append(row);
        sum = sum+amount;
    }
    sum = Math.round(sum * 100) / 100
    var totalAmt = '<tr><td>' + ' Total Price:  </td><td>Rs ' + sum  + '</td></tr>';
    $tbody.append(totalAmt);
    var helper = '<td><span id="helper" hidden="hidden">'+e.order_id+'</span></td>';
    $tbody.append(helper);
}
function downloadInvoice(){
    var helper = document.getElementById('helper').innerText;
    console.log(helper);
    var invoiceUrl = getInvoiceUrl() + "/"+parseInt(helper);
    window.open(invoiceUrl);
}
function refresh(){
    location.reload(true);
}
//INITIALIZATION CODE
function init(){
    $('#download-invoice').click(downloadInvoice);
    var roleElement = document.getElementById('role');
    var role = roleElement.innerText;

    if(role=="operator"){
        document.getElementById("adminAccess").innerHTML = "";
    }
}
$(document).ready(getOrderList);
$(document).ready(init);



