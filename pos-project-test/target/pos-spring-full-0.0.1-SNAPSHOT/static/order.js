var table;
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
table.row.add(["","<i class='fa fa-refresh fa-spin'></i>",""]).draw();
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
    	table.clear().draw();
    	var dataRows = [];
    	for(var i in data){
    		var e = data[i];
    		var buttonHtml = ' <button class="btn btn-outline-info" onclick="displayParticularOrder(' + e.id + ')">View Order</button>'
    		var minute = '00';
    		var hour = '00';
    		var date = '00';
    		var month = '00';
    		(parseInt(e.date_time[4])<10)?minute='0'+e.date_time[4] : minute=e.date_time[4];
    		(parseInt(e.date_time[3])<10)?hour='0'+e.date_time[3] : hour=e.date_time[3];
    		(parseInt(e.date_time[2])<10)?date = '0'+e.date_time[2] : date = e.date_time[2];
    		(parseInt(e.date_time[1])<10)?month='0'+e.date_time[1] : month=e.date_time[1];
    		var formattedDate =date+'-'+month+'-'+e.date_time[0]+',  '+ hour+':'+minute;
    		dataRows.push([e.id, formattedDate, buttonHtml]);
    	}
    	table.rows.add(dataRows).draw();
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
    var maxLength=25;
    let sum = 0;
    for(var i in data){
        var e = data[i];
        var amount = parseInt(e.quantity) * parseFloat(e.selling_price);
        amount = Math.round(amount * 100) / 100
        var name = (e.name.length>maxLength)?e.name.substring(0,maxLength)+'...':e.name;
        var row = '<tr>'
        + '<td>' + e.barcode + '</td>'
        + '<td>' + name + '</td>'
        + '<td>' + e.quantity + '</td>'
        + '<td>Rs ' + (Math.round(parseFloat(e.selling_price)*100)/100).toFixed(2) + '</td>'
        + '<td>Rs ' + amount.toFixed(2) + '</td>'
        + '</tr>';
        $tbody.append(row);
        sum = sum+amount;
    }
    sum = Math.round(sum * 100) / 100
    var totalAmt = '<tr style="border-top: 2px solid"><td></td><td></td><td></td><td>' + '<b> Total Price:  <b></td><td>Rs ' + sum.toFixed(2)  + '</td></tr>';
    $tbody.append(totalAmt);
    var helper = '<td><span id="helper" hidden="hidden">'+e.order_id+'</span></td>';
    $tbody.append(helper);
}
function downloadInvoice(){
    var helper = document.getElementById('helper').innerText;
    console.log(helper);
    var invoiceUrl = getInvoiceUrl() + "/"+parseInt(helper);
    window.open(invoiceUrl);
    successClick("Invoice Downloaded");
}
//INITIALIZATION CODE
function init(){
    $('#download-invoice').click(downloadInvoice);
    table = $('#order-table').DataTable(
                                        {order: [[0, 'desc']],
                                        'columnDefs': [ {'targets': [2],'orderable': false },
                                            {'targets': [0,1,2], "className": "text-center"}],
                                            searching: false,
                                            info:false,
                                            lengthMenu: [
                                                             [15, 25, 50, -1],
                                                             [15, 25, 50, 'All']
                                            ],
                                        deferRender: true}
    );
}
$(document).ready(init);
$(document).ready(getOrderList);




