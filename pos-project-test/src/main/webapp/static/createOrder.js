var barcodeMap = new Map();
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

var jsonData = [];
let sum = 0.0;
//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#order-item-form");
	var formData = $form.serializeArray();
	var flag = 0;
	var idOfDuplicate;
	formData[0].value = formData[0].value.toLowerCase().trim();
	formData[1].value = +formData[1].value;
	//Frontend Validations
	if(formData[0].value==null ||formData[0].value==""){
	    warnClick("Barcode cannot be empty");
	    return;
	}
	if(formData[1].value==null || formData[1].value==""){
        warnClick("Quantity cannot be empty");
        return;
    }
	if(parseFloat(formData[1].value)%1!==0){
        warnClick("Please enter a valid integer value for quantity");
        return;
    }
	if(parseInt(formData[1].value)<=0){
	    warnClick("Please enter a positive value for Quantity");
	    return;
	    }
	if(parseInt(formData[1].value)>1000000000){
	    warnClick("Quantity cannot be more than 1000000000");
	    return;
	}
	if(formData[2].value==null || formData[2].value==""){
        warnClick("Selling price cannot be empty");
        return;
    }
	if(parseFloat(formData[2].value)<0){
	    warnClick("Selling price cannot be negative");
	    return;
	    }
	for(var i in jsonData){
	    var element = jsonData[i];
	    if(element.barcode.localeCompare(formData[0].value)==0){
	        flag = 1;
	        idOfDuplicate = i;
	        formData[1].value = parseInt(formData[1].value) + parseInt(element.quantity);
	    }
	}
	var checkingUrl = getOrderUrl() + "/check";
	//creating json
	var json = fromSerializedToJson(formData);
    $.ajax({
    	   url: checkingUrl,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		if(flag!=0){
    	   		    jsonData.splice(idOfDuplicate,1);
    	   		}
    	   		var jsonObject ={barcode: formData[0].value,quantity: parseInt(formData[1].value), selling_price: parseFloat(formData[2].value)}
    	   		jsonData.push(jsonObject);
    	   		updateTable(jsonData);
    	   		$("#order-item-form input[name=barcode]").val("");
    	   		$("#order-item-form input[name=quantity]").val("");
    	   		$("#order-item-form input[name=selling_price]").val("");
    	   },
    	   error: handleAjaxError
    	});

	return false;
}

function submit(event){
    var url = getOrderUrl();
	var json = JSON.stringify(jsonData);
	console.log(json);
    if(jsonData.length <1){
        dangerClick("Cannot create an empty order");
        return;
       }
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        document.getElementById("upload-data").disabled="true";
	        var baseUrl = $("meta[name=baseUrl]").attr("content");
	        console.log(response);
	   		var invoiceUrl = baseUrl+"/api/invoice/"+parseInt(response);
	   		displaySuccessModal(invoiceUrl);
	   },
	   error: handleAjaxError
	});

	return false;
}
function updateOrder(){
    var $form = $("#order-edit-form");
    	var formData = $form.serializeArray();
    	var idOfDuplicate;
    	formData[0].value = formData[0].value.toLowerCase().trim();
    	formData[1].value = +formData[1].value;
    	//Frontend Validations
    	if(formData[1].value==null || formData[1].value==""){
                warnClick("Quantity cannot be empty");
                return;
            }
    	    if(parseFloat(formData[1].value)%1!==0){
    	        dangerClick("Please enter a valid integer value for quantity");
    	        return;
    	    }
        	if(parseInt(formData[1].value)<=0){
        	    dangerClick("Please enter a positive value for Quantity");
        	    return;
        	    }
        if(formData[2].value==null || formData[2].value==""){
                warnClick("Selling price cannot be empty");
                return;
            }
        	if(parseFloat(formData[2].value)<0){
        	    dangerClick("Selling price cannot be negative");
        	    return;
        	    }
        	    console.log("Successful frontend validations");
    	for(var i in jsonData){
    	    var element = jsonData[i];
    	    if(element.barcode.localeCompare(formData[0].value)==0){
    	        idOfDuplicate = i;
    	        break;
    	    }
    	}
    	var checkingUrl = getOrderUrl() + "/check";
    	//creating json
    	console.log(formData);
    	var json = fromSerializedToJson(formData);
        $.ajax({
        	   url: checkingUrl,
        	   type: 'POST',
        	   data: json,
        	   headers: {
               	'Content-Type': 'application/json'
               },
        	   success: function(response) {
        	   		var jsonObject ={barcode: formData[0].value,quantity: parseInt(formData[1].value), selling_price: parseFloat(formData[2].value)}
        	   		jsonData.splice(idOfDuplicate,1, jsonObject);
        	   		updateTable(jsonData);
        	   		$('#edit-order-modal').modal('toggle');
        	   },
        	   error: handleAjaxError
        	});

    	return false;
}


