
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/order";
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
	//Frontend Validations
	if(parseInt(formData[1].value)<=0){
	    alert("Please enter a positive value for Quantity");
	    return;
	    }
	if(parseFloat(formData[2].value)<0){
	    alert("Selling price cannot be negative");
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
    	   },
    	   error: handleAjaxError
    	});

	return false;
}

function submit(event){
    var url = getOrderUrl();
	var json = JSON.stringify(jsonData);
	console.log(json);
    if(jsonData.length <1)
        alert("Cannot create an empty order");
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	        var baseUrl = $("meta[name=baseUrl]").attr("content");
	        var redirectUrl = baseUrl + "/ui/order";
	        window.location.href = redirectUrl;
	   		//TODO REDIRECT TO INVOICE GENERATION...
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
    	//Frontend Validations
        	if(parseInt(formData[1].value)<=0){
        	    alert("Please enter a positive value for Quantity");
        	    return;
        	    }
        	if(parseFloat(formData[2].value)<0){
        	    alert("Selling price cannot be negative");
        	    return;
        	    }
    	for(var i in jsonData){
    	    var element = jsonData[i];
    	    if(element.barcode.localeCompare(formData[0].value)==0){
    	        idOfDuplicate = i;
    	        break;
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
           let amount = parseInt(e.quantity) * parseFloat(e.selling_price);
           editButtonHtml = ' <button onclick="displayEditOrderDetail(' + i + ')">Edit</button>';
           deleteButtonHtml = ' <button onclick="deleteOrder(' + i + ')">Delete</button>';
           var row = '<tr>'
           + '<td>' + e.barcode + '</td>' //barcode
           + '<td>'  + e.quantity + '</td>' //mrp
           + '<td>'  + e.selling_price + '</td>' //quantity
           + '<td>'  + amount + '</td>' //total
           + '<td>' + editButtonHtml + '</td>'
           + '<td>' + deleteButtonHtml + '</td>'
           + '</tr>';
            $tbody.append(row)
            sum = sum+amount;
        }
        if(addedData.length>0){
             var totalAmt = '<td>' + ' Total Payable = RS ' + sum  + '</td>';
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
//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#upload-data').click(submit);
	$('#update-order').click(updateOrder);
	var roleElement = document.getElementById('role');
        var role = roleElement.innerText;
        if(role=="operator"){
            document.getElementById("add-product").disabled = true;
            document.getElementById("upload-data").disabled = true;
            document.getElementById("update-order").disabled = true;
        }
}

$(document).ready(init);


