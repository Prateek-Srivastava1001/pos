
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
	if(formData[0].value==null ||formData[0].value==""){
	    alert("Barcode cannot be empty");
	    return;
	}
	if(formData[2].value==null || formData[2].value==""){
	    alert("Selling price cannot be empty");
	}
	if(parseFloat(formData[1].value)%1!==0){
        alert("Please enter a valid integer value for quantity");
        return;
    }
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
    if(jsonData.length <1){
        alert("Cannot create an empty order");
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
	        var redirectUrl = baseUrl + "/ui/order";
	   		var invoiceUrl = baseUrl+"/api/invoice/"+parseInt(response);
	   		window.open(invoiceUrl);
	   		window.location.href = redirectUrl;

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
    	    if(parseFloat(formData[1].value)%1!==0){
    	        alert("Please enter a valid integer value for quantity");
    	        return;
    	    }
        	if(parseInt(formData[1].value)<=0){
        	    alert("Please enter a positive value for Quantity");
        	    return;
        	    }
        	if(parseFloat(formData[2].value)<0){
        	    alert("Selling price cannot be negative");
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
           editButtonHtml = ' <button onclick="displayEditOrderDetail(' + i + ')">Edit</button>';
           deleteButtonHtml = ' <button onclick="deleteOrder(' + i + ')">Delete</button>';
           var row = '<tr>'
           + '<td>' + e.barcode + '</td>' //barcode
           + '<td>'  + e.quantity + '</td>' //mrp
           + '<td>'  + Math.round(parseFloat(e.selling_price)*100)/100 + '</td>' //quantity
           + '<td>Rs '  + amount + '</td>' //total
           + '<td>' + editButtonHtml + '</td>'
           + '<td>' + deleteButtonHtml + '</td>'
           + '</tr>';
            $tbody.append(row)
            sum = sum+amount;
        }
        sum = Math.round(sum * 100) / 100
        if(addedData.length>0){
             var totalAmt = '<td>' + ' Total Payable = Rs ' + sum  + '</td>';
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