//UI DISPLAY METHODS
function updateTable(addedData){
document.getElementById("upload-data").disabled = false;
var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
    sum=0;
 for(var i in addedData){
           var e = addedData[i];
           var amount = parseInt(e.quantity) * parseFloat(e.selling_price);
           amount = Math.round(amount * 100) / 100;
           editButtonHtml = ' <button class="btn btn-outline-info" onclick="displayEditOrderDetail(' + i + ')">Edit</button>';
           deleteButtonHtml = ' <button class="btn btn-outline-danger" onclick="deleteOrder(' + i + ')">Delete</button>';
           var row = '<tr>'
           + '<td>' + e.barcode + '</td>' //barcode
           + '<td>'  + e.quantity + '</td>' //mrp
           + '<td>'  + (Math.round(parseFloat(e.selling_price)*100)/100).toFixed(2) + '</td>' //quantity
           + '<td>Rs '  + amount.toFixed(2) + '</td>' //total
           + '<td>' + editButtonHtml + '&nbsp;&nbsp;'
           + ' ' + deleteButtonHtml + '</td>'
           + '</tr>';
            $tbody.append(row)
            sum = sum+amount;
        }
        sum = Math.round(sum * 100) / 100
        if(addedData.length>0){
             var totalAmt = '<td>' + ' Total Payable = Rs ' + sum.toFixed(2)  + '</td>';
              $tbody.append(totalAmt);
        }
}
function displayEditOrderDetail(i){
    data = jsonData[i];
    $("#order-edit-form input[name=barcode]").val(data.barcode);
    $("#order-edit-form input[name=quantity]").val(data.quantity);
    $("#order-edit-form input[name=selling_price]").val(data.selling_price);
    $('#edit-order-modal').modal('toggle');
}

function deleteOrder(id){
    jsonData.splice(id,1);
    updateTable(jsonData);
}

function fromSerializedToJson(serialized){
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function refresh(){
    location.reload(true);
}
function checkMrp(){
    var barcode =  document.getElementById("inputBarcode").value;
    if(barcodeMap.get(barcode))
        $("#order-item-form input[name=selling_price]").val(barcodeMap.get(barcode));
    else
        $("#order-item-form input[name=selling_price]").val(0);

}
function getProducts(){
    var url = $("meta[name=baseUrl]").attr("content") + "/api/product";
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		populateBarcodeSet(data);
    	   },
    	   error: handleAjaxError
    	});
}
function populateBarcodeSet(data){
    data.forEach(function(item) {
           barcodeMap.set(item.barcode, item.mrp);
        });
}
function displaySuccessModal(invoiceUrl){
    document.getElementById("invoice-url").innerText=invoiceUrl;
    jsonData = [];
    sum=0.0;
    var $tbody = $('#order-item-table').find('tbody');
    $tbody.empty();
    $('#successModal').modal('show');
}
function redirectToOrders(){
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    var redirectUrl = baseUrl + "/ui/order";
    window.location.href = redirectUrl;
}
function downloadInvoice(){
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    var invoiceUrl = document.getElementById("invoice-url").innerText;
    var redirectUrl = baseUrl + "/ui/order";
    window.open(invoiceUrl);
    window.location.href = redirectUrl;
}
//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#upload-data').click(submit);
	$('#update-order').click(updateOrder);
	$('#redirect-to-orders').click(redirectToOrders);
	$('#download-invoice').click(downloadInvoice);
	$('#inputBarcode').on('change', checkMrp);

	$("#order-item-form input[name=selling_price]").val(0);
}
$(document).ready(init);
$(document).ready(getProducts);


